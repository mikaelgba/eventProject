package com.eventProject.integration.controllers;

import com.eventProject.dto.AddressDTO;
import com.eventProject.dto.EventDTO;
import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.repositories.AddressRepository;
import com.eventProject.repositories.EventRepository;
import com.eventProject.repositories.InscriptionRepository;
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
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    Event event1 = null;

    @BeforeEach
    void setUp() {
        inscriptionRepository.deleteAll();
        eventRepository.deleteAll();
        addressRepository.deleteAll();

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
        event1 = eventRepository.save(event);
    }

    @Test
    void findAll() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/events")
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void findById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/events/{id}", event1.getId())
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void findByNameEvent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/events/search/{name}", event1.getName())
                        .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void create() throws Exception {

        AddressDTO addressDTO = new AddressDTO(
                "Street 01", "City 01",
                "Stage 01", "123456789",
                null, null
        );
        EventDTO eventDTO = new EventDTO(
                "New Event", "2024-05-10", addressDTO,
                100, 0, null, null
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .with(user("admin").password("pass").roles("USER","ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void update() throws Exception {

        AddressDTO addressDTO = new AddressDTO(
                "Street 01", "City 01",
                "Stage 01", "123456789",
                null, null
        );
        EventDTO eventDTO = new EventDTO(
                "Update Event", "2024-05-10", addressDTO,
                100, 0, null, null
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/events/{id}", event1.getId())
                        .with(user("admin").password("pass").roles("USER","ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void delete() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", event1.getId())
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
