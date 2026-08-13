package com.pd.market_service.domain.dto;

import java.util.List;

public record SymbolSearchResponse(
        int count,
        List<SymbolResult> result
) {
}
