package com.kolomiiets.user.service.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolomiiets.user.service.api.UserService;
import com.kolomiiets.user.service.api.dto.CreateUserRequest;
import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;
import com.kolomiiets.user.service.core.UserServiceApplication;
import com.kolomiiets.user.service.core.aspect.UserServiceTelemetryAspect;
import com.kolomiiets.user.service.core.service.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {UserServiceApplication.class, UserServiceControllerTest.TestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
        })
public class UserServiceControllerTest {

    private final long testUserId = 1L;
    private final String testUserEmail = "some@mail.com";
    private final String testUserNickname = "testUser";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    public UserServiceTelemetryAspect userServiceTelemetryAspect;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void shouldCallCreateUserMethod() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(testUserEmail)
                .nickname(testUserNickname)
                .build();
        String requestJson = objectMapper.writeValueAsString(request);
        CreateUserResponse expected = CreateUserResponse.builder()
                .id(testUserId)
                .email(testUserEmail)
                .nickname(testUserNickname)
                .isSaved(true).build();
        when(userServiceImpl.createUser(request)).thenReturn(expected);

        String response = mockMvc.perform(post("/users")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CreateUserResponse actual = objectMapper.readValue(response, CreateUserResponse.class);
        assertEquals("expected and actual are not equal", expected, actual);
    }

    @Test
    public void shouldCallGetUserMethod() throws Exception {
        GetUserResponse expected = GetUserResponse.builder()
                .id(testUserId)
                .email(testUserEmail)
                .nickname(testUserNickname)
                .build();
        when(userServiceImpl.getUser(testUserId)).thenReturn(expected);

        String response = mockMvc.perform(get("/users/" + testUserId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GetUserResponse actual = objectMapper.readValue(response, GetUserResponse.class);
        assertEquals("expected and actual are not equal", expected, actual);
    }

    @TestConfiguration
    @EnableAutoConfiguration(exclude = {SpringDataWebAutoConfiguration.class})
    public static class TestConfig {

        @Bean
        public UserServiceImpl userServiceImpl() {
            return Mockito.mock(UserServiceImpl.class);
        }
    }
}
