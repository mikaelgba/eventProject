package com.eventProject.integration.repositories;

import com.eventProject.enuns.UserRole;
import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.models.User;
import com.eventProject.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class UserRepositoryIntegrationTest {


    @Autowired
    private UserRepository userRepository;

    User userTest = null;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setName("example");
        user.setLogin("example@example.com");
        user.setCpf("12345678900");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        userTest = userRepository.save(user);
    }

    @Test
    public void testFindByLogin() {

        User foundUser = userRepository.findByUserLogin("example@example.com");

        assertNotNull(foundUser);
        assertEquals("example@example.com", foundUser.getLogin());
    }

    @Test
    public void testFindByCpf() {

        User foundUser = userRepository.findByUserCpf("12345678900");

        assertNotNull(foundUser);
        assertEquals("12345678900", foundUser.getCpf());
    }

    @Test
    public void testFindByIdUser() {

        User foundUser = userRepository.findByIdUser(userTest.getId());

        assertNotNull(foundUser);
        assertEquals(userTest.getId(), foundUser.getId());
    }

    @Test
    public void testFindByNameOrCpf() {

        User user = new User();
        user.setName("John Doe");
        user.setCpf("12345678900");
        userRepository.save(user);

        Page<User> foundUsers = userRepository.findByNameOrCpf("John Doe", PageRequest.of(0, 10));

        assertTrue(foundUsers.getTotalElements() > 0);
        assertEquals("John Doe", foundUsers.getContent().get(0).getName());
    }

    @Test
    public void testFindByUserLogin() {
        User foundUser = userRepository.findByUserLogin("example@example.com");
        assertNotNull(foundUser);
        assertEquals("example@example.com", foundUser.getLogin());
    }
}
