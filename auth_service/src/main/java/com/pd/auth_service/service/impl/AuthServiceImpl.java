package com.pd.auth_service.service.impl;

import com.pd.auth_service.client.KeycloakClient;
import com.pd.auth_service.domain.dto.*;
import com.pd.auth_service.domain.entity.AuthUser;
import com.pd.auth_service.domain.entity.RefreshToken;
import com.pd.auth_service.domain.enums.AccountStatus;
import com.pd.auth_service.domain.enums.AuthProvider;
import com.pd.auth_service.domain.enums.Role;
import com.pd.auth_service.domain.event.UserRegisteredEvent;
import com.pd.auth_service.exception.AccountSuspendedException;
import com.pd.auth_service.exception.InvalidTokenException;
import com.pd.auth_service.exception.InvalidCredentialsException;
import com.pd.auth_service.exception.UserAlreadyExistsException;
import com.pd.auth_service.mapper.AuthMapper;
import com.pd.auth_service.repository.AuthUserRepository;
import com.pd.auth_service.repository.RefreshTokenRepository;
import com.pd.auth_service.service.AuthService;
import com.pd.auth_service.service.JwtService;
import com.pd.auth_service.service.UserProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final KeycloakClient keycloakClient;
    private final JwtDecoder jwtDecoder;
    private final UserProvisioningService userProvisioningService;

    @Value("${oauth.client-secret}")
    private String clientSecret;

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    @Override
    public SignupResponse signupUser(SignupRequest signupRequest){

        if(authUserRepository.existsByEmail(signupRequest.email())){
            throw new UserAlreadyExistsException("Email already in use");
        }

        if(authUserRepository.existsByUsername(signupRequest.username())){
            throw new UserAlreadyExistsException("Username already taken");
        }

        AuthUser user = AuthUser.builder()
                .username(signupRequest.username())
                .passwordHash(passwordEncoder.encode(signupRequest.password()))
                .email(signupRequest.email())
                .authProvider(AuthProvider.LOCAL)
                .accountStatus(AccountStatus.ACTIVE)
                .role(Role.USER)
                .lastLoginAt(LocalDateTime.now(ZoneId.systemDefault()))
                .build();

        AuthUser savedUser = authUserRepository.save(user);

        UserRegisteredEvent event = new UserRegisteredEvent(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreatedAt(),
                savedUser.getAuthProvider());

        kafkaTemplate.send("user-registered",savedUser.getUserId().toString(),event);

        return authMapper.toDto(savedUser);
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        AuthUser user = authUserRepository.findByUsername(loginRequest.username())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        if(AccountStatus.SUSPENDED.equals(user.getAccountStatus())){
            throw new AccountSuspendedException("User account is suspended");
        }

        if(AccountStatus.DEACTIVATED.equals(user.getAccountStatus())){
            throw new AccountSuspendedException("User account is deactivated");
        }

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateAndPersistRefreshToken(user.getUserId());

        user.setLastLoginAt(LocalDateTime.now(ZoneId.systemDefault()));
        authUserRepository.save(user);

        return authMapper.toLoginResponse(token,900L,refreshToken);
    }

    @Override
    public LoginResponse refreshUser(RefreshRequest refreshRequest){
        String tokenHash = jwtService.hashToken(refreshRequest.refreshToken());
        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(()->new InvalidTokenException("Token does not exist"));

        if(Boolean.TRUE.equals(storedToken.getRevoked())){
            throw new InvalidTokenException("Refresh Token is revoked");
        }

        if(storedToken.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.systemDefault()))){
            throw new InvalidTokenException("Refresh Token is expired");
        }

        AuthUser user = authUserRepository.findById(storedToken.getUserId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        if(AccountStatus.SUSPENDED.equals(user.getAccountStatus())){
            throw new AccountSuspendedException("User Account is suspended");
        }

        storedToken.setRevoked(Boolean.TRUE);
        refreshTokenRepository.save(storedToken);

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateAndPersistRefreshToken(user.getUserId());

        return authMapper.toLoginResponse(newAccessToken,900L,newRefreshToken);
    }

    @Override
    public void logoutUser(LogoutRequest logoutRequest){
        String tokenHash = jwtService.hashToken(logoutRequest.refreshToken());
        refreshTokenRepository.findByTokenHash(tokenHash)
                .ifPresent(t->{
                    t.setRevoked(Boolean.TRUE);
                    refreshTokenRepository.save(t);
                });
    }

    @Override
    public LoginResponse keycloakLogin(String authCode){
        Map<String,String> body = new HashMap<>();
        body.put("grant_type","authorization_code");
        body.put("client_id","tradehub-client");
        body.put("client_secret",clientSecret);
        body.put("code",authCode);
        body.put("redirect_uri", "http://localhost:8080/api/v1/auth/keycloak/callback");

        KeycloakTokenResponse response = keycloakClient.getAccessToken(body);

        String keycloakToken = response.accessToken();

        Jwt jwt = jwtDecoder.decode(keycloakToken);

        AuthUser user = userProvisioningService.jitProvisioning(jwt);

        if(AccountStatus.SUSPENDED.equals(user.getAccountStatus())){
            throw new AccountSuspendedException("User account is suspended");
        }

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateAndPersistRefreshToken(user.getUserId());

        user.setLastLoginAt(LocalDateTime.now(ZoneId.systemDefault()));
        authUserRepository.save(user);

        return authMapper.toLoginResponse(token,900L,refreshToken);
    }

    @Override
    public void changePassword(ChangePasswordRequest request){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        AuthUser user = authUserRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        if(AuthProvider.KEYCLOAK.equals(user.getAuthProvider())){
            throw new UnsupportedOperationException("Password change is not supported for keycloak users");
        }

        if(!passwordEncoder.matches(request.currentPassword(),user.getPasswordHash())){
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        authUserRepository.save(user);

        refreshTokenRepository.revokeAllByUserId(user.getUserId());
    }

}
