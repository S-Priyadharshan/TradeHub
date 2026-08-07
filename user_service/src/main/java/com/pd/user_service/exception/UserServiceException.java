package com.pd.user_service.exception;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message,Throwable cause){super(message,cause);}

    public UserServiceException(Throwable cause){super(cause);}
}
