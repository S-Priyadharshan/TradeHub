package com.pd.auth_service.exception;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message,Throwable cause){super(message,cause);}

    public InvalidCredentialsException(Throwable cause){super(cause);}
}
