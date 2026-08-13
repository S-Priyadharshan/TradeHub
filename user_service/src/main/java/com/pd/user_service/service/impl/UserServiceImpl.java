package com.pd.user_service.service.impl;

import com.pd.user_service.domain.dto.UpdateUserRequest;
import com.pd.user_service.domain.dto.UserProfileResponse;
import com.pd.user_service.domain.dto.UserSummaryResponse;
import com.pd.user_service.domain.entity.User;
import com.pd.user_service.domain.event.UserDeletedEvent;
import com.pd.user_service.domain.event.UserRegisteredEvent;
import com.pd.user_service.exception.UserNotFound;
import com.pd.user_service.exception.UserServiceException;
import com.pd.user_service.mapper.UserMapper;
import com.pd.user_service.repository.UserRepository;
import com.pd.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final KafkaTemplate<String, UserDeletedEvent> kafkaTemplate;

    @Override
    public void createUser(UserRegisteredEvent event) {

        if(userRepository.existsByUserId(event.userId())){
            return;
        }

        User user = User.builder()
                .userId(event.userId())
                .username(event.username())
                .email(event.email())
                .createdAt(event.registeredAt())
                .authProvider(event.authProvider())
                .build();

        userRepository.save(user);
    }

    public UserProfileResponse getUser(UUID userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()->new UserServiceException("User Not found"));
        return userMapper.toUserProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateUser(UUID userId, UpdateUserRequest request){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFound("User not found"));

        if(request.fullName()!= null){
            user.setFullName(request.fullName());
        }
        if(request.phoneNumber()!=null){
            user.setPhoneNumber(request.phoneNumber());
        }

        if(request.dateOfBirth()!=null){
            user.setDateOfBirth(request.dateOfBirth());
        }
        User savedUser = userRepository.save(user);
        return userMapper.toUserProfileResponse(savedUser);
    }

    public void deleteUser(UUID userId){

        User user = userRepository.findByUserId(userId)
                        .orElseThrow(()-> new UserNotFound("User not found"));

        user.setDeletedAt(LocalDateTime.now(ZoneId.systemDefault()));
        User savedUser = userRepository.save(user);

        UserDeletedEvent event = new UserDeletedEvent(
                savedUser.getUserId(),
                savedUser.getDeletedAt()
        );

        kafkaTemplate.send("user-deleted",savedUser.getUserId().toString(),event);
    }

    @Override
    public Page<UserSummaryResponse> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable)
                .map(userMapper::toUserSummaryResponse);
    }
}
