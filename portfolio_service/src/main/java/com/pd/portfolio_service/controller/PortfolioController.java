package com.pd.portfolio_service.controller;

import com.pd.portfolio_service.domain.dto.*;
import com.pd.portfolio_service.security.AuthenticatedUserProvider;
import com.pd.portfolio_service.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @PostMapping("/trade/buy")
    public ResponseEntity<TradeResponse> executeBuy(@RequestBody TradeRequest request){
        UUID userId = authenticatedUserProvider.getCurrentUser();
        TradeResponse response = portfolioService.executeBuy(request,userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/trade/sell")
    public ResponseEntity<TradeResponse> executeSell(@RequestBody TradeRequest request){
        UUID userId = authenticatedUserProvider.getCurrentUser();
        TradeResponse response =portfolioService.executeSell(request,userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/holdings")
    public ResponseEntity<List<HoldingResponse>> getHoldings(){
        UUID userId = authenticatedUserProvider.getCurrentUser();
        List<HoldingResponse> holdings = portfolioService.fetchHoldings(userId);
        return new ResponseEntity<>(holdings,HttpStatus.OK);
    }

    @GetMapping("/balance")
    public ResponseEntity<CashBalanceResponse> getCashBalance(){
        UUID userId = authenticatedUserProvider.getCurrentUser();
        CashBalanceResponse response = portfolioService.fetchCashBalance(userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/trades")
    public ResponseEntity<Page<ListTradeResponse>> getTrades(Pageable pageable){
        UUID userId = authenticatedUserProvider.getCurrentUser();
        Page<ListTradeResponse> trades = portfolioService.fetchAllTrades(userId,pageable);
        return new ResponseEntity<>(trades,HttpStatus.OK);
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary(){
        UUID userId = authenticatedUserProvider.getCurrentUser();
        SummaryResponse summaryResponse = portfolioService.fetchSummary(userId);
        return new ResponseEntity<>(summaryResponse,HttpStatus.OK);
    }
}
