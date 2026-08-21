package com.pd.portfolio_service.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignConfig {

    @Value("${HEADER_SECRET}")
    private String internalSecret;

    @Bean
    public RequestInterceptor gatewayHeaderInterceptor(){

        return requestTemplate->{
            requestTemplate.header("X-Internal-Gateway",internalSecret);

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null ||
                    !authentication.isAuthenticated()) {
                return;
            }

            String userId = authentication.getName();
            String role = authentication.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .map(authority -> authority.replace("ROLE_", ""))
                    .orElse(null);

            if (userId!=null) {
                requestTemplate.header("X-User-Id", userId);
            }
            if (role!=null) {
                requestTemplate.header("X-User-Role", role);
            }
        };
    }

}
