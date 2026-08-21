package com.pd.portfolio_service.domain.dto;

import com.pd.portfolio_service.domain.enums.TradeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TradeResponse(
        UUID tradeId,
        String symbol,
        TradeType tradeType,
        BigDecimal quantity,
        BigDecimal pricePerUnit,
        BigDecimal totalAmount,
        BigDecimal remainingCashBalance,
        LocalDateTime executedAt
) {
}
