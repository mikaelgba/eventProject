package com.eventProject.vo;

import com.eventProject.models.Event;
import com.eventProject.models.Inscription;
import com.eventProject.models.User;


import java.time.LocalDateTime;

public class InscriptionVO {
    private String id;
    private User user;
    private Event event;
    private LocalDateTime dateInscription;

    public InscriptionVO(Inscription inscription) {
        this.id = inscription.getId();
        this.user = inscription.getUser();
        this.event = inscription.getEvent();
        this.dateInscription = inscription.getDateInscription();
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Event getEvent() { return event; }

    public void setEvent(Event event) { this.event = event; }

    public LocalDateTime getDateInscription() { return dateInscription; }

    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }
}
