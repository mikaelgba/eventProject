package com.eventProject.integration.services;

import com.eventProject.dto.AddressDTO;
import com.eventProject.dto.EventDTO;
import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.repositories.AddressRepository;
import com.eventProject.repositories.EventRepository;
import com.eventProject.services.impl.EventServiceImpl;
import com.eventProject.vo.EventVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class EventServiceImplIntegrationTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EventServiceImpl eventService;

    @BeforeEach
    public void setUp() {
        eventRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    public void findAll() {

        Event event1 = new Event();
        Event event2 = new Event();

        eventRepository.save(event1);
        eventRepository.save(event2);

        Page<EventVO> result = eventService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void findById() {

        Event event =   eventRepository.save(new Event());
        EventVO result = eventService.findById(event.getId());

        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
    }

    @Test
    public void findByName() {

        Event event1 = new Event();
        Event event2 = new Event();
        event2.setName("Event 01");
        eventRepository.save(event1);
        eventRepository.save(event2);

        Page<EventVO> result = eventService.findByName(event2.getName(), PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertNotEquals(event1.getName(), result.getContent().get(0).getName());
    }

    @Test
    public void save() {

        AddressDTO addressDTO = new AddressDTO("123 Main St", "City", "State", "12345678", null, null);
        EventDTO eventDTO = new EventDTO("Test Event", "20/10", addressDTO, 1000, 0, null, null);

        EventVO result = eventService.save(eventDTO);

        assertNotNull(result);
        assertEquals("Test Event", result.getName());
    }

    @Test
    public void update() {

        Event event = new Event();
        event.setName("Initial Event");
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setCep("12345678");
        addressRepository.save(address);
        event.setAddress(address);
        Event savedEvent = eventRepository.save(event);
        String eventId = savedEvent.getId();

        AddressDTO updatedAddressDTO = new AddressDTO("123 Main St", "City", "State", "12345678", null, null);
        EventDTO updatedEventDTO = new EventDTO("Test Event", "20/10", updatedAddressDTO, 1000, 0, null, null);
        updatedEventDTO.setName("Updated Test Event");

        EventVO result = eventService.update(eventId, updatedEventDTO);

        assertNotNull(result);
        assertEquals("Updated Test Event", result.getName());
    }

    @Test
    public void delete() {

        Event event = new Event();
        event.setName("Test Event");
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setCep("12345678");
        addressRepository.save(address);
        event.setAddress(address);
        Event savedEvent = eventRepository.save(event);

        assertNotNull(savedEvent.getId());

        eventService.delete(savedEvent.getId());

        assertNull(addressRepository.findByAddressId(savedEvent.getAddress().getId()));
        assertFalse(eventRepository.existsById(savedEvent.getId()));
        assertFalse(addressRepository.existsById(address.getId()));
    }
}
