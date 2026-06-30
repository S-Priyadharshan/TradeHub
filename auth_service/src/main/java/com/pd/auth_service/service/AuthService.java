package com.pd.auth_service.service;

import com.pd.auth_service.domain.dto.*;

public interface AuthService {
    SignupResponse signupUser(SignupRequest signupRequest);
    LoginResponse loginUser(LoginRequest loginRequest);
    LoginResponse refreshUser(RefreshRequest refreshRequest);
    LoginResponse keycloakLogin(String authCode);
    void logoutUser(LogoutRequest logoutRequest);
}