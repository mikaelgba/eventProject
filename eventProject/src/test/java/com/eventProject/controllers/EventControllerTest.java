package com.eventProject.controllers;

import com.eventProject.dto.EventDTO;
import com.eventProject.models.Event;
import com.eventProject.services.EventService;
import com.eventProject.vo.EventVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAll() {

        List<EventVO> eventVOList = new ArrayList<>();
        eventVOList.add(new EventVO(
                    new Event(
                            "1", "eventName", "2024-05-10",
                            null, 100, 50,
                            LocalDateTime.now(), LocalDateTime.now())
                )
        );

        eventVOList.add(new EventVO(
                new Event(
                        "2", "eventName_01", "2024-05-15",
                        null, 150, 100,
                        LocalDateTime.now(), LocalDateTime.now()
                )
        ));
        Page<EventVO> page = new PageImpl<>(eventVOList);

        when(eventService.findAll(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<EventVO>> responseEntity = eventController.findAll(0, 10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(page, responseEntity.getBody());
    }

    @Test
    public void findById() {

        String eventId = "1";
        EventVO eventVO = new EventVO(
                new Event(
                        "1", "eventName", "2024-05-10",
                        null, 100, 50,
                        LocalDateTime.now(), LocalDateTime.now())
        );

        when(eventService.findById(eventId)).thenReturn(eventVO);

        ResponseEntity<EventVO> responseEntity = eventController.findById(eventId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(eventVO, responseEntity.getBody());
    }

    @Test
    public void findByNameEvent() {

        String eventName = "Event";
        List<EventVO> eventVOList = new ArrayList<>();
        eventVOList.add(new EventVO(
                        new Event(
                                "1", "eventName", "2024-05-10",
                                null, 100, 50,
                                LocalDateTime.now(), LocalDateTime.now()
                        )
                )
        );

        eventVOList.add(new EventVO(
                new Event(
                        "2", "eventName_01", "2024-05-15",
                        null, 150, 100,
                        LocalDateTime.now(), LocalDateTime.now()
                )
        ));
        Page<EventVO> page = new PageImpl<>(eventVOList);

        when(eventService.findByName(eq(eventName), any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<EventVO>> responseEntity = eventController.findByNameEvent(0, 10, eventName);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(page, responseEntity.getBody());
    }

    @Test
    public void create() {

        EventDTO eventDTO = new EventDTO(
                "eventName_01", "2024-05-15",
                null, 150, 100,
                LocalDateTime.now(), LocalDateTime.now()
        );
        Event event = new Event(
                "1", "eventName", "2024-05-10",
                null, 100, 50,
                LocalDateTime.now(), LocalDateTime.now()
        );
        EventVO savedEventVO = new EventVO(event);

        when(eventService.save(eventDTO)).thenReturn(savedEventVO);
        ResponseEntity<EventVO> responseEntity = eventController.create(eventDTO);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedEventVO, responseEntity.getBody());
    }

    @Test
    public void update() {

        String eventId = "1";
        EventDTO eventDTO = new EventDTO(
                "eventName_01", "2024-05-15",
                null, 150, 100,
                LocalDateTime.now(), LocalDateTime.now()
        );
        Event event = new Event(
                "1", "eventName update", "2024-05-10",
                null, 100, 50,
                LocalDateTime.now(), LocalDateTime.now()
        );

        EventVO updatedEventVO = new EventVO(event);

        when(eventService.update(eventId, eventDTO)).thenReturn(updatedEventVO);
        ResponseEntity<EventVO> responseEntity = eventController.update(eventId, eventDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedEventVO, responseEntity.getBody());
    }

    @Test
    public void delete() {

        doNothing().when(eventService).delete(any(String.class));

        ResponseEntity<Void> responseEntity = eventController.delete("1");

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(eventService).delete("1");
    }

}