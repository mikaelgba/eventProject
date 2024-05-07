package com.eventProject.repositories;

import com.eventProject.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    Event findByName(String name);

    @Query("SELECT e FROM events e WHERE e.id = :idParam")
    Event findByIdEvent(@Param("idParam")String id);

    @Query("SELECT e FROM events e WHERE LOWER(e.name) LIKE LOWER(concat('%', :nameParam, '%'))")
    Page<Event> findByNameEvent(@Param("nameParam") String nameParam, Pageable pageable);

    @Query("SELECT COUNT(e) FROM events e WHERE e.address.id = :addressId")
    Long countByAddressId(@Param("addressId") String addressId);
}