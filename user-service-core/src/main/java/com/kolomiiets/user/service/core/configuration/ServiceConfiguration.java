package com.kolomiiets.user.service.core.configuration;

import com.kolomiiets.user.service.core.repository.UserRepository;
import com.kolomiiets.user.service.core.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public UserServiceImpl userServiceImpl(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }
}
