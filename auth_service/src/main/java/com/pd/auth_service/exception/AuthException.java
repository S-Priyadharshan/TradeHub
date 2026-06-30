package com.pd.auth_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message,Throwable cause){super(message,cause);}

    public AuthException(Throwable cause){super(cause);}
}
