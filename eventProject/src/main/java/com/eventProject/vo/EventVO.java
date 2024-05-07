package com.eventProject.vo;

import com.eventProject.models.Address;
import com.eventProject.models.Event;

import java.time.LocalDateTime;

public class EventVO {

    private String id;
    private String name;
    private String date;
    private Address address;
    private int capMax;
    private int capActual;
    private LocalDateTime created;
    private LocalDateTime updated;

    public EventVO(Event event) {
        if (event != null) {
            this.id = event.getId();
            this.name = event.getName();
            this.date = event.getDate();
            this.address = event.getAddress();
            this.capMax = event.getCapMax();
            this.capActual = event.getCapActual();
            this.created = event.getCreated();
            this.updated = event.getUpdated();
        }
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }

    public int getCapMax() { return capMax; }

    public void setCapMax(int capMax) { this.capMax = capMax; }

    public int getCapActual() { return capActual; }

    public void setCapActual(int capActual) { this.capActual = capActual; }

    public LocalDateTime getCreated() { return created; }

    public void setCreated(LocalDateTime created) { this.created = created; }

    public LocalDateTime getUpdated() { return updated; }

    public void setUpdated(LocalDateTime updated) { this.updated = updated; }
}
