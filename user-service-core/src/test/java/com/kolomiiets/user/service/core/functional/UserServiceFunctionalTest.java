package com.kolomiiets.user.service.core.functional;

import com.kolomiiets.user.service.api.UserService;
import com.kolomiiets.user.service.api.dto.CreateUserRequest;
import com.kolomiiets.user.service.api.dto.CreateUserResponse;
import com.kolomiiets.user.service.api.dto.GetUserResponse;
import com.kolomiiets.user.service.client.UserServiceClient;
import com.kolomiiets.user.service.core.UserServiceApplication;
import com.kolomiiets.user.service.core.entity.UserEntity;
import com.kolomiiets.user.service.core.repository.UserRepository;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserServiceApplication.class, UserServiceFunctionalTest.TestConfig.class},
                webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = {UserServiceFunctionalTest.Initializer.class})
@ActiveProfiles("test")
public class UserServiceFunctionalTest {

    private final String testUserEmail = "some@mail.com";
    private final String testUserNickname = "testUser";
    @ClassRule
    public static MySQLContainer mySQLContainer = getMySQLContainer();
    @Autowired
    private UserService userServiceClient;
    @Autowired
    private UserRepository userRepository;

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void shouldSaveUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(testUserEmail)
                .nickname(testUserNickname)
                .build();

        userServiceClient.createUser(request);

        assertTrue(userRepository.existsByEmail(testUserEmail));
    }

    @Test
    public void shouldReadUser() {
        UserEntity saved = userRepository.save(UserEntity.builder().email(testUserEmail).nickname(testUserNickname).build());
        GetUserResponse expected = GetUserResponse.builder()
                .id(saved.getId())
                .email(testUserEmail)
                .nickname(testUserNickname)
                .build();

        GetUserResponse actual = userServiceClient.getUser(saved.getId());

        assertEquals("expected and actual are not equal!", expected, actual);
    }

    @Test
    public void shouldProcessInvalidCreateUserRequest() {
        UserEntity saved = userRepository.save(UserEntity.builder().email(testUserEmail).nickname(testUserNickname).build());
        String testUserNickname2 = "testUser2";
        CreateUserRequest invalidRequest = CreateUserRequest.builder()
                .email(testUserEmail)
                .nickname(testUserNickname2)
                .build();
        CreateUserResponse expected = CreateUserResponse.builder()
                .id(saved.getId())
                .email(testUserEmail)
                .nickname(testUserNickname)
                .isSaved(false).build();

        CreateUserResponse actual = userServiceClient.createUser(invalidRequest);

        assertEquals("expected and actual are not equal!", expected, actual);
    }


    private static MySQLContainer getMySQLContainer() {
        return new MySQLContainer()
                .withUsername("user")
                .withPassword("password")
                .withDatabaseName("chat_db");
    }

    @TestConfiguration
    @EnableAutoConfiguration
    @EnableFeignClients(basePackageClasses = UserServiceClient.class)
    public static class TestConfig {
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + mySQLContainer.getUsername(),
                    "spring.datasource.password=" + mySQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
