package com.eventProject.controllers;

import com.eventProject.dto.UserDTO;
import com.eventProject.enuns.UserRole;
import com.eventProject.exceptions.exceptions.AlreadyExistsException;
import com.eventProject.exceptions.exceptions.InvalidDataException;
import com.eventProject.models.User;
import com.eventProject.services.UserService;
import com.eventProject.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAll_successfully() {

        List<UserVO> userVOList = new ArrayList<>();
        User user_01 = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );
        User user_02 = new User(
                "1", "Barry Allen",
                "12345678999", "user",
                "senha123", UserRole.USER,
                null, null
        );
        userVOList.add(new UserVO(user_01));
        userVOList.add(new UserVO(user_02));

        Page<UserVO> userVOPage = new PageImpl<>(userVOList);

        when(userService.findAll(any())).thenReturn(userVOPage);

        ResponseEntity<Page<UserVO>> responseEntity = userController.findAll(0, 10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userVOPage, responseEntity.getBody());
    }

    @Test
    public void findAll_when_empty_list() {

        Page<UserVO> emptyPage = new PageImpl<>(new ArrayList<>());

        when(userService.findAll(any())).thenReturn(emptyPage);

        ResponseEntity<Page<UserVO>> responseEntity = userController.findAll(0, 10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(emptyPage, responseEntity.getBody());
    }

    @Test
    public void findById_successfully() {

        List<UserVO> userVOList = new ArrayList<>();
        User user = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );
        UserVO userVO = new UserVO(user);
        userVOList.add(new UserVO(user));

        when(userService.findById("1")).thenReturn(userVO);

        ResponseEntity<UserVO> responseEntity = userController.findById("1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userVO, responseEntity.getBody());
    }


    @Test
    public void findById_when_not_found() {

        when(userService.findById(any(String.class))).thenReturn(null);

        ResponseEntity<UserVO> responseEntity = userController.findById("nonexistent_id");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void findByNameOrCpf_successfully() {

        List<UserVO> userVOList = new ArrayList<>();
        User user_01 = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );
        User user_02 = new User(
                "1", "Barry Allen",
                "12345678999", "user",
                "senha123", UserRole.USER,
                null, null
        );
        userVOList.add(new UserVO(user_01));
        userVOList.add(new UserVO(user_02));

        Page<UserVO> userVOPage = new PageImpl<>(userVOList);

        when(userService.findByNameOrCpf(any(String.class), any())).thenReturn(userVOPage);

        ResponseEntity<Page<UserVO>> responseEntity = userController.findByNameOrCpf(0, 10, "search_query");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userVOPage, responseEntity.getBody());
    }

    @Test
    public void create_successfully() {

        UserDTO userDTO = new UserDTO(
                "John Constatine", "12345678914", "admin",
                "senha123", UserRole.ADMIN, null, null
        );

        User user = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );

        UserVO savedUserVO = new UserVO(user);

        when(userService.save(any(UserDTO.class))).thenReturn(savedUserVO);

        ResponseEntity<UserVO> responseEntity = userController.create(userDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedUserVO, responseEntity.getBody());
    }

    @Test
    public void create_when_return_AlreadyExistsException_Login() {

        UserDTO userDTO = new UserDTO(
                "John Constatine", "12345678914", "admin",
                "senha123", UserRole.ADMIN, null, null
        );

        when(userService.save(any(UserDTO.class))).thenThrow(new AlreadyExistsException("Login already exists"));

        assertThrows(AlreadyExistsException.class, () -> userController.create(userDTO));
    }

    @Test
    public void create_when_return_AlreadyExistsException_CPF() {

        UserDTO userDTO = new UserDTO(
                "John Constatine", "12345678914", "admin",
                "senha123", UserRole.ADMIN, null, null
        );

        when(userService.save(any(UserDTO.class))).thenThrow(new AlreadyExistsException("CPF already exists with user"));
        assertThrows(AlreadyExistsException.class, () -> userController.create(userDTO));
    }

    @Test
    public void create_when_return_InvalidDataException_null_values() {

        UserDTO userDTO = new UserDTO(
                null, null, null,
                null, UserRole.ADMIN, null, null
        );

        when(userService.save(any(UserDTO.class))).thenThrow(new InvalidDataException("CPF, Login, Password, and Role cannot be null"));
        assertThrows(InvalidDataException.class, () -> userController.create(userDTO));
    }

    @Test
    public void update_successfully() {

        String userId = "1";
        UserDTO userDTO = new UserDTO("John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null);

        User user = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );

        UserVO updatedUserVO = new UserVO(user);

        when(userService.update(userId, userDTO)).thenReturn(updatedUserVO);

        ResponseEntity<UserVO> responseEntity = userController.update(userId, userDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedUserVO, responseEntity.getBody());
    }

    @Test
    public void update_when_return_AlreadyExistsException_Login() {

        String userId = "1";
        UserDTO userDTO = new UserDTO(
                "Barry Allen",
                "12345678999", "user",
                "senha123", UserRole.USER,
                null, null
        );

        when(userService.update(userId, userDTO)).thenThrow(new AlreadyExistsException("Login already exists"));
        assertThrows(AlreadyExistsException.class, () -> userController.update(userId, userDTO));
    }

    @Test
    public void update_when_return_AlreadyExistsException_CPF() {
        // Mocking data
        String userId = "1";
        UserDTO userDTO = new UserDTO(
                "Barry Allen",
                "12345678999", "user",
                "senha123", UserRole.USER,
                null, null
        );

        when(userService.update(userId, userDTO)).thenThrow(new AlreadyExistsException("CPF already exists with user"));
        assertThrows(AlreadyExistsException.class, () -> userController.update(userId, userDTO));
    }

    @Test
    public void update_when_return_InvalidDataException_null_values() {

        String userId = "1";
        UserDTO userDTO = new UserDTO(
                null,
                "12345678999", "user",
                "senha123", UserRole.USER,
                null, null
        );

        when(userService.update(userId, userDTO)).thenThrow(new InvalidDataException("CPF, Login, Password, and Role cannot be null"));
        assertThrows(InvalidDataException.class, () -> userController.update(userId, userDTO));
    }

    @Test
    public void delete_successfully() {

        doNothing().when(userService).delete(any(String.class));

        ResponseEntity<Void> responseEntity = userController.delete("1");

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(userService).delete("1");
    }

}