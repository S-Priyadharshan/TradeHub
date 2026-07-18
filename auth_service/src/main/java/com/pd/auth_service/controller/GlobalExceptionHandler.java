package com.pd.auth_service.controller;

import com.pd.auth_service.domain.dto.ErrorDto;
import com.pd.auth_service.exception.AccountSuspendedException;
import com.pd.auth_service.exception.AuthException;
import com.pd.auth_service.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        log.error("Caught Exception",ex);
        ErrorDto errorDto = new ErrorDto("An unknown error has occurred");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorDto> handleInvalidTokenException(InvalidTokenException ex){
        log.error("Caught InvalidTokenException",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccountSuspendedException.class)
    public ResponseEntity<ErrorDto> handleAccountSuspendedException(AccountSuspendedException ex){
        log.error("Caught AccountSuspendedException",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorDto> handleAuthException(AuthException ex){
        log.error("Caught AuthException",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDto> handleInvalidCredentialsException(InvalidCredentialsException ex){
        log.error("Caught InvalidCredentialsException",ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto,HttpStatus.BAD_REQUEST);
    }
}
