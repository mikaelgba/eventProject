package com.eventProject.controllers;

import com.eventProject.dto.InscriptionDTO;
import com.eventProject.enuns.UserRole;
import com.eventProject.models.Event;
import com.eventProject.models.Inscription;
import com.eventProject.models.User;
import com.eventProject.services.InscriptionService;
import com.eventProject.vo.InscriptionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class InscriptionControllerTest {

    @Mock
    private InscriptionService inscriptionService;

    @InjectMocks
    private InscriptionController inscriptionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAll() {

        List<InscriptionVO> inscriptionVOList = new ArrayList<>();

        User user_01 = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );
        User user_02 = new User(
                "2", "Barry Allen",
                "12345678999", "user",
                "senha123", UserRole.USER,
                null, null
        );

        Event event = new Event(
                "1", "eventName", "2024-05-10",
                null, 100, 50,
                LocalDateTime.now(), LocalDateTime.now()
        );

        Inscription inscription_01 = new Inscription("1", user_01, event, LocalDateTime.now());
        Inscription inscription_02 = new Inscription("2", user_02, event, LocalDateTime.now());
        inscriptionVOList.add(new InscriptionVO(inscription_01));
        inscriptionVOList.add(new InscriptionVO(inscription_02));
        Page<InscriptionVO> page = new PageImpl<>(inscriptionVOList);

        when(inscriptionService.findAllInscription(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<InscriptionVO>> responseEntity = inscriptionController.findAll(0, 10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(page, responseEntity.getBody());
    }

    @Test
    public void findById() {

        String inscriptionId = "1";
        User user_01 = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );

        Event event = new Event(
                "1", "eventName", "2024-05-10",
                null, 100, 50,
                LocalDateTime.now(), LocalDateTime.now()
        );

        Inscription inscription_01 = new Inscription("1", user_01, event, LocalDateTime.now());
        InscriptionVO inscriptionVO = new InscriptionVO(inscription_01);

        when(inscriptionService.findById(inscriptionId)).thenReturn(inscriptionVO);

        ResponseEntity<InscriptionVO> responseEntity = inscriptionController.findById(inscriptionId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inscriptionVO, responseEntity.getBody());
    }

    @Test
    public void findByNameEvent() {

        String eventName = "eventName";
        List<InscriptionVO> inscriptionVOList = new ArrayList<>();

        User user_01 = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );
        User user_02 = new User(
                "2", "Barry Allen",
                "12345678999", "user",
                "senha123", UserRole.USER,
                null, null
        );

        Event event = new Event(
                "1", "eventName", "2024-05-10",
                null, 100, 50,
                LocalDateTime.now(), LocalDateTime.now()
        );

        Inscription inscription_01 = new Inscription("1", user_01, event, LocalDateTime.now());
        Inscription inscription_02 = new Inscription("2", user_02, event, LocalDateTime.now());
        inscriptionVOList.add(new InscriptionVO(inscription_01));
        inscriptionVOList.add(new InscriptionVO(inscription_02));
        Page<InscriptionVO> page = new PageImpl<>(inscriptionVOList);

        when(inscriptionService.findByNameEvent(eq(eventName), any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<InscriptionVO>> responseEntity = inscriptionController.findByNameEvent(0, 10, eventName);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(page, responseEntity.getBody());
    }

    @Test
    public void findByNameOrCpf() {

        String nameOrCpf = "User";
        List<InscriptionVO> inscriptionVOList = new ArrayList<>();

        User user_01 = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );
        User user_02 = new User(
                "2", "Barry Allen",
                "12345678999", "user",
                "senha123", UserRole.USER,
                null, null
        );

        Event event = new Event(
                "1", "eventName", "2024-05-10",
                null, 100, 50,
                LocalDateTime.now(), LocalDateTime.now()
        );

        Inscription inscription_01 = new Inscription("1", user_01, event, LocalDateTime.now());
        Inscription inscription_02 = new Inscription("2", user_02, event, LocalDateTime.now());

        inscriptionVOList.add(new InscriptionVO(inscription_01));
        inscriptionVOList.add(new InscriptionVO(inscription_02));
        Page<InscriptionVO> page = new PageImpl<>(inscriptionVOList);

        when(inscriptionService.findByCpfOrNameUser(eq(nameOrCpf), any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<InscriptionVO>> responseEntity = inscriptionController.findByNameOrCpf(0, 10, nameOrCpf);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(page, responseEntity.getBody());
    }

    @Test
    public void create() {

        InscriptionDTO inscriptionDTO = new InscriptionDTO("user_id", "event_id");

        User newUser = new User(
                "1", "John Constatine",
                "12345678914", "admin",
                "senha123", UserRole.ADMIN,
                null, null
        );

        Event event = new Event(
                "1", "eventName", "2024-05-10",
                null, 100, 50,
                LocalDateTime.now(), LocalDateTime.now()
        );

        Inscription inscription = new Inscription("1", newUser, event, LocalDateTime.now());

        InscriptionVO savedInscriptionVO = new InscriptionVO(inscription);

        when(inscriptionService.save(inscriptionDTO)).thenReturn(savedInscriptionVO);

        ResponseEntity<InscriptionVO> responseEntity = inscriptionController.create(inscriptionDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedInscriptionVO, responseEntity.getBody());
    }
}