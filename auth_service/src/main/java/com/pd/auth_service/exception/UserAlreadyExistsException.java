package com.pd.auth_service.exception;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message,Throwable cause){super(message,cause);}

    public UserAlreadyExistsException(Throwable cause){super(cause);}
}
