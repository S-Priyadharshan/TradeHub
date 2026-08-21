package com.pd.portfolio_service.service;


import com.pd.portfolio_service.domain.event.UserRegisteredEvent;

public interface PortfolioEventListener {
    void createPortfolio(UserRegisteredEvent event);
}
