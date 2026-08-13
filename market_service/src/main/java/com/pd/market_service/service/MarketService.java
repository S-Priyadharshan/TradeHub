package com.pd.market_service.service;

import com.pd.market_service.domain.dto.QuoteResponse;
import com.pd.market_service.domain.dto.SymbolSearchResponse;
import com.pd.market_service.domain.dto.SymbolValidationResponse;

public interface MarketService {
    QuoteResponse getQuote(String symbol);
    SymbolValidationResponse validateSymbol(String symbol);
    QuoteResponse fallbackToAlphaVantage(String symbol);
    SymbolSearchResponse searchSymbol(String symbol);
}
