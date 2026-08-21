package com.pd.portfolio_service.exception;

public class InsufficientSharesException extends PortfolioServiceException {
    public InsufficientSharesException(String message) {
        super(message);
    }
}
