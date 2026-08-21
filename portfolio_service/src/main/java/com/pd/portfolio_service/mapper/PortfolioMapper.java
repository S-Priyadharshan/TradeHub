package com.pd.portfolio_service.mapper;

import com.pd.portfolio_service.domain.dto.*;
import com.pd.portfolio_service.domain.entity.Holding;
import com.pd.portfolio_service.domain.entity.Portfolio;
import com.pd.portfolio_service.domain.entity.Trade;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PortfolioMapper {

    HoldingResponse toResponse(Holding holding);
    List<HoldingResponse> toHoldingResponseList(List<Holding> holdings);
    CashBalanceResponse toCashBalanceResponse(Portfolio portfolio);
    TradeResponse toTradeResponse(Trade trade);
    Page<ListTradeResponse> toTradeResponseList(Page<Trade> trades);
    TradeEvent toTradeEvent(Trade trade);
}
