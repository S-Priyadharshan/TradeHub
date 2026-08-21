package com.pd.portfolio_service.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticatedUserProvider {

    public UUID getCurrentUser(){
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return UUID.fromString(userId);
    }
}
