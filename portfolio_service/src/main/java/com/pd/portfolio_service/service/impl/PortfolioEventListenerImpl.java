package com.pd.portfolio_service.service.impl;

import com.pd.portfolio_service.domain.event.UserRegisteredEvent;
import com.pd.portfolio_service.service.PortfolioEventListener;
import com.pd.portfolio_service.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioEventListenerImpl implements PortfolioEventListener {

    private final PortfolioService portfolioService;

    @Override
    @KafkaListener(topics = "user-registered",groupId = "portfolio-service-group")
    public void createPortfolio(UserRegisteredEvent event) {
        portfolioService.createPortfolio(event);
    }
}
