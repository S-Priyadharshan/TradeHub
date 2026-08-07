package com.pd.user_service.exception;

public class UserAlreadyExistsException extends UserServiceException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message,Throwable cause){super(message,cause);}

    public UserAlreadyExistsException(Throwable cause){super(cause);}
}
