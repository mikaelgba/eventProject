package com.eventProject.integration.controllers;

import com.eventProject.dto.UserDTO;
import com.eventProject.enuns.UserRole;
import com.eventProject.models.User;
import com.eventProject.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void findById() throws Exception {

        userRepository.deleteAll();
        User user = new User();
        user.setName("example");
        user.setLogin("example@example.com");
        user.setCpf("12345678900");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        User user1 = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", user1.getId())
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void findByNameOrCpf() throws Exception {

        userRepository.deleteAll();
        User user = new User();
        user.setName("example");
        user.setLogin("example@example.com");
        user.setCpf("12345678900");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        User user1 = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search/{nameOrCpf}", user1.getName())
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void create() throws Exception {

        userRepository.deleteAll();
        UserDTO userDTO = new UserDTO();
        userDTO.setName("example");
        userDTO.setLogin("example@example.com");
        userDTO.setCpf("12345678900");
        userDTO.setPassword("123");
        userDTO.setRole(UserRole.USER);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .with(user("admin").password("pass").roles("USER","ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void update() throws Exception {

        userRepository.deleteAll();

        User user = new User();
        user.setName("example");
        user.setLogin("example@example.com");
        user.setCpf("12345678900");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        User user1 = userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setName("example update");
        userDTO.setLogin("example@example.com");
        userDTO.setCpf("12345678900");
        userDTO.setPassword("123");
        userDTO.setRole(UserRole.USER);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", user1.getId())
                        .with(user("admin").password("pass").roles("USER","ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void update_bad_request() throws Exception {

        UserDTO userDTO = new UserDTO();

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", "not_id")
                        .with(user("admin").password("pass").roles("USER","ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void delete() throws Exception {

        userRepository.deleteAll();

        User user = new User();
        user.setName("example");
        user.setLogin("example@example.com");
        user.setCpf("12345678900");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        User user1 = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", user1.getId())
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
