package com.pd.portfolio_service.exception;

public class InsufficientFundsException extends PortfolioServiceException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
