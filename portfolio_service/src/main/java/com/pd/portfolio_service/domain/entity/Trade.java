package com.pd.portfolio_service.domain.entity;

import com.pd.portfolio_service.domain.enums.TradeType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trades")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "trade_id",updatable = false,nullable = false)
    private UUID tradeId;

    @Column(name = "user_id", nullable = false,updatable = false)
    private UUID userId;

    @Column(name = "symbol", nullable = false,updatable = false)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false,updatable = false)
    private TradeType tradeType;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 8,updatable = false)
    private BigDecimal quantity;

    @Column(name = "price_per_unit", nullable = false, precision = 19, scale = 8,updatable = false)
    private BigDecimal pricePerUnit;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 8,updatable = false)
    private BigDecimal totalAmount;

    @Column(name = "executed_at", nullable = false, updatable = false)
    private LocalDateTime executedAt;

    public Trade(
            UUID userId,
            String symbol,
            TradeType tradeType,
            BigDecimal quantity,
            BigDecimal pricePerUnit,
            BigDecimal totalAmount,
            LocalDateTime executedAt
    ) {
        this.userId = userId;
        this.symbol = symbol;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.totalAmount = totalAmount;
        this.executedAt = executedAt;
    }
}
