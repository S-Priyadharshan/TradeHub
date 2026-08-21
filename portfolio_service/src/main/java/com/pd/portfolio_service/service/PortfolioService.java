package com.pd.portfolio_service.service;

import com.pd.portfolio_service.domain.dto.*;
import com.pd.portfolio_service.domain.event.UserRegisteredEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PortfolioService {
    void createPortfolio(UserRegisteredEvent event);
    TradeResponse executeBuy(TradeRequest tradeRequest);
    TradeResponse executeSell(TradeRequest tradeRequest);
    List<HoldingResponse> fetchHoldings(String userId);
    CashBalanceResponse fetchCashBalance(String userId);
    Page<ListTradeResponse> fetchAllTrades(String userId, Pageable pageable);
    SummaryResponse fetchSummary(String userId);

}
