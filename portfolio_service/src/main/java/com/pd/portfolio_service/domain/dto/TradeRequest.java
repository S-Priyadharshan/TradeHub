package com.pd.portfolio_service.domain.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TradeRequest(

        @NotBlank(message = "Stock symbol is required")
        @Pattern(regexp = "^[A-Z0-9.]{1,10}$", message = "Symbol must be 1-10 uppercase alphanumeric characters")
        String symbol,

        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0.00000001", message = "Quantity must be at least 0.00000001")
        @Digits(integer = 11, fraction = 8, message = "Quantity cannot exceed 11 integer digits and 8 decimal places")
        BigDecimal quantity
) {
}
