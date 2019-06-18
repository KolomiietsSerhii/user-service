package com.kolomiiets.user.service.core.aspect;

import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;
import com.kolomiiets.user.service.core.telemetry.UserServiceTelemeter;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class UserServiceTelemetryAspect {

    private final UserServiceTelemeter userServiceTelemeter;

    public UserServiceTelemetryAspect(UserServiceTelemeter userServiceTelemeter) {
        this.userServiceTelemeter = userServiceTelemeter;
    }

    @AfterReturning(
            pointcut = "execution(* com.kolomiiets.user.service.core.service.UserServiceImpl.createUser(..))",
            returning = "createUserResponse"
    )
    public void logAndMeterCreateUserRequest(CreateUserResponse createUserResponse) {
        if (createUserResponse == null) return;
        if (createUserResponse.isSaved()) {
            userServiceTelemeter.logAndMeterValidCreateUserRequest(createUserResponse);
        } else {
            userServiceTelemeter.logAndMeterInvalidCreateUserRequest(createUserResponse);
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.kolomiiets.user.service.core.service.UserServiceImpl.getUser(..))",
            returning = "getUserResponse"
    )
    public void logAndMeterGetUserRequest(GetUserResponse getUserResponse) {
        if (getUserResponse == null) return;
        userServiceTelemeter.logAndMeterGetUserRequest(getUserResponse);
    }
}
