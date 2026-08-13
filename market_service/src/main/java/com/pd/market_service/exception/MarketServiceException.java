package com.pd.market_service.exception;

public class MarketServiceException extends RuntimeException {
    public MarketServiceException(String message) {
        super(message);
    }

    public MarketServiceException(String message,Throwable cause){super(message,cause);}

    public MarketServiceException(Throwable cause){super(cause);}
}
