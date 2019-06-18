package com.kolomiiets.user.service.client;

import com.kolomiiets.user.service.api.UserService;
import com.kolomiiets.user.service.api.dto.CreateUserRequest;
import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-service-client", url = "${feign.client.url}")
public interface UserServiceClient extends UserService {

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/users", consumes = "application/json", produces = "application/json")
    CreateUserResponse createUser(@RequestBody CreateUserRequest createUserRequest);

    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}", produces = "application/json")
    GetUserResponse getUser(@PathVariable("userId") long userId);
}
