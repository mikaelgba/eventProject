package com.eventProject.integration.repositories;

import com.eventProject.models.Address;
import com.eventProject.repositories.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class AddressRepositoryIntegrationTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void findByAddressId() {

        Address address = new Address("1", "Rua A", "Cidade A", "Estado A", "Cep A", null, null);
        addressRepository.save(address);

        Optional<Address> foundAddressOptional = addressRepository.findById(address.getId());

        assertTrue(foundAddressOptional.isPresent());
        Address foundAddress = foundAddressOptional.get();
        assertEquals("Rua A", foundAddress.getStreet());
        assertEquals("Cidade A", foundAddress.getCity());
        assertEquals("Estado A", foundAddress.getState());
    }

    @Test
    public void findByStreetCityAndState() {

        Address address = new Address("1", "Rua B", "Cidade B", "Estado B", "Cep B", null, null);
        addressRepository.save(address);

        Address foundAddress = addressRepository.findByStreetCityAndState("Rua B", "Cidade B", "Estado B");

        assertEquals("Rua B", foundAddress.getStreet());
        assertEquals("Cidade B", foundAddress.getCity());
        assertEquals("Estado B", foundAddress.getState());
    }
}