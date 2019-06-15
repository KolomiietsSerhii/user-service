package com.kolomiiets.user.service.core.telemetry;

import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserServiceTelemeter {

    private final MeterRegistry meterRegistry; //added just for example

    public UserServiceTelemeter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void logAndMeterValidCreateUserRequest(CreateUserResponse createUserRequest) {
        log.info("saved user, response={}", createUserRequest.toString());
        //custom telemeter logic
    }

    public void logAndMeterInvalidCreateUserRequest(CreateUserResponse createUserRequest) {
        log.error("user not saved, response={}", createUserRequest.toString());
        //custom telemeter logic
    }

    public void logAndMeterGetUserRequest(GetUserResponse getUserResponse) {
        log.info("get user response={}", getUserResponse.toString());
    }
}
