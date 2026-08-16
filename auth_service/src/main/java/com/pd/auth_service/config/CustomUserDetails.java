package com.pd.auth_service.config;

import com.pd.auth_service.domain.entity.AuthUser;
import com.pd.auth_service.domain.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final AuthUser authUser;

    public AuthUser getAuthUser(){
        return authUser;
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    @Override
    public @Nullable String getPassword() {
        return authUser.getPasswordHash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+authUser.getRole()));
    }

    @Override
    public boolean isAccountNonLocked() {
        return !AccountStatus.SUSPENDED.equals(authUser.getAccountStatus());
    }

    @Override
    public boolean isEnabled() {
        return !AccountStatus.DEACTIVATED.equals(authUser.getAccountStatus());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
