package com.eventProject.repositories;

import com.eventProject.models.Inscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, String> {

    @Query("SELECT i FROM inscriptions i WHERE LOWER(i.event.name) LIKE LOWER(concat('%', :nameEventParam, '%'))")
    Page<Inscription> findByNameEvent(@Param("nameEventParam") String nameEvent, Pageable pageable);

    @Query("SELECT i FROM inscriptions i WHERE LOWER(i.user.name) LIKE LOWER(concat('%', :cpfOrNameUserParam, '%')) OR " +
            "LOWER(i.user.cpf) LIKE LOWER(concat('%', :cpfOrNameUserParam, '%'))")
    Page<Inscription> findByCpfOrNameUser(@Param("cpfOrNameUserParam") String cpfOrNameUser, Pageable pageable);

}
