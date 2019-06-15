package com.kolomiiets.user.service.api;

import com.kolomiiets.user.service.api.dto.CreateUserRequest;
import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest createUserRequest);
    GetUserResponse getUser(long userId);
}
