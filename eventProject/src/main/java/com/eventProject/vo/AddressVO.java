package com.eventProject.vo;

import com.eventProject.models.Address;

import java.time.LocalDateTime;

public class AddressVO {

    private String id;
    private String street;
    private String city;
    private String state;
    private String cep;
    private LocalDateTime created;
    private LocalDateTime updated;

    public AddressVO(Address address) {
        if (address != null) {
            this.id = address.getId();
            this.street = address.getStreet();
            this.city = address.getCity();
            this.state = address.getState();
            this.cep = address.getCep();
            this.created = address.getCreated();
            this.updated = address.getUpdated();
        }
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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
