package com.eventProject.integration.services;

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
import com.eventProject.services.InscriptionService;
import com.eventProject.vo.InscriptionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class InscriptionServiceImplIntegrationTest {

    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        inscriptionRepository.deleteAll();
        userRepository.deleteAll();
        eventRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    public void findAllInscription_return_empty() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<InscriptionVO> inscriptionPage = inscriptionService.findAllInscription(pageable);

        assertTrue(inscriptionPage.isEmpty());

    }

    @Test
    public void findAllInscription_return_elements() {

        Inscription inscription1 = new Inscription();
        inscriptionRepository.save(inscription1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<InscriptionVO> inscriptionPage = inscriptionService.findAllInscription(pageable);

        assertEquals(inscriptionPage.getTotalElements(), 1);
    }

    @Test
    public void findById() {

        Event event = new Event();
        event.setName("Initial Event");
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setCep("12345678");
        Address savedAddress = addressRepository.save(address);
        event.setAddress(savedAddress);
        Event savedEvent = eventRepository.save(event);

        User user = new User();
        user.setName("example");
        user.setLogin("example@example.com");
        user.setCpf("12345678900");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        Inscription savedIns =  inscriptionRepository.save(new Inscription(null, savedUser, savedEvent, LocalDateTime.now()));

        InscriptionVO foundInscription = inscriptionService.findById(savedIns.getId());

        assertNotNull(foundInscription);
        assertEquals(savedIns.getId(), foundInscription.getId());
    }

    @Test
    public void findByNameEvent() {

        Event event = new Event();
        event.setName("Find by name event");
        Address address = new Address();
        Address savedAddress = addressRepository.save(address);
        event.setAddress(savedAddress);
        Event savedEvent = eventRepository.save(event);

        User user = new User();
        user.setName("example");
        User savedUser = userRepository.save(user);

        Inscription savedIns =  inscriptionRepository.save(new Inscription(null, savedUser, savedEvent, LocalDateTime.now()));
        Pageable pageable = PageRequest.of(0, 10);
        Page<InscriptionVO> inscriptionPage = inscriptionService.findByNameEvent(event.getName(), pageable);

        assertFalse(inscriptionPage.isEmpty());
        assertEquals(savedIns.getEvent().getName(), inscriptionPage.getContent().get(0).getEvent().getName());
    }

    @Test
    public void findByCpfOrNameUser() {

        Event event = new Event();
        event.setName("Find by name event");
        Address address = new Address();
        Address savedAddress = addressRepository.save(address);
        event.setAddress(savedAddress);
        Event savedEvent = eventRepository.save(event);

        User user = new User();
        user.setName("user find by name or cpf");
        user.setCpf("12345678900");
        User savedUser = userRepository.save(user);

        Inscription savedIns =  inscriptionRepository.save(new Inscription(null, savedUser, savedEvent, LocalDateTime.now()));
        String cpfOrNameUser = "12345678900";

        Pageable pageable = PageRequest.of(0, 10);
        Page<InscriptionVO> inscriptionPage = inscriptionService.findByCpfOrNameUser(cpfOrNameUser, pageable);

        assertFalse(inscriptionPage.isEmpty());
        assertEquals(savedIns.getId(), inscriptionPage.getContent().get(0).getId());
        assertEquals(savedIns.getUser().getName(), inscriptionPage.getContent().get(0).getUser().getName());
    }

    @Test
    public void save() {

        Event event = new Event();
        event.setName("Test Event");
        event.setCapMax(10);
        event.setCapActual(0);
        Event savedEvent = eventRepository.save(event);

        User user = new User();
        user.setName("User saved");
        User userSaved = userRepository.save(user);

        InscriptionDTO inscriptionDTO = new InscriptionDTO(userSaved.getId(), savedEvent.getId());

        InscriptionVO savedInscriptionVO = inscriptionService.save(inscriptionDTO);

        assertNotNull(savedInscriptionVO);
        assertNotNull(savedInscriptionVO.getId());
        assertEquals(savedEvent.getId(), savedInscriptionVO.getEvent().getId());
        assertEquals(userSaved.getId(), savedInscriptionVO.getUser().getId());

        Event updatedEvent = eventRepository.findById(savedEvent.getId()).orElse(null);
        assertNotNull(updatedEvent);
        assertEquals(1, updatedEvent.getCapActual());
    }

}
