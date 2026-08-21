package com.pd.portfolio_service.controller;

import com.pd.portfolio_service.domain.dto.ErrorDto;
import com.pd.portfolio_service.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSymbolException.class)
    public ResponseEntity<ErrorDto> handleInvalidSymbolException(InvalidSymbolException ex){
        log.error("Caught Exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HoldingNotFoundException.class)
    public ResponseEntity<ErrorDto> handleHoldingNotFoundException(HoldingNotFoundException ex){
        log.error("Caught Exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorDto> handleInsufficientFundsException(InsufficientFundsException ex){
        log.error("Caught Exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientSharesException.class)
    public ResponseEntity<ErrorDto> handleInsufficientSharesException(InsufficientSharesException ex){
        log.error("Caught Exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<ErrorDto> handlePortfolioNotFoundException(PortfolioNotFoundException ex){
        log.error("Caught Exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PortfolioServiceException.class)
    public ResponseEntity<ErrorDto> handlePortfolioServiceException(PortfolioServiceException ex){
        log.error("Caught Exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        log.error("Caught Exception",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
