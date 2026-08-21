package com.pd.portfolio_service.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CashBalanceResponse(
        UUID userId,
        BigDecimal cashBalance
) {
}
