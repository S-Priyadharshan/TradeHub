package com.pd.market_service.service.impl;

import com.pd.market_service.domain.dto.*;
import com.pd.market_service.domain.enums.QuoteSource;
import com.pd.market_service.exception.MarketDataUnavailableException;
import com.pd.market_service.exception.MarketProviderException;
import com.pd.market_service.exception.MarketServiceException;
import com.pd.market_service.service.MarketService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketServiceImpl implements MarketService {

    private final RestClient restClient;
    private final CacheManager cacheManager;

    @Value("${FINNHUB_SECRET}")
    private String finnhubApiKey;

    @Value("${ALPHA_VANTAGE_SECRET}")
    private String alphaVantageApiKey;

    private static final String FINNHUB_HEADER = "X-Finnhub-Token";
    private static final String FINNHUB_BASE = "https://finnhub.io/api/v1";

    private static final String ALPHA_VANTAGE_BASE = "https://www.alphavantage.co/query?";


    @Override
    @CircuitBreaker(name="finnhub",fallbackMethod = "fallbackToAlphaVantage")
    @Retry(name="finnhub")
    @RateLimiter(name="finnhub")
    public QuoteResponse getQuote(String symbol){

        Cache cache = cacheManager.getCache("stockQuotes");

        if (cache != null) {
            QuoteResponse cached = cache.get(symbol, QuoteResponse.class);

            if (cached != null) {
                return new QuoteResponse(
                        cached.symbol(),
                        cached.currentPrice(),
                        cached.change(),
                        cached.percentChange(),
                        cached.fetchedAt(),
                        QuoteSource.CACHE
                );
            }
        }

        try{
            FinnhubQuote finnhubQuote = restClient.get()
                    .uri(FINNHUB_BASE + "/quote?symbol=" + symbol)
                    .header(FINNHUB_HEADER, finnhubApiKey)
                    .retrieve()
                    .onStatus(status -> status.value() == 429, ((request, response) -> {
                        throw new MarketProviderException("Finnhub rate limit exceeded");
                    }))
                    .body(FinnhubQuote.class);

            QuoteResponse response = new QuoteResponse(
                    symbol,
                    finnhubQuote.c(),
                    finnhubQuote.d(),
                    finnhubQuote.dp(),
                    Instant.ofEpochSecond(finnhubQuote.t()),
                    QuoteSource.FINNHUB
            );

            if(cache!=null){
                cache.put(symbol,response);
            }

            return response;
        }catch(RestClientException e){
            throw new MarketServiceException(e.getMessage());
        }
    }

    @Override
    @RateLimiter(name="alphaVantage")
    public QuoteResponse fetchFromAlphaVantage(String symbol){
        try{
            AlphaVantageQuote quoteResponse = restClient.get()
                    .uri(ALPHA_VANTAGE_BASE +
                            "function=GLOBAL_QUOTE&symbol=" +
                            symbol +
                            "&apikey=" +
                            alphaVantageApiKey)
                    .retrieve()
                    .onStatus(status -> status.value() == 429, ((request, response) -> {
                        throw new MarketProviderException("Alpha Vantage rate limit exceeded");
                    }))
                    .body(AlphaVantageQuote.class);

            return new QuoteResponse(
                    quoteResponse.getSymbol(),
                    quoteResponse.getCurrentPrice(),
                    quoteResponse.getChange(),
                    quoteResponse.getPercentChange(),
                    Instant.now(),
                    QuoteSource.ALPHA_VANTAGE
            );
        }catch(RestClientException e){
            throw new MarketServiceException(e.getMessage());
        }
    }

    @Override
    public QuoteResponse fallbackToAlphaVantage(String symbol, Throwable t){
        log.warn("Finnhub failed for {}, falling back to Alpha Vantage. Reason: {}",
                symbol, t.getMessage());
        Cache cache = cacheManager.getCache("stockQuotes");
        try{
            return fetchFromAlphaVantage(symbol);
        }catch(Exception e){
            QuoteResponse stale = cache.get(symbol,QuoteResponse.class);
            if(stale!=null){
                return stale;
            }
            throw new MarketDataUnavailableException("All providers and cache exhausted for " + symbol);
        }
    }

    @Override
    public SymbolValidationResponse validateSymbol(String symbol){
        try{
            SymbolSearchResponse searchResponse = restClient.get()
                    .uri(FINNHUB_BASE + "/search?q=" + symbol)
                    .header(FINNHUB_HEADER, finnhubApiKey)
                    .retrieve()
                    .onStatus(status -> status.value() == 429, ((request, response) -> {
                        throw new MarketProviderException("Finnhub rate limit exceeded");
                    }))
                    .body(SymbolSearchResponse.class);

            boolean valid = searchResponse.result().stream()
                    .anyMatch(result -> result.symbol().equalsIgnoreCase(symbol));

            return new SymbolValidationResponse(symbol,valid);
        }catch(RestClientException e){
            throw new MarketServiceException(e.getMessage());
        }
    }

    @Override
    public SymbolSearchResponse searchSymbol(String symbol) {
        try{
            return restClient.get()
                    .uri(FINNHUB_BASE + "/search?q=" + symbol)
                    .header(FINNHUB_HEADER, finnhubApiKey)
                    .retrieve()
                    .onStatus(status -> status.value() == 429, ((request, response) -> {
                        throw new MarketProviderException("Finnhub rate limit exceeded");
                    }))
                    .body(SymbolSearchResponse.class);
        }catch(RestClientException e){
            throw new MarketServiceException(e.getMessage());
        }
    }
}
