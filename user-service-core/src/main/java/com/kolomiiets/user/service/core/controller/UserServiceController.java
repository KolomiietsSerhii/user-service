package com.kolomiiets.user.service.core.controller;

import com.kolomiiets.user.service.api.UserService;
import com.kolomiiets.user.service.api.dto.CreateUserRequest;
import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;
import com.kolomiiets.user.service.core.service.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: optimize beans wiring configuration
@RestController
@RequestMapping("/users")
public class UserServiceController implements UserService {

    private final UserServiceImpl userServiceImpl;

    public UserServiceController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    @ApiOperation(value = "Create user record")
    @PostMapping(produces = "application/json")
    public CreateUserResponse createUser(
            @ApiParam(value = "JSON representation of user to be created", required = true)
            @RequestBody CreateUserRequest createUserRequest) {
        return userServiceImpl.createUser(createUserRequest);
    }

    @Override
    @ApiOperation(value = "Get user record by id")
    @GetMapping(value = "/{userId}", produces = "application/json")
    public GetUserResponse getUser(
            @ApiParam(value = "id of user to search", required = true, example = "123") @PathVariable long userId) {
        return userServiceImpl.getUser(userId);
    }
}
