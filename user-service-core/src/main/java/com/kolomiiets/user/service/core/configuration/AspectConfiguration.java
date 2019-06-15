package com.kolomiiets.user.service.core.configuration;

import com.kolomiiets.user.service.core.aspect.UserServiceTelemetryAspect;
import com.kolomiiets.user.service.core.telemetry.UserServiceTelemeter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AspectConfiguration {

    @Bean
    public UserServiceTelemetryAspect userServiceTelemetryAspect(UserServiceTelemeter userServiceTelemeter) {
        return new UserServiceTelemetryAspect(userServiceTelemeter);
    }
}
