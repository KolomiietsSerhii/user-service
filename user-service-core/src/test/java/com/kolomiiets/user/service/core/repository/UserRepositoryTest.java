package com.kolomiiets.user.service.core.repository;

import com.kolomiiets.user.service.core.entity.UserEntity;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

//TODO: optimize shutdown sequence, resolve mess with test values
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = {UserRepositoryTest.Initializer.class})
@ActiveProfiles("test")
public class UserRepositoryTest {

    private final String testUserEmail = "some@mail.com";
    private final String testUserNickname = "testUser";
    @ClassRule
    public static MySQLContainer mySQLContainer = getMySQLContainer();
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void shouldSaveUserToDb() {
        UserEntity expected = UserEntity.builder().email("first." + testUserEmail).nickname(testUserNickname).build();

        UserEntity actual = userRepository.save(expected);

        assertEquals("expected and actual are not equal!", expected, actual);
    }

    @Test
    @Transactional
    public void shouldReadUserFromDb() {
        UserEntity expected = UserEntity.builder().email("second." + testUserEmail).nickname(testUserNickname).build();
        UserEntity saved = userRepository.save(expected);

        UserEntity actual = userRepository.getOne(saved.getId());

        assertEquals("expected and actual are not equal!", expected, actual);
    }

    private static MySQLContainer getMySQLContainer() {
        return new MySQLContainer()
                .withUsername("user")
                .withPassword("password")
                .withDatabaseName("chat_db");
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
