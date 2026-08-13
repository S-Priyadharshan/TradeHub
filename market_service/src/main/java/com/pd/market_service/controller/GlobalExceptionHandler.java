package com.pd.market_service.controller;

import com.pd.market_service.domain.dto.ErrorDto;
import com.pd.market_service.exception.MarketDataUnavailableException;
import com.pd.market_service.exception.MarketProviderException;
import com.pd.market_service.exception.MarketServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MarketProviderException.class)
    public ResponseEntity<ErrorDto> handleMarketProviderException(MarketProviderException ex){
        log.error("Caught exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MarketDataUnavailableException.class)
    public ResponseEntity<ErrorDto> handleMarketDataUnavailableException(MarketDataUnavailableException ex){
        log.error("Caught exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MarketServiceException.class)
    public ResponseEntity<ErrorDto> handleMarketServiceException(MarketServiceException ex){
        log.error("Caught exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        log.error("Caught exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }


}
