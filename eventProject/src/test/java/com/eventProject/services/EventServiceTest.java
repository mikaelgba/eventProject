package com.eventProject.services;

import com.eventProject.dto.AddressDTO;
import com.eventProject.dto.EventDTO;
import com.eventProject.exceptions.exceptions.AlreadyExistsException;
import com.eventProject.exceptions.exceptions.NotFoundException;
import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.repositories.AddressRepository;
import com.eventProject.repositories.EventRepository;
import com.eventProject.services.impl.EventServiceImpl;
import com.eventProject.vo.EventVO;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private EventRepository eventRepository;

    @Autowired
    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void findAll_returns_page_of_eventVO() {

        List<Event> events = IntStream.rangeClosed(1, 2)
                .mapToObj(i -> new Event(
                        String.valueOf(i),
                        "Event " + i,
                        "2024-05-10",
                        null,
                        100,
                        50,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ))
                .collect(Collectors.toList());

        Page<Event> page = new PageImpl<>(events);

        when(eventRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<EventVO> resultPage = eventService.findAll(Pageable.ofSize(10).withPage(0));

        assertEquals(events.size(), resultPage.getTotalElements());

        resultPage.getContent().forEach(eventVO -> {
            Event event = events.stream()
                    .filter(e -> e.getId().equals(eventVO.getId()))
                    .findFirst()
                    .orElse(null);

            assert event != null;
            assertEquals(event.getName(), eventVO.getName());
            assertEquals(event.getDate(), eventVO.getDate());
        });
        verify(eventRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void findAll_returns_empty_page() {

        Page<Event> emptyPage = new PageImpl<>(Collections.emptyList());

        when(eventRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<EventVO> resultPage = eventService.findAll(Pageable.ofSize(10).withPage(0));

        assertTrue(resultPage.isEmpty());

        verify(eventRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void findById_event_found() {

        String eventId = "existingId";
        Event event = new Event(
                eventId,
                "Event 1",
                "2024-05-10",
                null,
                100,
                50,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        EventVO eventVO = eventService.findById(eventId);

        assertEquals(event.getId(), eventVO.getId());
        assertEquals(event.getName(), eventVO.getName());
    }

    @Test
    void findById_event_not_found() {

        String eventId = "not_id";
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> eventService.findById(eventId));
    }

    @Test
    void findByName_events_found() {

        String eventName = "Event Name";
        List<Event> eventList = new ArrayList<>();
        eventList.add(new Event("1", eventName, "2024-05-10", null, 100, 50, LocalDateTime.now(), LocalDateTime.now()));
        eventList.add(new Event("2", eventName, "2024-05-15", null, 150, 100, LocalDateTime.now(), LocalDateTime.now()));
        Page<Event> page = new PageImpl<>(eventList);

        when(eventRepository.findByNameEvent(eventName, Pageable.unpaged())).thenReturn(page);

        Page<EventVO> resultPage = eventService.findByName(eventName, Pageable.unpaged());

        assertEquals(eventList.size(), resultPage.getTotalElements());
        assertTrue(resultPage.getContent().stream().allMatch(eventVO -> eventVO.getName().equals(eventName)));
        verify(eventRepository, times(1)).findByNameEvent(eventName, Pageable.unpaged());
    }

    @Test
    void findByName_no_events_found() {

        String eventName = "Non-existent Event Name";
        Page<Event> emptyPage = new PageImpl<>(new ArrayList<>());

        when(eventRepository.findByNameEvent(eventName, Pageable.unpaged())).thenReturn(emptyPage);
        Page<EventVO> resultPage = eventService.findByName(eventName, Pageable.unpaged());

        assertTrue(resultPage.isEmpty());
        verify(eventRepository, times(1)).findByNameEvent(eventName, Pageable.unpaged());
    }

    @Test
    void save_successful() {

        AddressDTO addressDTO = new AddressDTO(
                "Street 01", "City 01",
                "Stage 01", "123456789",
                LocalDateTime.now(), LocalDateTime.now()
        );
        EventDTO eventDTO = new EventDTO(
                "Event Name", "2024-05-10", addressDTO,
                100, 0, null, null
        );
        Event savedEvent = new Event(
                "1", "Event Name", "2024-05-10", null,
                100, 0, LocalDateTime.now(), LocalDateTime.now()
        );
        when(eventRepository.findByName(eventDTO.getName())).thenReturn(null);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(addressRepository.save(any(Address.class))).thenReturn(new Address());

        EventVO savedEventVO = eventService.save(eventDTO);

        assertNotNull(savedEventVO);
        assertNotNull(savedEventVO.getId());
        assertEquals(eventDTO.getName(), savedEventVO.getName());

        verify(eventRepository, times(1)).findByName(eventDTO.getName());
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void save_when_existing_event_throw_AlreadyExistsException() {

        AddressDTO addressDTO = new AddressDTO(
                "Street 01", "City 01",
                "State 01", "123456789",
                LocalDateTime.now(), LocalDateTime.now()
        );
        EventDTO eventDTO = new EventDTO(
                "Existing Event", "2024-05-10", addressDTO,
                100, 0, null, null
        );
        Event existingEvent = new Event(
                "1", "Existing Event", "2024-05-10", null,
                100, 0, LocalDateTime.now(), LocalDateTime.now()
        );

        when(eventRepository.findByName(eventDTO.getName())).thenReturn(existingEvent);
        assertThrows(AlreadyExistsException.class, () -> eventService.save(eventDTO));

        verify(eventRepository, times(1)).findByName(eventDTO.getName());
        verify(eventRepository, never()).save(any(Event.class));
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    public void update_successful() {

        String id = "1";
        AddressDTO addressDTO = new AddressDTO(
                "Street 01", "City 01",
                "Stage 01", "123456789",
                LocalDateTime.now(), LocalDateTime.now()
        );
        EventDTO eventDTO = new EventDTO(
                "Event Name 01", "2024-05-10", addressDTO,
                100, 0, null, null
        );

        Event existingEvent = new Event();
        existingEvent.setId(id);
        existingEvent.setName("Event Name 02");
        existingEvent.setAddress(new Address());
        existingEvent.setCreated(LocalDateTime.now());

        when(eventRepository.findById(id)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        EventVO updatedEventVO = eventService.update(id, eventDTO);

        assertNotNull(updatedEventVO);
        assertEquals(eventDTO.getName(), updatedEventVO.getName());

        verify(eventRepository, times(1)).findByName(eventDTO.getName());
        verify(eventRepository, times(1)).findById(id);

    }

    @Test
    public void update_when_event_not_found() {
        // Arrange
        String eventId = "1";
        AddressDTO addressDTO = new AddressDTO(
                "Street 01", "City 01",
                "Stage 01", "123456789",
                LocalDateTime.now(), LocalDateTime.now()
        );
        EventDTO eventDTO = new EventDTO(
                "Existing Event", "2024-05-10", addressDTO,
                100, 0, null, null
        );

        when(eventRepository.findById(eventId)).thenReturn(java.util.Optional.empty());
        assertThrows(NotFoundException.class, () -> eventService.update(eventId, eventDTO));
    }

    @Test
    void delete_successful() {
        Event event = new Event();
        Address address = new Address();
        event.setAddress(address);

        when(eventRepository.findById(anyString())).thenReturn(Optional.of(event));

        eventService.delete("id");

        verify(eventRepository, times(1)).findById("id");
        verify(eventRepository, times(1)).deleteById(event.getId());
        verify(eventRepository, times(1)).countByAddressId(event.getAddress().getId());
        verify(addressRepository, times(1)).deleteById(event.getAddress().getId());
    }

    @Test
    void delete_when_event_not_found_throw_NotFoundException() {

        String eventId = "no_id";
        when(eventRepository.findById(eventId)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.delete(eventId));

        verify(eventRepository, times(1)).findById(eventId);
        verifyNoMoreInteractions(eventRepository, addressRepository);
    }

}