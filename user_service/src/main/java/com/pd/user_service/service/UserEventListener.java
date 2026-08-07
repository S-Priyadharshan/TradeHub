package com.pd.user_service.service;

import com.pd.user_service.domain.event.UserRegisteredEvent;

public interface UserEventListener {
    void createUser(UserRegisteredEvent event);
}
