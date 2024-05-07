package com.eventProject.repositories;

import com.eventProject.enuns.UserRole;
import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.models.Inscription;
import com.eventProject.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class InscriptionRepositoryTest {

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

        User user1 = new User();
        user1.setName("Clark Kent");
        user1.setLogin("clark@example.com");
        user1.setPassword("password");
        user1.setRole(UserRole.USER);
        user1.setCpf("12345678900");
        user1.setCreated(LocalDateTime.now());
        user1.setUpdated(LocalDateTime.now());
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Bruce Wayne");
        user2.setLogin("bruce@example.com");
        user2.setPassword("password");
        user2.setRole(UserRole.USER);
        user2.setCpf("98765432111");
        user2.setCreated(LocalDateTime.now());
        user2.setUpdated(LocalDateTime.now());
        userRepository.save(user2);

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setCep("12345678");
        address.setCreated(LocalDateTime.now());
        address.setUpdated(LocalDateTime.now());
        Address addressSave = addressRepository.save(address);

        Event event = new Event();
        event.setName("Justice League Event");
        event.setDate("2024-05-01");
        event.setAddress(addressSave);
        event.setCapMax(100);
        event.setCapActual(50);
        event.setCreated(LocalDateTime.now());
        event.setUpdated(LocalDateTime.now());
        eventRepository.save(event);

        Inscription inscription1 = new Inscription();
        inscription1.setUser(user1);
        inscription1.setEvent(event);
        inscription1.setDateInscription(LocalDateTime.now());
        inscriptionRepository.save(inscription1);

        Inscription inscription2 = new Inscription();
        inscription2.setUser(user2);
        inscription2.setEvent(event);
        inscription2.setDateInscription(LocalDateTime.now());
        inscriptionRepository.save(inscription2);
    }

    @Test
    void findByNameEvent_when_exists_returns_page_with_inscriptions() {

        String eventName = "Justice League Event";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Inscription> inscriptionsPage = inscriptionRepository.findByNameEvent(eventName, pageable);

        assertThat(inscriptionsPage).isNotEmpty();
        assertThat(inscriptionsPage.getContent()).extracting("event.name").contains(eventName);
    }

    @Test
    void findByCpfOrNameUser_when_exists_returns_page_with_inscriptions() {
        String cpfOrName = "Clark Kent";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Inscription> inscriptionsPage = inscriptionRepository.findByCpfOrNameUser(cpfOrName, pageable);

        assertThat(inscriptionsPage).isNotEmpty();
        assertThat(inscriptionsPage.getContent()).extracting("user.name").contains(cpfOrName);
    }

}