package com.eventProject.repositories;

import com.eventProject.models.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    @Query("SELECT a FROM address a WHERE a.id = :id")
    Address findByAddressId(@Param("id") String id);

    @Query("SELECT a FROM address a WHERE a.street = :street AND a.city = :city AND a.state = :state")
    Address findByStreetCityAndState(@Param("street") String street,
                                     @Param("city") String city,
                                     @Param("state") String state);
}
