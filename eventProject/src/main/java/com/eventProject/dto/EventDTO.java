package com.eventProject.dto;

import java.time.LocalDateTime;

public class EventDTO {

    private String name;
    private String date;
    private AddressDTO address;
    private int capMax;
    private int capActual;
    private LocalDateTime created;
    private LocalDateTime updated;

    public EventDTO(
            String name,
            String date,
            AddressDTO address,
            int capMax,
            int capActual,
            LocalDateTime created,
            LocalDateTime updated) {
        this.name = name;
        this.date = date;
        this.address = address;
        this.capMax = capMax;
        this.capActual = capActual;
        this.created = created;
        this.updated = updated;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public AddressDTO getAddress() { return address; }

    public void setAddress(AddressDTO address) { this.address = address; }

    public int getCapMax() { return capMax; }

    public void setCapMax(int capMax) { this.capMax = capMax; }

    public int getCapActual() { return capActual; }

    public void setCapActual(int capActual) { this.capActual = capActual; }

    public LocalDateTime getCreated() { return created; }

    public void setCreated(LocalDateTime created) { this.created = created; }

    public LocalDateTime getUpdated() { return updated; }

    public void setUpdated(LocalDateTime updated) { this.updated = updated; }
}
