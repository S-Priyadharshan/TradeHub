package com.pd.auth_service.mapper;

import com.pd.auth_service.domain.dto.LoginResponse;
import com.pd.auth_service.domain.dto.SignupResponse;
import com.pd.auth_service.domain.entity.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {
    SignupResponse toDto(AuthUser user);

    LoginResponse toLoginResponse(String token,Long expiresIn,String refreshToken);
}
