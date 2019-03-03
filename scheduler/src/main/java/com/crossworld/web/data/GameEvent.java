package com.crossworld.web.data;

import lombok.Data;

@Data
public class GameEvent {
    private String id;
    private String gameCharacterId;
    private String currentAction;
    private EventStatus eventStatus;
    private EventType eventType;
    private EventDetails eventDetails;
}
