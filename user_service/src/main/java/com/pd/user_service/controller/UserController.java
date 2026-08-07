package com.pd.user_service.controller;

import com.pd.user_service.domain.dto.UpdateUserRequest;
import com.pd.user_service.domain.dto.UserProfileResponse;
import com.pd.user_service.domain.dto.UserSummaryResponse;
import com.pd.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUser(@PathVariable UUID userId){
        UserProfileResponse response = userService.getUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> updateUser(
            @PathVariable UUID userId,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserSummaryResponse>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
}
