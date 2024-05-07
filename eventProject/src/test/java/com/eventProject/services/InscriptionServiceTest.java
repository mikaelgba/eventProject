package com.eventProject.services;

import com.eventProject.dto.InscriptionDTO;
import com.eventProject.exceptions.exceptions.FullEventException;
import com.eventProject.exceptions.exceptions.NotFoundException;
import com.eventProject.models.Event;
import com.eventProject.models.Inscription;
import com.eventProject.models.User;
import com.eventProject.repositories.AddressRepository;
import com.eventProject.repositories.EventRepository;
import com.eventProject.repositories.InscriptionRepository;
import com.eventProject.repositories.UserRepository;
import com.eventProject.services.impl.InscriptionServiceImpl;
import com.eventProject.vo.InscriptionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscriptionServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InscriptionRepository inscriptionRepository;

    @Autowired
    @InjectMocks
    private InscriptionServiceImpl inscriptionService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void findAllInscription_successfully() {

        List<Inscription> inscriptions = new ArrayList<>();
        Inscription inscription1 = new Inscription();
        inscription1.setId("1");
        inscriptions.add(inscription1);

        Pageable pageable = Pageable.unpaged();
        Page<Inscription> inscriptionsPage = new PageImpl<>(inscriptions);
        when(inscriptionRepository.findAll(pageable)).thenReturn(inscriptionsPage);

        Page<InscriptionVO> inscriptionVOPage = inscriptionService.findAllInscription(pageable);

        verify(inscriptionRepository, times(1)).findAll(pageable);
        assertEquals(inscriptionsPage.getTotalElements(), inscriptionVOPage.getTotalElements());
    }

    @Test
    void findAllInscription_when_list_empty() {

        Page<Inscription> inscriptionsPage = new PageImpl<>(Collections.emptyList());
        when(inscriptionRepository.findAll(any(Pageable.class))).thenReturn(inscriptionsPage);

        Page<InscriptionVO> result = inscriptionService.findAllInscription(Pageable.unpaged());
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_successfully() {

        String idExistente = "1";
        Inscription inscriptionMock = new Inscription();
        inscriptionMock.setId(idExistente);
        when(inscriptionRepository.findById(idExistente)).thenReturn(Optional.of(inscriptionMock));

        InscriptionVO result = inscriptionService.findById(idExistente);

        assertNotNull(result);
        assertEquals(idExistente, result.getId());
    }

    @Test
    void findById_when_not_found() {

        String idNaoExistente = "2";
        when(inscriptionRepository.findById(idNaoExistente)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> inscriptionService.findById(idNaoExistente));
    }

    @Test
    void findByNameEvent_successfully() {

        String nameEvent = "Evento Teste";
        List<Inscription> inscriptions = new ArrayList<>();
        Inscription inscription1 = new Inscription();
        inscription1.setId("1");
        inscription1.setDateInscription(LocalDateTime.now());
        Event event = new Event();
        event.setName(nameEvent);
        inscription1.setEvent(event);
        User user = new User();
        user.setId("1");
        inscription1.setUser(user);
        inscriptions.add(inscription1);

        Pageable pageable = Pageable.unpaged();
        Page<Inscription> inscriptionsPage = new PageImpl<>(inscriptions);
        when(inscriptionRepository.findByNameEvent(nameEvent, pageable)).thenReturn(inscriptionsPage);

        Page<InscriptionVO> inscriptionVOPage = inscriptionService.findByNameEvent(nameEvent, pageable);

        assertFalse(inscriptionVOPage.isEmpty());
        assertEquals(inscriptionsPage.getTotalElements(), inscriptionVOPage.getTotalElements());
    }

    @Test
    void findByNameEvent_when_not_found() {

        String nameEvent = "Evento Inexistente";
        Page<Inscription> inscriptionsPage = new PageImpl<>(Collections.emptyList());

        when(inscriptionRepository.findByNameEvent(nameEvent, Pageable.unpaged())).thenReturn(inscriptionsPage);
        Page<InscriptionVO> inscriptionVOPage = inscriptionService.findByNameEvent(nameEvent, Pageable.unpaged());
        assertTrue(inscriptionVOPage.isEmpty());
    }

    @Test
    void FindByCpfOrNameUser_successfully() {

        String cpfOrNameUser = "12345678900";
        List<Inscription> inscriptions = new ArrayList<>();
        Inscription inscription1 = new Inscription();
        inscription1.setId("1");
        inscription1.setDateInscription(LocalDateTime.now());
        Event event = new Event();
        event.setName("Evento Teste");
        inscription1.setEvent(event);
        User user = new User();
        user.setId("1");
        user.setName("João");
        user.setCpf(cpfOrNameUser);
        inscription1.setUser(user);
        inscriptions.add(inscription1);

        Pageable pageable = Pageable.unpaged();
        Page<Inscription> inscriptionsPage = new PageImpl<>(inscriptions);
        when(inscriptionRepository.findByCpfOrNameUser(cpfOrNameUser, pageable)).thenReturn(inscriptionsPage);

        Page<InscriptionVO> inscriptionVOPage = inscriptionService.findByCpfOrNameUser(cpfOrNameUser, pageable);

        assertFalse(inscriptionVOPage.isEmpty());
        assertEquals(inscriptionsPage.getTotalElements(), inscriptionVOPage.getTotalElements());
    }

    @Test
    void findByCpfOrNameUser_when_not_found() {

        String cpfOrNameUser = "12345678900";
        Page<Inscription> inscriptionsPage = new PageImpl<>(Collections.emptyList());
        when(inscriptionRepository.findByCpfOrNameUser(cpfOrNameUser, Pageable.unpaged())).thenReturn(inscriptionsPage);

        Page<InscriptionVO> inscriptionVOPage = inscriptionService.findByCpfOrNameUser(cpfOrNameUser, Pageable.unpaged());

        assertTrue(inscriptionVOPage.isEmpty());
    }

    @Test
    void save_successfully() {

        String eventId = "1";
        String userId = "1";
        InscriptionDTO inscriptionDTO = new InscriptionDTO(eventId, userId);

        Event event = new Event();
        event.setId(eventId);
        event.setCapMax(10);
        event.setCapActual(9);

        User user = new User();
        user.setId(userId);

        when(eventRepository.findByIdEvent(eventId)).thenReturn(event);
        when(userRepository.findByIdUser(userId)).thenReturn(user);
        when(inscriptionRepository.save(any(Inscription.class))).thenAnswer(invocation -> {
            Inscription inscriptionArg = invocation.getArgument(0);
            inscriptionArg.setId("1");
            return inscriptionArg;
        });

        InscriptionVO inscriptionVO = inscriptionService.save(inscriptionDTO);

        assertNotNull(inscriptionVO);
        assertEquals(eventId, inscriptionVO.getEvent().getId());
        assertEquals(userId, inscriptionVO.getUser().getId());
        verify(eventRepository, times(1)).save(event);
        verify(inscriptionRepository, times(1)).save(any(Inscription.class));
    }

    @Test
    void save_when_event_full() {

        String eventId = "1";
        String userId = "1";
        InscriptionDTO inscriptionDTO = new InscriptionDTO(eventId, userId);

        Event event = new Event();
        event.setId(eventId);
        event.setCapMax(10);
        event.setCapActual(10);

        when(eventRepository.findByIdEvent(eventId)).thenReturn(event);
        assertThrows(FullEventException.class, () -> inscriptionService.save(inscriptionDTO));
    }

    @Test
    void save_when_id_event_not_found() {

        String eventId = "1";
        String userId = "1";
        InscriptionDTO inscriptionDTO = new InscriptionDTO(eventId, userId);

        when(eventRepository.findByIdEvent(eventId)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> inscriptionService.save(inscriptionDTO));
    }

    @Test
    void save_when_id_user_not_found() {

        String eventId = "1";
        String userId = "1";
        InscriptionDTO inscriptionDTO = new InscriptionDTO(eventId, userId);

        Event event = new Event();
        event.setCapMax(100);
        event.setCapActual(0);
        event.setId(eventId);

        when(eventRepository.findByIdEvent(eventId)).thenReturn(event);
        when(userRepository.findByIdUser(userId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> inscriptionService.save(inscriptionDTO));
    }

}