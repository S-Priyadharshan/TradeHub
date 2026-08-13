package com.pd.auth_service.service;

import com.pd.auth_service.domain.dto.*;
import com.pd.auth_service.domain.event.UserDeletedEvent;

public interface AuthService {
    SignupResponse signupUser(SignupRequest signupRequest);
    LoginResponse loginUser(LoginRequest loginRequest);
    LoginResponse refreshUser(RefreshRequest refreshRequest);
    LoginResponse keycloakLogin(String authCode);
    void logoutUser(LogoutRequest logoutRequest);
    void changePassword(ChangePasswordRequest request);
    void deleteUser(UserDeletedEvent event);
}