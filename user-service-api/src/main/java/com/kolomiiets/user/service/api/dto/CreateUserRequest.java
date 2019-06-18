package com.kolomiiets.user.service.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {
    private String email;
    private String nickname;
}


