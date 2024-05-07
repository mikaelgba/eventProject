package com.eventProject.integration.services;

import com.eventProject.dto.UserDTO;
import com.eventProject.enuns.UserRole;
import com.eventProject.exceptions.exceptions.NotFoundException;
import com.eventProject.models.User;
import com.eventProject.repositories.UserRepository;
import com.eventProject.services.impl.UserServiceImpl;
import com.eventProject.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void findAll() {

        List<User> usersList = List.of(
                new User(
                        "1",
                        "Virgil Ovid Aldecort Hawkins",
                        "12345678901",
                        "virgil@example.com",
                        "password",
                        UserRole.ADMIN,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                new User(
                        "2",
                        "Hal Jordan",
                        "98765432100",
                        "hal@example.com",
                        "password",
                        UserRole.USER,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        Page<User> usersPage = new PageImpl<>(usersList);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);


        Page<UserVO> result = userService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void findById() {

        User user = new User(
                "2",
                "Hal Jordan",
                "98765432100",
                "hal@example.com",
                "password",
                UserRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(userRepository.findById("2")).thenReturn(Optional.of(user));

        UserVO result = userService.findById("2");

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    public void findByNameOrCpf() {

        List<User> usersList = List.of(
                new User(
                        "1",
                        "Virgil Ovid Aldecort Hawkins",
                        "12345678901",
                        "virgil@example.com",
                        "password",
                        UserRole.ADMIN,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                new User(
                        "2",
                        "Hal Jordan",
                        "98765432100",
                        "hal@example.com",
                        "password",
                        UserRole.USER,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        Page<User> usersPage = new PageImpl<>(usersList);
        when(userRepository.findByNameOrCpf(anyString(), any(Pageable.class))).thenReturn(usersPage);

        Page<UserVO> result = userService.findByNameOrCpf("Doe", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void saveUser() {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setCpf("12345678901");
        userDTO.setLogin("johndoe");
        userDTO.setPassword("password");
        userDTO.setRole(UserRole.USER);

        User userToSave = new User();
        userToSave.setName(userDTO.getName());
        userToSave.setCpf(userDTO.getCpf());
        userToSave.setLogin(userDTO.getLogin());
        userToSave.setPassword(userDTO.getPassword());
        userToSave.setRole(userDTO.getRole());
        userToSave.setCreated(LocalDateTime.now());
        userToSave.setUpdated(LocalDateTime.now());
        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        UserVO savedUserVO = userService.save(userDTO);

        assertNotNull(savedUserVO);
        assertEquals(userDTO.getName(), savedUserVO.getName());
        assertEquals(userDTO.getCpf(), savedUserVO.getCpf());
        assertEquals(userDTO.getLogin(), savedUserVO.getLogin());
        assertEquals(userDTO.getRole(), savedUserVO.getRole());
    }

    @Test
    public void updateUser() {
        // Mocking userDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setCpf("12345678901");
        userDTO.setLogin("johndoe");
        userDTO.setPassword("newpassword");
        userDTO.setRole(UserRole.ADMIN);

        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setName("John Doe");
        existingUser.setCpf("12345678901");
        existingUser.setLogin("johndoe");
        existingUser.setPassword("password");
        existingUser.setRole(UserRole.USER);
        existingUser.setCreated(LocalDateTime.now());
        existingUser.setUpdated(LocalDateTime.now());
        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setName("John Doe");
        updatedUser.setCpf("12345678901");
        updatedUser.setLogin("johndoe");
        updatedUser.setPassword("newpassword");
        updatedUser.setRole(UserRole.ADMIN);
        updatedUser.setCreated(existingUser.getCreated());
        updatedUser.setUpdated(LocalDateTime.now());
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserVO updatedUserVO = userService.update("1", userDTO);

        assertNotNull(updatedUserVO);
        assertEquals(userDTO.getName(), updatedUserVO.getName());
        assertEquals(userDTO.getCpf(), updatedUserVO.getCpf());
        assertEquals(userDTO.getLogin(), updatedUserVO.getLogin());
        assertEquals(userDTO.getRole(), updatedUserVO.getRole());
    }

    @Test
    public void delete() {

        User user = new User(
                "1",
                "Virgil Ovid Aldecort Hawkins",
                "12345678901",
                "virgil@example.com",
                "password",
                UserRole.ADMIN,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.delete("1"));
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    public void delete_user_not_found() {

        when(userRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.delete("1"));
    }
}
