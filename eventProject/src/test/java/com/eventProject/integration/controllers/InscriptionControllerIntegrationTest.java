package com.eventProject.integration.controllers;

import com.eventProject.dto.InscriptionDTO;
import com.eventProject.enuns.UserRole;
import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.models.Inscription;
import com.eventProject.models.User;
import com.eventProject.repositories.AddressRepository;
import com.eventProject.repositories.EventRepository;
import com.eventProject.repositories.InscriptionRepository;
import com.eventProject.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class InscriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    Inscription inscription01 = null;

    @BeforeEach
    void setUp() {

        inscriptionRepository.deleteAll();
        eventRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("user01");
        user.setLogin("user01@example.com");
        user.setCpf("12345678900");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        User user1 = userRepository.save(user);

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setCep("12345678");
        address.setCreated(LocalDateTime.now());
        address.setUpdated(LocalDateTime.now());
        Address addressSave = addressRepository.save(address);

        Event event = new Event();
        event.setId("1");
        event.setName("Event name");
        event.setDate("2024-05-01");
        event.setAddress(addressSave);
        event.setCapMax(100);
        event.setCapActual(50);
        event.setCreated(LocalDateTime.now());
        event.setUpdated(LocalDateTime.now());
        Event eventSaved = eventRepository.save(event);

        inscription01 = inscriptionRepository.save(new Inscription("", user1, eventSaved, LocalDateTime.now()));
    }

    @Test
    void findAll() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions")
                .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void findById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/" + inscription01.getId())
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void findByNameEvent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/search-event/" + inscription01.getEvent().getName())
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void findByNameOrCpf() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/search-user/" + inscription01.getUser().getCpf())
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void create() throws Exception {

        InscriptionDTO inscriptionDTO = new InscriptionDTO(
                inscription01.getUser().getId(), inscription01.getEvent().getId()
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/inscriptions")
                        .with(user("admin").password("pass").roles("USER","ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inscriptionDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
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
