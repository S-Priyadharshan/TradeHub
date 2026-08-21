package com.pd.portfolio_service.repository;

import com.pd.portfolio_service.domain.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, UUID> {

    List<Holding> findByPortfolioId(UUID portfolioId);
    Optional<Holding> findByPortfolioIdAndSymbol(UUID portfolioId,String symbol);
}
