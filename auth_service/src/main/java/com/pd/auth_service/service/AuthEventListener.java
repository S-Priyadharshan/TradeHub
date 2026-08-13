package com.pd.auth_service.service;

import com.pd.auth_service.domain.event.UserDeletedEvent;

public interface AuthEventListener {
    void deleteUser(UserDeletedEvent event);
}
