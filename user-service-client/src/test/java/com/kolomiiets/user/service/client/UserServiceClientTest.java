package com.kolomiiets.user.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.kolomiiets.user.service.api.dto.CreateUserRequest;
import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        initializers = {UserServiceClientTest.Initializer.class},
        classes = {UserServiceClientTest.TestConfig.class})
@ActiveProfiles("test")
public class UserServiceClientTest {

    private static WireMockServer wireMockServer;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserServiceClient userServiceClient;

    @BeforeClass
    public static void setUp() {
        wireMockServer = new WireMockServer();
        configureFor("localhost", 8080);
        wireMockServer.start();
    }

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void shouldCallCreateUserEndpoint() throws JsonProcessingException {
        String testUserEmail = "some@mail.com";
        String testUserNickname = "testUser";
        CreateUserRequest request = CreateUserRequest.builder()
                .email(testUserEmail)
                .nickname(testUserNickname)
                .build();
        CreateUserResponse response = CreateUserResponse.builder().build();
        String responseJson = objectMapper.writeValueAsString(response);
        stubFor(post("/user-service/users")).setResponse(aResponse().withBody(responseJson).build());

        userServiceClient.createUser(request);

        verify(postRequestedFor(urlEqualTo("/user-service/users")));
    }

    @Test
    public void shouldCallGetUserEndpoint() throws JsonProcessingException {
        GetUserResponse response = GetUserResponse.builder().build();
        String responseJson = objectMapper.writeValueAsString(response);
        long testUserId = 1L;
        stubFor(get("/user-service/users/" + testUserId)).setResponse(aResponse().withBody(responseJson).build());

        userServiceClient.getUser(testUserId);

        verify(getRequestedFor(urlEqualTo("/user-service/users/" + testUserId)));
    }

    @TestConfiguration
    @EnableAutoConfiguration
    @EnableFeignClients(basePackageClasses = UserServiceClient.class)
    public static class TestConfig {}

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "ribbon.eureka.enabled=false",
                    "feign.client.url=localhost:8080/user-service"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
