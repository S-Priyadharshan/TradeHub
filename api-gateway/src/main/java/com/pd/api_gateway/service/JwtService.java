package com.pd.api_gateway.service;

import com.pd.api_gateway.domain.dto.JwtPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secret;

    public JwtPrincipal validateAndExtractClaims(String token){
        Claims claims = Jwts
                .parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new JwtPrincipal(
                claims.getSubject(),
                claims.get("username",String.class),
                claims.get("roles",String.class),
                claims.get("provider",String.class)
        );
    }
}
