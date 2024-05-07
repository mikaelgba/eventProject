package com.eventProject.services.impl;

import com.eventProject.dto.AddressDTO;
import com.eventProject.exceptions.exceptions.InvalidDataException;
import com.eventProject.vo.EventVO;
import com.eventProject.dto.EventDTO;
import com.eventProject.exceptions.exceptions.AlreadyExistsException;
import com.eventProject.exceptions.exceptions.NotFoundException;
import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.repositories.AddressRepository;
import com.eventProject.repositories.EventRepository;
import com.eventProject.services.EventService;
import com.eventProject.services.impl.converts.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Page<EventVO> findAll(Pageable pageable) {
        Page<Event> usersPage = eventRepository.findAll(pageable);
        return usersPage.map(EventVO::new);
    }

    @Override
    public EventVO findById(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        return new EventVO(event);
    }

    @Override
    public Page<EventVO> findByName(String name, Pageable pageable) {
        Page<Event> events = eventRepository.findByNameEvent(name, pageable);
        return events.map(EventVO::new);
    }

    @Override
    public EventVO save(EventDTO eventDTO) {

        if (eventRepository.findByName(eventDTO.getName()) != null) {
            throw new AlreadyExistsException("Event already exists");
        }

        validateAddressData(eventDTO.getAddress());

        Converters converters = new Converters();

        Event event = converters.convertToEvent(eventDTO);

        if (eventDTO.getAddress() != null) {
            updateEventAddress(event, eventDTO.getAddress());
        }

        event.setCapActual(0);
        event.setCreated(LocalDateTime.now());
        event.setUpdated(LocalDateTime.now());

        return new EventVO(eventRepository.save(event));
    }

    @Override
    public EventVO update(String id, EventDTO eventDTO) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        validateEventName(existingEvent, eventDTO.getName());

        Converters converters = new Converters();
        existingEvent = converters.convertToEvent(eventDTO);

        if (eventDTO.getAddress() != null) {
            updateEventAddress(existingEvent, eventDTO.getAddress());
        }

        existingEvent.setUpdated(LocalDateTime.now());
        Event updatedEvent = eventRepository.save(existingEvent);
        return new EventVO(updatedEvent);
    }

    private void validateEventName(Event existingEvent, String newName) {
        if (!existingEvent.getName().equals(newName)) {
            Event existingEventWithName = eventRepository.findByName(newName);
            if (existingEventWithName != null && !existingEventWithName.getId().equals(existingEvent.getId())) {
                throw new AlreadyExistsException("Event with the same name already exists");
            }
        }
    }

    private void updateEventAddress(Event existingEvent, AddressDTO addressDTO) {
        validateAddressData(addressDTO);
        Address existingAddress = addressRepository.findByStreetCityAndState(
                addressDTO.getStreet(),
                addressDTO.getCity(),
                addressDTO.getState());

        Converters converters = new Converters();
        if (existingAddress == null) {
            Address newAddress = converters.convertToAddress(addressDTO);
            newAddress.setCreated(LocalDateTime.now());
            newAddress.setUpdated(LocalDateTime.now());
            Address savedAddress = addressRepository.save(newAddress);
            existingEvent.setAddress(savedAddress);
        } else {
            existingEvent.setAddress(existingAddress);
        }
    }

    @Override
    public void delete(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        eventRepository.deleteById(event.getId());
        Long countEventsWithAddress = eventRepository.countByAddressId(event.getAddress().getId());
        if (countEventsWithAddress == 0) addressRepository.deleteById(event.getAddress().getId());
    }

    private void validateAddressData(AddressDTO addressDTO) {
        if (addressDTO.getStreet() == null || addressDTO.getStreet().isEmpty()) {
            throw new InvalidDataException("Street is required");
        }
        if (addressDTO.getCity() == null || addressDTO.getCity().isEmpty()) {
            throw new InvalidDataException("City is required");
        }
        if (addressDTO.getState() == null || addressDTO.getState().isEmpty()) {
            throw new InvalidDataException("State is required");
        }
    }
}
