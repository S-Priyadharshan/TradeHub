package com.pd.portfolio_service.repository;

import com.pd.portfolio_service.domain.entity.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TradeRepository extends JpaRepository<Trade, UUID> {
    Page<Trade> findByUserId(UUID userId, Pageable pageable);
}
