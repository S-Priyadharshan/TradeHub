package com.pd.portfolio_service.service;

import com.pd.portfolio_service.domain.dto.*;
import com.pd.portfolio_service.domain.event.UserRegisteredEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PortfolioService {
    void createPortfolio(UserRegisteredEvent event);
    TradeResponse executeBuy(TradeRequest tradeRequest,UUID userId);
    TradeResponse executeSell(TradeRequest tradeRequest,UUID userId);
    List<HoldingResponse> fetchHoldings(UUID userId);
    CashBalanceResponse fetchCashBalance(UUID userId);
    Page<ListTradeResponse> fetchAllTrades(UUID userId, Pageable pageable);
    SummaryResponse fetchSummary(UUID userId);

}
