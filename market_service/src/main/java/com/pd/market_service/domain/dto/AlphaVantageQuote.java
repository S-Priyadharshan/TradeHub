package com.pd.market_service.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlphaVantageQuote(

        @JsonProperty("Global Quote")
        AlphaVantageGlobalQuote quote
) {
    public String getSymbol(){
        return quote.symbol();
    }
    public BigDecimal getCurrentPrice(){
        return quote.price();
    }
    public BigDecimal getChange(){
        return quote.change();
    }
    public BigDecimal getPercentChange(){
        return new BigDecimal(quote.changePercent().replace("%",""));
    }
}

record AlphaVantageGlobalQuote(

        @JsonProperty("01. symbol")
        String symbol,
        @JsonProperty("02. open")
        BigDecimal open,
        @JsonProperty("03. high")
        BigDecimal high,
        @JsonProperty("04. low")
        BigDecimal low,
        @JsonProperty("05. price")
        BigDecimal price,
        @JsonProperty("06. volume")
        BigDecimal volume,
        @JsonProperty("07. latest trading day")
        LocalDate latestTradingDay,
        @JsonProperty("08. previous close")
        BigDecimal previousClose,
        @JsonProperty("09. change")
        BigDecimal change,
        @JsonProperty("10. change percent")
        String changePercent
){

}