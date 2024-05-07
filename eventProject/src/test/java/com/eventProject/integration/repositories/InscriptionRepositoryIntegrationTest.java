package com.eventProject.integration.repositories;

import com.eventProject.enuns.UserRole;
import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.models.Inscription;
import com.eventProject.models.User;
import com.eventProject.repositories.AddressRepository;
import com.eventProject.repositories.EventRepository;
import com.eventProject.repositories.InscriptionRepository;
import com.eventProject.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class InscriptionRepositoryIntegrationTest {

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {

        userRepository.deleteAll();
        inscriptionRepository.deleteAll();
        eventRepository.deleteAll();
        addressRepository.deleteAll();

        User user = new User();
        user.setName("example");
        user.setLogin("example@example.com");
        user.setCpf("12345678900");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        User userTest = userRepository.save(user);

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

        Event eventSave = eventRepository.save(event);

        Inscription inscription = new Inscription();
        inscription.setEvent(event);
        inscription.setUser(userTest);
        inscription.setDateInscription(LocalDateTime.now());

        inscriptionRepository.save(inscription);
    }

    @Test
    public void testFindByNameEvent() {

        Page<Inscription> foundInscriptions = inscriptionRepository.findByNameEvent("Test Event", PageRequest.of(0, 10));

        assertNotNull(foundInscriptions);
        assertEquals(1, foundInscriptions.getTotalElements());
    }

    @Test
    public void testFindByCpfOrNameUser() {

        Page<Inscription> foundInscriptions = inscriptionRepository.findByCpfOrNameUser("example", PageRequest.of(0, 10));

        assertNotNull(foundInscriptions);
        assertEquals(1, foundInscriptions.getTotalElements());
    }
}
