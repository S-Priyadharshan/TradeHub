package com.pd.portfolio_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="portfolios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Portfolio {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "user_id", updatable = false,nullable = false, unique = true)
    private UUID userId;

    @Column(name = "cash_balance",nullable = false, precision = 19, scale = 8)
    private BigDecimal cashBalance;

    @Version
    private Long version;

    @EqualsAndHashCode.Include
    @CreatedDate
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;
}
