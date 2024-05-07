package com.eventProject.dto;

import com.eventProject.enuns.UserRole;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class UserDTO {

    private String name;

    private String cpf;

    private String login;

    private String password;

    private UserRole role;

    private LocalDateTime created;

    private LocalDateTime updated;

    public UserDTO(
            String name,
            String cpf,
            String login,
            String password,
            UserRole role,
            LocalDateTime created,
            LocalDateTime updated
    ) {
        this.name = name;
        this.cpf = cpf;
        this.login = login;
        this.password = password;
        this.role = role;
        this.created = created;
        this.updated = updated;
    }

    public UserDTO() { }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }

    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getCreated() { return created; }

    public void setCreated(LocalDateTime created) { this.created = created; }

    public LocalDateTime getUpdated() { return updated; }

    public void setUpdated(LocalDateTime updated) { this.updated = updated; }
}
