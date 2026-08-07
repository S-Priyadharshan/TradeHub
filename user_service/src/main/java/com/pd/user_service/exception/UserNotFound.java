package com.pd.user_service.exception;

public class UserNotFound extends UserServiceException {
    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound(String message,Throwable cause){super(message,cause);}

    public UserNotFound(Throwable cause){super(cause);}
}
