package com.pd.market_service.domain.dto;

import java.math.BigDecimal;

public record FinnhubQuote(
        BigDecimal c,
        BigDecimal d,
        BigDecimal dp,
        BigDecimal h,
        BigDecimal l,
        BigDecimal o,
        BigDecimal pc,
        long t
) {}
