package com.kolomiiets.user.service.core.service;

import com.kolomiiets.user.service.api.UserService;
import com.kolomiiets.user.service.api.dto.CreateUserRequest;
import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;
import com.kolomiiets.user.service.core.entity.UserEntity;
import com.kolomiiets.user.service.core.repository.UserRepository;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        return requestIsValid(createUserRequest)
                ? processValidRequest(createUserRequest)
                : processInvalidRequest(createUserRequest);
    }

    @Override
    public GetUserResponse getUser(long userId) {
        return buildGetUserResponseFrom(userRepository.getOne(userId));
    }

    private CreateUserResponse processValidRequest(CreateUserRequest createUserRequest) {
        UserEntity saved = saveUser(createUserRequest);
        return buildCreateUserResponseFrom(saved, true);
    }

    private CreateUserResponse processInvalidRequest(CreateUserRequest createUserRequest) {
        UserEntity savedBefore = userRepository.getByEmail(createUserRequest.getEmail());
        return buildCreateUserResponseFrom(savedBefore, false);
    }

    private boolean requestIsValid(CreateUserRequest createUserRequest) {
        return !userRepository.existsByEmail(createUserRequest.getEmail());
        //validation logic may be extended on demand
    }

    private UserEntity saveUser(CreateUserRequest createUserRequest) {
        UserEntity userEntity = UserEntity.builder()
                .email(createUserRequest.getEmail())
                .nickname(createUserRequest.getNickname())
                .build();
        return userRepository.save(userEntity);
    }

    private CreateUserResponse buildCreateUserResponseFrom(UserEntity userEntity, boolean isSaved) {
        return CreateUserResponse.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .isSaved(isSaved)
                .build();
    }

    private GetUserResponse buildGetUserResponseFrom(UserEntity userEntity) {
        return GetUserResponse.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .build();
    }
}
