package com.pd.portfolio_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="holdings",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_holding_portfolio_symbol",
                    columnNames = {"portfolio_id","symbol"}
            )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "id",updatable = false,nullable = false,unique = true)
    private UUID id;

    @Column(name = "portfolio_id", updatable = false,nullable = false)
    private UUID portfolioId;

    @Column(name="symbol",nullable = false,updatable = false)
    private String symbol;

    @Column(name = "quantity",nullable = false, precision = 19, scale = 8)
    private BigDecimal quantity;

    @Column(name = "average_cost_basis",nullable = false, precision = 19, scale = 8)
    private BigDecimal averageCostBasis;

    @Version
    private Long version;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void increaseQuantity(BigDecimal quantity){
        if(quantity.compareTo(BigDecimal.ZERO)>=0){
            this.quantity = this.quantity.add(quantity);
        }
    }

    public void decreaseQuantity(BigDecimal quantity){
        if(quantity.compareTo(BigDecimal.ZERO)>=0 && quantity.compareTo(this.quantity)<=0){
            this.quantity = this.quantity.subtract(quantity);
        }
    }

    public void computeAverageCostBasis(BigDecimal newTotalPrice,BigDecimal newQuantity){
        if(newTotalPrice.compareTo(BigDecimal.ZERO)>=0 && newQuantity.compareTo(BigDecimal.ZERO)>=0){
            this.averageCostBasis = (this.averageCostBasis.multiply(this.quantity))
                    .add(newTotalPrice)
                    .divide(this.quantity.add(newQuantity),8, RoundingMode.HALF_UP);
        }
    }
}
