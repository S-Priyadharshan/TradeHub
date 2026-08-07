package com.pd.user_service.service;

import com.pd.user_service.domain.dto.UpdateUserRequest;
import com.pd.user_service.domain.dto.UserProfileResponse;
import com.pd.user_service.domain.dto.UserSummaryResponse;
import com.pd.user_service.domain.event.UserRegisteredEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    void createUser(UserRegisteredEvent event);
    UserProfileResponse getUser(UUID userId);
    UserProfileResponse updateUser(UUID userId, UpdateUserRequest request);
    void deleteUser(UUID userId);
    Page<UserSummaryResponse> getAllUsers(Pageable pageable);
}
