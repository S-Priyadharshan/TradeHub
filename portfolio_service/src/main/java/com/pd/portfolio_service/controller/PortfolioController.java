package com.pd.portfolio_service.controller;

import com.pd.portfolio_service.domain.dto.TradeRequest;
import com.pd.portfolio_service.domain.dto.TradeResponse;
import com.pd.portfolio_service.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/trade/buy")
    public ResponseEntity<TradeResponse> executeBuy(@RequestBody TradeRequest request){
        TradeResponse response = portfolioService.executeBuy(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
