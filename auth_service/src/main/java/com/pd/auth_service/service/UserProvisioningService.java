package com.pd.auth_service.service;

import com.pd.auth_service.domain.entity.AuthUser;
import com.pd.auth_service.domain.enums.AccountStatus;
import com.pd.auth_service.domain.enums.AuthProvider;
import com.pd.auth_service.domain.enums.Role;
import com.pd.auth_service.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserProvisioningService {

    private final AuthUserRepository authUserRepository;

    public AuthUser jitProvisioning(Jwt jwt){

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");

        return authUserRepository.findByKeycloakId(keycloakId)
                .orElseGet(()->
                    authUserRepository.findByEmail(email)
                            .map(existingUser -> linkKeycloakAccount(existingUser,keycloakId))
                            .orElseGet(()->createNewKeycloakUser(keycloakId,jwt))
                );
    }

    private AuthUser linkKeycloakAccount(AuthUser existingUser,UUID keycloakId){
        existingUser.setKeycloakId(keycloakId);
        existingUser.setAuthProvider(AuthProvider.BOTH);
        existingUser.setLastLoginAt(LocalDateTime.now(ZoneId.systemDefault()));
        return authUserRepository.save(existingUser);
    }

    private AuthUser createNewKeycloakUser(UUID keycloakId,Jwt jwt){
        AuthUser user = AuthUser.builder()
                .keycloakId(keycloakId)
                .username(jwt.getClaimAsString("preferred_username"))
                .email(jwt.getClaimAsString("email"))
                .authProvider(AuthProvider.KEYCLOAK)
                .accountStatus(AccountStatus.ACTIVE)
                .role(Role.USER)
                .lastLoginAt(LocalDateTime.now(ZoneId.systemDefault()))
                .build();
        return authUserRepository.save(user);

    }
}
