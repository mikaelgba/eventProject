package com.eventProject.dto;

import java.time.LocalDateTime;

public class AddressDTO {

    private String street;
    private String city;
    private String state;
    private String cep;
    private LocalDateTime created;
    private LocalDateTime updated;

    public AddressDTO(
            String street,
            String city,
            String state,
            String cep,
            LocalDateTime created,
            LocalDateTime updated
    ) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.cep = cep;
        this.created = created;
        this.updated = updated;
    }


    public String getStreet() { return street; }

    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getCep() { return cep; }

    public void setCep(String cep) { this.cep = cep; }

    public LocalDateTime getCreated() { return created; }

    public void setCreated(LocalDateTime created) { this.created = created; }

    public LocalDateTime getUpdated() { return updated; }

    public void setUpdated(LocalDateTime updated) { this.updated = updated; }
}
