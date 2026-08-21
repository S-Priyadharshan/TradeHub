package com.pd.portfolio_service.client;

import com.pd.portfolio_service.config.FeignConfig;
import com.pd.portfolio_service.domain.dto.QuoteResponse;
import com.pd.portfolio_service.domain.dto.SymbolValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "market-service",configuration = FeignConfig.class)
public interface MarketServiceClient {


    @GetMapping(value = "/api/v1/market/quote/{symbol}")
    QuoteResponse getQuote(@PathVariable("symbol") String symbol);

    @GetMapping(value = "/api/v1/market/validate/{symbol}")
    SymbolValidationResponse validateSymbol(@PathVariable("symbol") String symbol);
}
