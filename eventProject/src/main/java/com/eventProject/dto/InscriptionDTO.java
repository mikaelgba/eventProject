package com.eventProject.dto;

public class InscriptionDTO {

    private String userId;

    private String eventId;


    public InscriptionDTO(
            String userId,
            String eventId
    ) {
        this.userId = userId;
        this.eventId = eventId;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getEventId() { return eventId; }

    public void setEventId(String eventId) { this.eventId = eventId; }
}
