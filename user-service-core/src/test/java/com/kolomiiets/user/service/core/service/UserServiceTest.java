package com.kolomiiets.user.service.core.service;

import com.kolomiiets.user.service.api.UserService;
import com.kolomiiets.user.service.api.dto.CreateUserRequest;
import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;
import com.kolomiiets.user.service.core.entity.UserEntity;
import com.kolomiiets.user.service.core.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    private final long testUserId = 1L;
    private final String testUserEmail = "some@mail.com";
    private final String testUserNickname = "testUser";
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserService userService = new UserServiceImpl(userRepository);

    @Test
    public void shouldProcessValidCreateUserRequest() {
        CreateUserResponse expected = CreateUserResponse.builder()
                .id(testUserId)
                .email(testUserEmail)
                .nickname(testUserNickname)
                .isSaved(true)
                .build();
        CreateUserRequest validCreateUserRequest = CreateUserRequest.builder()
                .email(testUserEmail)
                .nickname(testUserNickname)
                .build();
        when(userRepository.existsByEmail(testUserEmail)).thenReturn(false);
        when(userRepository.save(any())).thenReturn(UserEntity.builder()
                .id(testUserId)
                .email(testUserEmail)
                .nickname(testUserNickname)
                .build());

        CreateUserResponse actual = userService.createUser(validCreateUserRequest);

        assertEquals("expected and actual are not equal", expected, actual);
    }

    @Test
    public void shouldReturnUser() {
        GetUserResponse expected = GetUserResponse.builder()
                .id(testUserId)
                .email(testUserEmail)
                .nickname(testUserNickname)
                .build();
        when(userRepository.getOne(testUserId)).thenReturn(UserEntity.builder()
                .id(testUserId)
                .email(testUserEmail)
                .nickname(testUserNickname)
                .build());

        GetUserResponse actual = userService.getUser(testUserId);

        assertEquals("expected and actual are not equal", expected, actual);
    }

    @Test
    public void shouldProcessInvalidCreateUserRequest() {
        long testUserId2 = 1L;
        String testUserEmail2 = "some2@mail.com";
        String testUserNickname2 = "testUser2";
        CreateUserResponse expected = CreateUserResponse.builder()
                .id(testUserId2)
                .email(testUserEmail2)
                .nickname(testUserNickname2)
                .isSaved(false)
                .build();
        CreateUserRequest invalidCreateUserRequest = CreateUserRequest.builder()
                .email(testUserEmail2)
                .nickname(testUserNickname)
                .build();
        when(userRepository.existsByEmail(testUserEmail2)).thenReturn(true);
        when(userRepository.getByEmail(testUserEmail2)).thenReturn(UserEntity.builder()
                .id(testUserId2)
                .email(testUserEmail2)
                .nickname(testUserNickname2)
                .build());

        CreateUserResponse actual = userService.createUser(invalidCreateUserRequest);

        assertEquals("expected and actual are not equal", expected, actual);
    }
}
