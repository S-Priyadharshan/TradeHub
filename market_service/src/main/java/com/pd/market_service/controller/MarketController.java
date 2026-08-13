package com.pd.market_service.controller;

import com.pd.market_service.domain.dto.QuoteResponse;
import com.pd.market_service.domain.dto.SymbolSearchResponse;
import com.pd.market_service.domain.dto.SymbolValidationResponse;
import com.pd.market_service.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/market")
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/quote/{symbol}")
    public ResponseEntity<QuoteResponse> getQuote(@PathVariable String symbol){
        QuoteResponse response = marketService.getQuote(symbol);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/quote/vantage/{symbol}")
    public ResponseEntity<QuoteResponse> getVatageQuote(@PathVariable String symbol){
        QuoteResponse response = marketService.fallbackToAlphaVantage(symbol);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/search/{symbol}")
    public ResponseEntity<SymbolSearchResponse> searchSymbol(@PathVariable String symbol){
        SymbolSearchResponse response = marketService.searchSymbol(symbol);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/validate/{symbol}")
    public ResponseEntity<SymbolValidationResponse> validateSymbol(@PathVariable String symbol){
        SymbolValidationResponse response = marketService.validateSymbol(symbol);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
