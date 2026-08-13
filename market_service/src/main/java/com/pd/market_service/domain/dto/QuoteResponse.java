package com.pd.market_service.domain.dto;

import com.pd.market_service.domain.enums.QuoteSource;

import java.math.BigDecimal;
import java.time.Instant;

public record QuoteResponse(
        String symbol,
        BigDecimal currentPrice,
        BigDecimal change,
        BigDecimal percentChange,
        Instant fetchedAt,
        QuoteSource quoteSource
) {
}
