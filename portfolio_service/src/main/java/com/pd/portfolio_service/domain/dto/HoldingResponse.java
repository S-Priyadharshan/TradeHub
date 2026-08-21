package com.pd.portfolio_service.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record HoldingResponse(
        UUID id,
        UUID portfolioId,
        String symbol,
        BigDecimal quantity,
        BigDecimal averageCostBasis,
        LocalDateTime updatedAt
) {
}
