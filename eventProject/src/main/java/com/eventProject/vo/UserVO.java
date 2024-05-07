package com.eventProject.vo;

import com.eventProject.enuns.UserRole;
import com.eventProject.models.User;
import java.time.LocalDateTime;

public class UserVO {

    private String id;
    private String name;
    private String cpf;
    private String login;
    private String password;
    private UserRole role;
    private LocalDateTime created;
    private LocalDateTime updated;

    public UserVO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.cpf = user.getCpf();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.created = user.getCreated();
        this.updated = user.getUpdated();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
