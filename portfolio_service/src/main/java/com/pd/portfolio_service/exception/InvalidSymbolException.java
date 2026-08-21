package com.pd.portfolio_service.exception;

public class InvalidSymbolException extends PortfolioServiceException {
    public InvalidSymbolException(String message) {
        super(message);
    }
}
