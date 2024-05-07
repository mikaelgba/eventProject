package com.eventProject.services.impl.converts;

import com.eventProject.dto.AddressDTO;
import com.eventProject.dto.EventDTO;
import com.eventProject.dto.UserDTO;
import com.eventProject.models.Address;
import com.eventProject.models.Event;
import com.eventProject.models.User;

public class Converters {

    public UserDTO convertUserToDTO(User user) {
        return new UserDTO(
                user.getName(),
                user.getCpf(),
                user.getLogin(),
                user.getPassword(),
                user.getRole(),
                user.getCreated(),
                user.getUpdated()
        );
    }

    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setCpf(userDTO.getCpf());
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setCreated(userDTO.getCreated());
        user.setUpdated(userDTO.getUpdated());
        return user;
    }

    public EventDTO convertEventToDTO(Event event) {

        AddressDTO addressDTO = convertToAddressDTO(event.getAddress());

        return new EventDTO(
                event.getName(),
                event.getDate(),
                addressDTO,
                event.getCapMax(),
                event.getCapActual(),
                event.getCreated(),
                event.getUpdated()
        );
    }

    public Event convertToEvent(EventDTO eventDTO) {

        Address address = convertToAddress(eventDTO.getAddress());

        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setAddress(address);
        event.setCapMax(eventDTO.getCapMax());
        event.setCapActual(eventDTO.getCapActual());
        event.setCreated(eventDTO.getCreated());
        event.setUpdated(eventDTO.getUpdated());
        return event;
    }

    public AddressDTO convertToAddressDTO(Address address) {
        return new AddressDTO(
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCep(),
                address.getCreated(),
                address.getUpdated()
        );
    }

    public Address convertToAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCep(addressDTO.getCep());
        address.setCreated(addressDTO.getCreated());
        address.setUpdated(addressDTO.getUpdated());
        return address;
    }

}
