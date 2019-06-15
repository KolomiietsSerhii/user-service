package com.kolomiiets.user.service.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserResponse {
    private long id;
    private String email;
    private String nickname;
    //additional info, that may not be defined on registration phase, but may be added later (user groups, contacts, etc.)
}
