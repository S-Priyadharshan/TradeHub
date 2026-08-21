package com.pd.portfolio_service.exception;

public class HoldingNotFoundException extends PortfolioServiceException {
    public HoldingNotFoundException(String message) {
        super(message);
    }
}
