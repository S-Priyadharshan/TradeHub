package com.pd.portfolio_service.service.impl;

import com.pd.portfolio_service.client.MarketServiceClient;
import com.pd.portfolio_service.domain.dto.*;
import com.pd.portfolio_service.domain.entity.Holding;
import com.pd.portfolio_service.domain.entity.Portfolio;
import com.pd.portfolio_service.domain.entity.Trade;
import com.pd.portfolio_service.domain.enums.TradeType;
import com.pd.portfolio_service.domain.event.UserRegisteredEvent;
import com.pd.portfolio_service.exception.*;
import com.pd.portfolio_service.mapper.PortfolioMapper;
import com.pd.portfolio_service.repository.HoldingRepository;
import com.pd.portfolio_service.repository.PortfolioRepository;
import com.pd.portfolio_service.repository.TradeRepository;
import com.pd.portfolio_service.service.PortfolioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final TradeRepository tradeRepository;

    private final MarketServiceClient marketServiceClient;

    private final PortfolioMapper portfolioMapper;

    private final KafkaTemplate<String,TradeEvent> kafkaTemplate;

    @Value("${BASE_CASH_BALANCE}")
    private BigDecimal baseCashBalance;

    @Override
    @Transactional
    public void createPortfolio(UserRegisteredEvent event) {
        if (portfolioRepository.existsById(event.userId())) {
            return;
        }

        Portfolio portfolio = Portfolio.builder()
                .userId(event.userId())
                .cashBalance(baseCashBalance)
                .build();

        portfolioRepository.save(portfolio);
    }

    @Override
    @Transactional
    public TradeResponse executeBuy(TradeRequest tradeRequest) {
        SymbolValidationResponse validationResponse = marketServiceClient.validateSymbol(tradeRequest.symbol());

        if (!validationResponse.valid()) {
            throw new InvalidSymbolException("Invalid symbol");
        }

        QuoteResponse quoteResponse = marketServiceClient.getQuote(tradeRequest.symbol());

        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Portfolio portfolio = portfolioRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found"));

        BigDecimal totalPrice = tradeRequest.quantity().multiply(quoteResponse.currentPrice());

        if (portfolio.getCashBalance().compareTo(totalPrice) < 0) {
            throw new InsufficientFundsException("Insufficient Funds");
        }

        portfolio.setCashBalance(portfolio.getCashBalance().subtract(totalPrice));

        Holding holding = holdingRepository.findByPortfolioIdAndSymbol(portfolio.getUserId(), tradeRequest.symbol())
                .orElseGet(() -> {
                    return Holding.builder()
                            .portfolioId(portfolio.getUserId())
                            .symbol(tradeRequest.symbol())
                            .quantity(BigDecimal.ZERO)
                            .averageCostBasis(BigDecimal.ZERO)
                            .build();
                });

        holding.computeAverageCostBasis(totalPrice, tradeRequest.quantity());
        holding.increaseQuantity(tradeRequest.quantity());

        Trade trade = new Trade(UUID.fromString(userId),
                tradeRequest.symbol(),
                TradeType.BUY,
                tradeRequest.quantity(),
                quoteResponse.currentPrice(),
                totalPrice,
                LocalDateTime.now(ZoneId.systemDefault()));

        Trade savedTrade = tradeRepository.save(trade);

        TradeEvent tradeEvent = portfolioMapper.toTradeEvent(savedTrade);

        kafkaTemplate.send("trade-event",savedTrade.getTradeId().toString(),tradeEvent);

        return new TradeResponse(savedTrade.getTradeId(),
                savedTrade.getSymbol(),
                savedTrade.getTradeType(),
                savedTrade.getQuantity(),
                savedTrade.getPricePerUnit(),
                savedTrade.getTotalAmount(),
                portfolio.getCashBalance(),
                savedTrade.getExecutedAt()
        );
    }

    @Override
    @Transactional
    public TradeResponse executeSell(TradeRequest tradeRequest) {

        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Holding holding = holdingRepository.findByPortfolioIdAndSymbol(UUID.fromString(userId), tradeRequest.symbol())
                .orElseThrow(() -> new HoldingNotFoundException(
                        String.format("Holding for user %s with symbol %s not found", userId, tradeRequest.symbol()))
                );

        if (holding.getQuantity().compareTo(tradeRequest.quantity()) < 0) {
            throw new InsufficientSharesException("User does not have sufficient shares");
        }

        Portfolio portfolio = portfolioRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new PortfolioNotFoundException(
                        String.format("Porfolio of user with id %s not found", userId))
                );

        QuoteResponse quoteResponse = marketServiceClient.getQuote(tradeRequest.symbol());

        BigDecimal totalPrice = tradeRequest.quantity().multiply(quoteResponse.currentPrice());

        portfolio.setCashBalance(portfolio.getCashBalance().add(totalPrice));

        holding.decreaseQuantity(tradeRequest.quantity());

        if (holding.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            holdingRepository.deleteById(holding.getId());
        }

        Trade trade = new Trade(UUID.fromString(userId),
                tradeRequest.symbol(),
                TradeType.SELL,
                tradeRequest.quantity(),
                quoteResponse.currentPrice(),
                totalPrice,
                LocalDateTime.now(ZoneId.systemDefault()));

        Trade savedTrade = tradeRepository.save(trade);

        return new TradeResponse(savedTrade.getTradeId(),
                savedTrade.getSymbol(),
                savedTrade.getTradeType(),
                savedTrade.getQuantity(),
                savedTrade.getPricePerUnit(),
                savedTrade.getTotalAmount(),
                portfolio.getCashBalance(),
                savedTrade.getExecutedAt()
        );
    }

    @Override
    public List<HoldingResponse> fetchHoldings(String userId) {
        UUID portfolioId = UUID.fromString(userId);

        List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);

        return portfolioMapper.toHoldingResponseList(holdings);
    }

    @Override
    public CashBalanceResponse fetchCashBalance(String userId) {
        Portfolio portfolio = portfolioRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new PortfolioNotFoundException(
                        String.format("Portfolio for user with id %s not found", userId)
                ));
        return portfolioMapper.toCashBalanceResponse(portfolio);
    }

    @Override
    public Page<ListTradeResponse> fetchAllTrades(String userId, Pageable pageable) {
        Page<Trade> trades = tradeRepository.findByUserId(UUID.fromString(userId), pageable);
        return portfolioMapper.toTradeResponseList(trades);
    }

    @Override
    public SummaryResponse fetchSummary(String userId) {
        Portfolio portfolio = portfolioRepository.findById(UUID.fromString(userId))
                .orElseThrow(()-> new PortfolioNotFoundException("Portfolio not found"));

        List<Holding> holdings = holdingRepository.findByPortfolioId(UUID.fromString(userId));

        BigDecimal holdingsValue = holdings
                .stream()
                .map(holding -> {
                    QuoteResponse quoteResponse = marketServiceClient.getQuote(holding.getSymbol());
                    return quoteResponse.currentPrice().multiply(holding.getQuantity());
                })
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        return new SummaryResponse(
                portfolio.getCashBalance(),
                holdingsValue,
                portfolio.getCashBalance().add(holdingsValue)
        );
    }

}
