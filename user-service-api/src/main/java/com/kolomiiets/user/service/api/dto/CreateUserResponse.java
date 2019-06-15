package com.kolomiiets.user.service.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Reflection of CreateUserRequest as answer from service, but supplemented by id (that defined by DB)
 * @see CreateUserRequest
 */

@Data
@Builder
public class CreateUserResponse {
    private long id;
    private String email;
    private String nickname;
    private boolean isSaved;
}
