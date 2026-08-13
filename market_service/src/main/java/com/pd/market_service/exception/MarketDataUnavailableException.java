package com.pd.market_service.exception;

public class MarketDataUnavailableException extends MarketServiceException {
    public MarketDataUnavailableException(String message) {
        super(message);
    }

    public MarketDataUnavailableException(String message,Throwable cause){super(message,cause);}

    public MarketDataUnavailableException(Throwable cause){super(cause);}
}
