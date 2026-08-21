package com.pd.portfolio_service.domain.dto;

import com.pd.portfolio_service.domain.enums.TradeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TradeEvent(
        UUID tradeId,
        UUID userId,
        String symbol,
        TradeType tradeType,
        BigDecimal quantity,
        BigDecimal pricePerUnit,
        BigDecimal totalAmount,
        LocalDateTime executedAt
) {
}
