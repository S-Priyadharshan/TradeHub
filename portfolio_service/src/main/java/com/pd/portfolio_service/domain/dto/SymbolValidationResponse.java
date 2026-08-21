package com.pd.portfolio_service.domain.dto;

public record SymbolValidationResponse(
        String symbol,
        boolean valid
) {
}
