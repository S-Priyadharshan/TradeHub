package com.pd.auth_service.exception;

public class AccountSuspendedException extends AuthException {
    public AccountSuspendedException(String message) {
        super(message);
    }

    public AccountSuspendedException(String message,Throwable cause){super(message,cause);}

    public AccountSuspendedException(Throwable cause){super(cause);}
}
