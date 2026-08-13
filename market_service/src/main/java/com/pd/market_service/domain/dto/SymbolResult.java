package com.pd.market_service.domain.dto;

public record SymbolResult(
        String description,
        String displaySymbol,
        String symbol,
        String type
) {
}
