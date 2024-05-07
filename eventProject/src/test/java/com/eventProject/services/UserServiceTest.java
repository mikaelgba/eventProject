package com.eventProject.services;

import com.eventProject.dto.UserDTO;
import com.eventProject.enuns.UserRole;
import com.eventProject.exceptions.exceptions.AlreadyExistsException;
import com.eventProject.exceptions.exceptions.InvalidDataException;
import com.eventProject.exceptions.exceptions.NotFoundException;
import com.eventProject.models.User;
import com.eventProject.repositories.UserRepository;
import com.eventProject.services.impl.UserServiceImpl;
import com.eventProject.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returns_page_of_userVO() {

        Pageable pageable = Pageable.ofSize(10).withPage(0);

        User user1 = new User(
                "1",
                "Virgil Ovid Aldecort Hawkins",
                "12345678901",
                "virgil@example.com",
                "password",
                UserRole.ADMIN,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        User user2 = new User(
                "2",
                "Hal Jordan",
                "98765432100",
                "hal@example.com",
                "password",
                UserRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user1, user2), pageable, 2));

        Page<UserVO> userVOPage = userService.findAll(pageable);

        assertNotNull(userVOPage);
        assertEquals(2, userVOPage.getTotalElements());
        assertEquals(1, userVOPage.getTotalPages());
        assertEquals("1", userVOPage.getContent().get(0).getId());
        assertEquals("Virgil Ovid Aldecort Hawkins", userVOPage.getContent().get(0).getName());
        assertEquals("12345678901", userVOPage.getContent().get(0).getCpf());
        assertEquals("2", userVOPage.getContent().get(1).getId());
        assertEquals("Hal Jordan", userVOPage.getContent().get(1).getName());
        assertEquals("98765432100", userVOPage.getContent().get(1).getCpf());
    }

    @Test
    void findAll_empty_page_returns_empty_page() {

        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<User> emptyPage = Page.empty(pageable);
        when(userRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<UserVO> userVOPage = userService.findAll(pageable);

        assertNotNull(userVOPage);
        assertTrue(userVOPage.isEmpty());
        assertEquals(0, userVOPage.getTotalElements());
        assertEquals(0, userVOPage.getTotalPages());
    }

    @Test
    void findById_existing_user_id_returns_userVO() {

        String userId = "1";
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

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserVO userVO = userService.findById(userId);

        assertNotNull(userVO);
        assertEquals(userId, userVO.getId());
        assertEquals("Virgil Ovid Aldecort Hawkins", userVO.getName());
    }

    @Test
    void findById_not_existing_user_id_throws_NotFoundException() {

        String userId = "1";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    void findByNameOrCpf_returns_page_of_userVO() {

        String nameOrCpf = "John";
        Pageable pageable = Pageable.ofSize(10).withPage(0);
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
        Page<User> usersPage = new PageImpl<>(usersList, pageable, usersList.size());
        when(userRepository.findByNameOrCpf(nameOrCpf, pageable)).thenReturn(usersPage);

        Page<UserVO> userVOPage = userService.findByNameOrCpf(nameOrCpf, pageable);

        assertNotNull(userVOPage);
        assertEquals(2, userVOPage.getTotalElements());
        assertEquals(1, userVOPage.getTotalPages());
        assertEquals("1", userVOPage.getContent().get(0).getId());
        assertEquals("Virgil Ovid Aldecort Hawkins", userVOPage.getContent().get(0).getName());
        assertEquals("2", userVOPage.getContent().get(1).getId());
        assertEquals("Hal Jordan", userVOPage.getContent().get(1).getName());
    }

    @Test
    void findByNameOrCpf_returns_empty_page() {

        String nameOrCpf = "John";
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<User> emptyPage = Page.empty(pageable);
        when(userRepository.findByNameOrCpf(nameOrCpf, pageable)).thenReturn(emptyPage);

        Page<UserVO> userVOPage = userService.findByNameOrCpf(nameOrCpf, pageable);

        assertNotNull(userVOPage);
        assertTrue(userVOPage.isEmpty());
        assertEquals(0, userVOPage.getTotalElements());
        assertEquals(0, userVOPage.getTotalPages());
    }

    @Test
    void save_new_user_successfully() {

        UserDTO userDTO = new UserDTO(
                "Virgil Ovid Aldecort Hawkins",
                "12345678901",
                "virgil@example.com",
                "password",
                UserRole.USER,
                null,
                null
        );
        when(userRepository.findByUserLogin(userDTO.getLogin())).thenReturn(null);
        when(userRepository.save(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("1");
            return user;
        });

        UserVO savedUserVO = userService.save(userDTO);

        assertNotNull(savedUserVO);
        assertEquals("1", savedUserVO.getId());
    }

    @Test
    void save_user_with_null_fields_throws_InvalidDataException() {

        UserDTO userDTO = new UserDTO(
                null,
                "12345678901",
                "hal@example.com",
                "password",
                UserRole.USER,
                null,
                null
        );

        assertThrows(InvalidDataException.class, () -> userService.save(userDTO));
    }

    @Test
    void save_user_with_existing_login_throws_InvalidDataException() {

        UserDTO userDTO = new UserDTO(
                "Hal Jordan",
                "12345678901",
                "hal@login.com",
                "password",
                UserRole.USER,
                null,
                null
        );

        when(userRepository.findByUserLogin(userDTO.getLogin())).thenReturn(new User());

        assertThrows(AlreadyExistsException.class, () -> userService.save(userDTO));
    }

    @Test
    void save_user_with_existing_cpf_throws_AlreadyExistsException() {

        UserDTO userDTO = new UserDTO(
                "Hal Jordan",
                "existingcpf",
                "hal@example.com",
                "password",
                UserRole.USER,
                null,
                null
        );

        when(userRepository.findByUserCpf(userDTO.getCpf())).thenReturn(new User());
        assertThrows(AlreadyExistsException.class, () -> userService.save(userDTO));
    }

    @Test
    void update_successfully() {

        String userId = "existingId";
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Virgil Ovid Aldecort Hawkins Updated");
        userDTO.setLogin("newlogin");
        userDTO.setPassword("newPassword");
        userDTO.setRole(UserRole.USER);
        userDTO.setCpf("12345678901");
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setCreated(LocalDateTime.now().minusDays(1));
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserVO updatedUserVO = userService.update(userId, userDTO);

        assertNotNull(updatedUserVO);
        assertEquals(existingUser.getId(), updatedUserVO.getId());
        assertEquals(userDTO.getName(), updatedUserVO.getName());
        assertEquals(userDTO.getRole(), updatedUserVO.getRole());
        assertEquals(userDTO.getCpf(), updatedUserVO.getCpf());
    }

    @Test
    void update_login_already_exists() {

        String userId = "existingId";
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Hal Updated");
        userDTO.setLogin("newlogin");
        userDTO.setPassword("newPassword");
        userDTO.setRole(UserRole.USER);
        userDTO.setCpf("12345678900");

        when(userRepository.findByUserLogin(anyString())).thenReturn(new User());

        assertThrows(AlreadyExistsException.class, () -> userService.update(userId, userDTO));
        verify(userRepository, never()).findById(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_cpf_already_exists() {

        String userId = "existingId";
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Hal Updated");
        userDTO.setLogin("newlogin");
        userDTO.setPassword("newPassword");
        userDTO.setRole(UserRole.USER);
        userDTO.setCpf("existingCpf");

        when(userRepository.findByUserCpf(anyString())).thenReturn(new User());

        assertThrows(AlreadyExistsException.class, () -> userService.update(userId, userDTO));
        verify(userRepository, never()).findById(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_null_fields_in_userDTO() {

        String userId = "existingId";
        UserDTO userDTO = new UserDTO();

        assertThrows(InvalidDataException.class, () -> userService.update(userId, userDTO));
        verify(userRepository, never()).findById(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_user_not_found() {

        String userId = "notId";
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Hal Updated");
        userDTO.setLogin("newlogin");
        userDTO.setPassword("newPassword");
        userDTO.setRole(UserRole.USER);
        userDTO.setCpf("existingCpf");

        assertThrows(NotFoundException.class, () -> userService.update(userId, userDTO));
    }

    @Test
    void delete_sccessful() {

        String userId = "existingId";
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void delete_user_not_found() {

        String userId = "nonExistingId";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.delete(userId));
        verify(userRepository, never()).deleteById(anyString());
    }
}