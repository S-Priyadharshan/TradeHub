package com.pd.auth_service.service.impl;

import com.pd.auth_service.domain.event.UserDeletedEvent;
import com.pd.auth_service.service.AuthEventListener;
import com.pd.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class AuthEventListenerImpl implements AuthEventListener {

    private AuthService authService;

    @Override
    @KafkaListener(topics = "user-deleted",groupId = "auth-service-group")
    public void deleteUser(UserDeletedEvent event){
        authService.deleteUser(event);
    }
}
