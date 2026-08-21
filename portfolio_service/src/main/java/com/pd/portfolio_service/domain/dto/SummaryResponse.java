package com.pd.portfolio_service.domain.dto;

import java.math.BigDecimal;

public record SummaryResponse(
        BigDecimal cashBalance,
        BigDecimal holdingsValue,
        BigDecimal TotalAmount
) {
}
