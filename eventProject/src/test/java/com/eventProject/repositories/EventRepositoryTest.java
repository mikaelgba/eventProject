package com.eventProject.repositories;

import com.eventProject.models.Address;
import com.eventProject.models.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Event eventTest;

    @BeforeEach
    void setUp() {
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
        event.setName("Example Event");
        event.setDate("2024-05-01");
        event.setAddress(addressSave);
        event.setCapMax(100);
        event.setCapActual(50);
        event.setCreated(LocalDateTime.now());
        event.setUpdated(LocalDateTime.now());
        eventTest = eventRepository.save(event);
    }

    @AfterEach
    void tearDown() {
        eventTest = null;
    }

    @Test
    void findByName_when_exists() {
        String eventName = "Example Event";
        Event event = eventRepository.findByName(eventName);
        assertThat(event).isNotNull();
        assertEquals(eventName, event.getName());
    }

    @Test
    void findByName_when_not_exists() {
        String eventName = "Nonexistent Event";
        Event event = eventRepository.findByName(eventName);
        assertThat(event).isNull();
    }

    @Test
    void findByIdEvent_when_exists() {

        Event event = new Event();
        event.setName("Evento Teste");
        eventRepository.save(event);

        String eventId = event.getId();
        Event foundEvent = eventRepository.findByIdEvent(eventId);

        assertNotNull(foundEvent);
        assertEquals(eventId, foundEvent.getId());
    }

    @Test
    void findByIdEvent_when_not_found() {
        Event foundEvent = eventRepository.findByIdEvent("id_inexistente");
        assertNull(foundEvent);
    }

    @Test
    void findByNameEvent_when_exists() {
        String eventNameParam = "Example";
        Pageable pageable = PageRequest.of(0, 10); // Exemplo de paginação
        Page<Event> eventsPage = eventRepository.findByNameEvent(eventNameParam, pageable);

        assertThat(eventsPage).isNotEmpty();
        assertThat(eventsPage.getContent()).extracting("name").contains("Example Event");
    }

    @Test
    void findByNameEvent_when_not_exists() {
        String eventNameParam = "Nonexistent";
        Pageable pageable = PageRequest.of(0, 10); // Exemplo de paginação
        Page<Event> eventsPage = eventRepository.findByNameEvent(eventNameParam, pageable);

        assertThat(eventsPage).isEmpty();
    }

    @Test
    void countByAddressId_when_exists() {
        String addressId = eventTest.getAddress().getId();

        Long count = eventRepository.countByAddressId(addressId);
        assertThat(count).isNotNull();
        assertEquals(count, 1);
    }

    @Test
    void countByAddressId_when_not_exists() {
        String addressId = "id_inexistente";

        Long count = eventRepository.countByAddressId(addressId);
        assertThat(count).isNotNull().isZero();
    }
}