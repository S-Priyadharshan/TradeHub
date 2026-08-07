package com.pd.user_service.mapper;

import com.pd.user_service.domain.dto.UserProfileResponse;
import com.pd.user_service.domain.dto.UserSummaryResponse;
import com.pd.user_service.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserProfileResponse toUserProfileResponse(User user);
    UserSummaryResponse toUserSummaryResponse(User user);
}
