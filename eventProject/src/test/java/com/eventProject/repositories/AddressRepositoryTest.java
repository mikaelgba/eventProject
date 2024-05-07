package com.eventProject.repositories;

import com.eventProject.models.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AddressRepositoryTest {

    @Autowired
    AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setCep("12345678");
        address.setCreated(LocalDateTime.now());
        address.setUpdated(LocalDateTime.now());
        addressRepository.save(address);
    }

    @Test
    void findByAddressId_when_exists() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setCep("12345678");
        address.setCreated(LocalDateTime.now());
        address.setUpdated(LocalDateTime.now());
        Address addressSaved = addressRepository.save(address);
        Address addressFind = addressRepository.findByAddressId(addressSaved.getId());

        assertThat(addressFind).isNotNull();
        assertEquals(addressFind.getId(), addressSaved.getId());
    }

    @Test
    void findByAddressId_when_not_exists() {

        String addressId = "id_inexistente";
        Address address = addressRepository.findByAddressId(addressId);

        assertThat(address).isNull();
    }

    @Test
    void findByStreetCityAndState_when_exists() {

        String street = "123 Main St";
        String city = "City";
        String state = "State";

        Address address = addressRepository.findByStreetCityAndState(street, city, state);

        assertThat(address).isNotNull();
        assertThat(address.getStreet()).isEqualTo(street);
        assertThat(address.getCity()).isEqualTo(city);
        assertThat(address.getState()).isEqualTo(state);
    }

    @Test
    void findByStreetCityAndState_when_not_exists() {

        String street = "Nonexistent St";
        String city = "Nonexistent City";
        String state = "Nonexistent State";

        Address address = addressRepository.findByStreetCityAndState(street, city, state);

        assertThat(address).isNull();
    }
}