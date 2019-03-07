package com.crossworld.web.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEvent {
    private String id;
    private String gameCharacterId;
    private String currentAction;
    private EventStatus eventStatus;
    private EventDetails eventDetails;
}
