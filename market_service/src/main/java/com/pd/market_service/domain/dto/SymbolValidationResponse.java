package com.pd.market_service.domain.dto;

public record SymbolValidationResponse(
        String symbol,
        boolean valid
) {
}
