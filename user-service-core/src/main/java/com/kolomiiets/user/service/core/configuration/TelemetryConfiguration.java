package com.kolomiiets.user.service.core.configuration;

import com.kolomiiets.user.service.core.telemetry.UserServiceTelemeter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelemetryConfiguration {

    @Bean
    public UserServiceTelemeter userServiceTelemeter(MeterRegistry meterRegistry) {
        return new UserServiceTelemeter(meterRegistry);
    }
}
