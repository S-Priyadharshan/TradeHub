package com.pd.user_service.service.impl;

import com.pd.user_service.domain.event.UserRegisteredEvent;
import com.pd.user_service.service.UserEventListener;
import com.pd.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventListenerImpl implements UserEventListener {

    private final UserService userService;

    @Override
    @KafkaListener(topics = "user-registered",groupId = "user-service-group")
    public void createUser(UserRegisteredEvent event) {
        userService.createUser(event);
    }
}
