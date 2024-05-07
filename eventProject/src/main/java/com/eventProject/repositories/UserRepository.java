package com.eventProject.repositories;

import com.eventProject.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByLogin(String Login);

    Optional<User> findByCpf(String cpf);

    @Query("SELECT u FROM users u WHERE u.id = :idParam")
    User findByIdUser(@Param("idParam") String id);

    @Query("SELECT u FROM users u WHERE u.cpf = :cpfParam")
    User findByUserCpf(@Param("cpfParam") String cpf);

    @Query("SELECT u FROM users u WHERE LOWER(u.name) LIKE LOWER(concat('%', :nameOrCpfParam, '%')) OR " +
            "LOWER(u.cpf) LIKE LOWER(concat('%', :nameOrCpfParam, '%'))")
    Page<User> findByNameOrCpf(@Param("nameOrCpfParam") String nameOrCpf, Pageable pageable);

    @Query("SELECT u FROM users u WHERE u.login = :loginParam")
    User findByUserLogin(@Param("loginParam") String login);

}
