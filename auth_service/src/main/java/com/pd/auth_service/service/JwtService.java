package com.pd.auth_service.service;

import com.pd.auth_service.domain.entity.AuthUser;
import com.pd.auth_service.domain.entity.RefreshToken;
import com.pd.auth_service.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secret;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateToken(AuthUser user){

        return Jwts.builder()
                .subject(user.getUserId().toString())
                .claim("username",user.getUsername())
                .claim("role",user.getRole().name())
                .claim("provider",user.getAuthProvider().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*15))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token).get("username",String.class);
    }

    public String extractUserId(String token){
        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token){

        return Jwts
                .parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAndPersistRefreshToken(UUID userId){
        String rawToken = UUID.randomUUID().toString();
        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .tokenHash(tokenHash)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    public String hashToken(String raw) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        }catch (NoSuchAlgorithmException ex){
            throw new IllegalStateException("SHA-256 algo not available",ex);
        }
    }

}
