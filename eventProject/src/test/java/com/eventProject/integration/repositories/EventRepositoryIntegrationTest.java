package com.eventProject.integration.repositories;

import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.repositories.AddressRepository;
import com.eventProject.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class EventRepositoryIntegrationTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void findByName() {

        String eventName = "Test Event";
        Event event = new Event();
        event.setName(eventName);
        eventRepository.save(event);

        Event foundEvent = eventRepository.findByName(eventName);

        assertNotNull(foundEvent);
        assertEquals(eventName, foundEvent.getName());
    }

    @Test
    public void findByIdEvent() {

        Event event = new Event();
        event.setName("Test Event");
        eventRepository.save(event);

        Event foundEvent = eventRepository.findByIdEvent(event.getId());

        assertNotNull(foundEvent);
        assertEquals(event.getName(), foundEvent.getName());
    }

    @Test
    public void findByNameEvent() {

        String eventName = "Test Event";
        Event event = new Event();
        event.setName(eventName);
        eventRepository.save(event);

        Page<Event> foundEvents = eventRepository.findByNameEvent(eventName, null);

        assertEquals(1, foundEvents.getTotalElements());
        assertEquals(event.getName(), foundEvents.getContent().get(0).getName());
    }

    @Test
    public void countByAddressId() {

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setCep("12345678");
        address.setCreated(LocalDateTime.now());
        address.setUpdated(LocalDateTime.now());
        Address addressSave = addressRepository.save(address);

        Event event = new Event();
        event.setName("Test Event");
        event.setAddress(addressSave);

        eventRepository.save(event);

        Long count = eventRepository.countByAddressId(event.getAddress().getId());

        assertEquals(1, count);
    }
}
