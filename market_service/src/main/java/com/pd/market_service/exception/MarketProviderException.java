package com.pd.market_service.exception;

public class MarketProviderException extends MarketServiceException {
    public MarketProviderException(String message) {
        super(message);
    }

    public MarketProviderException(String message,Throwable cause){super(message,cause);}

    public MarketProviderException(Throwable cause){super(cause);}
}
