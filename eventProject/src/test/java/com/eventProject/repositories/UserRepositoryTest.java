package com.eventProject.repositories;

import com.eventProject.enuns.UserRole;
import com.eventProject.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User userTest;

    @BeforeEach
    void setUp() {
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

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        userTest = null;
    }


    @Test
    void test_save_user() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setLogin("newuser@example.com");
        newUser.setCpf("11122233344");
        newUser.setPassword("password123");
        newUser.setRole(UserRole.USER);
        newUser.setCreated(LocalDateTime.now());
        newUser.setUpdated(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getId());
        assertEquals("New User", savedUser.getName());
        assertEquals("newuser@example.com", savedUser.getLogin());
        assertEquals("11122233344", savedUser.getCpf());
        assertEquals(UserRole.USER, savedUser.getRole());
    }

    @Test
    void test_update_user() {
        Optional<User> optionalUser = userRepository.findById(userTest.getId());

        assertTrue(optionalUser.isPresent());

        User existingUser = optionalUser.get();
        existingUser.setName("Updated Name");
        existingUser.setPassword("newpassword");

        User updatedUser = userRepository.save(existingUser);

        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("newpassword", updatedUser.getPassword());
    }

    @Test
    void test_deleteUser() {

        Optional<User> optionalUser = userRepository.findById(userTest.getId());

        assertTrue(optionalUser.isPresent());

        User existingUser = optionalUser.get();
        userRepository.delete(existingUser);

        assertFalse(userRepository.findById(userTest.getId()).isPresent());
    }

    @Test
    void findByLogin_WhenExists() {
        UserDetails userTest = userRepository.findByLogin("example@example.com");

        assertThat(userTest).isNotNull();
        assertEquals("example@example.com", userTest.getUsername());
    }

    @Test
    void findByLogin_WhenNotExists() {
        UserDetails userTest = userRepository.findByLogin("nonexistent@example.com");

        assertThat(userTest).isNull();
    }

    @Test
    void findByCpf_WhenExists() {
        String cpf = "12345678900";
        Optional<User> userOptional = userRepository.findByCpf(cpf);

        assertThat(userOptional).isPresent();
        assertEquals(cpf, userOptional.get().getCpf());
    }

    @Test
    void findByCpf_WhenNotExists() {
        String cpf = "98765432100";
        Optional<User> userOptional = userRepository.findByCpf(cpf);

        assertThat(userOptional).isNotPresent();
        assertThat(userOptional).isEmpty();
    }

    @Test
    void findByUserCpf_WhenExists() {
        String cpf = "12345678900";
        User user = userRepository.findByUserCpf(cpf);

        assertThat(user).isNotNull();
        assertEquals(cpf, user.getCpf());
    }

    @Test
    void findByUserCpf_WhenNotExists() {
        String cpf = "99999999999";
        User user = userRepository.findByUserCpf(cpf);

        assertThat(user).isNull();
    }

    @Test
    void findByNameOrCpf_WhenExists() {
        String nameOrCpf = "Example";
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> usersPage = userRepository.findByNameOrCpf(nameOrCpf, pageable);

        assertThat(usersPage).isNotEmpty();
        assertThat(usersPage.getContent()).extracting("name").contains("example");
    }

    @Test
    void findByNameOrCpf_WhenNotExists() {
        String nameOrCpf = "Nonexistent";
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> usersPage = userRepository.findByNameOrCpf(nameOrCpf, pageable);

        assertThat(usersPage).isEmpty();
    }
}