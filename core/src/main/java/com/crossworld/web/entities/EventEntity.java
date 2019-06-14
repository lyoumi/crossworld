package com.crossworld.web.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class EventEntity implements Serializable {
    private String id;
    private String gameCharacterId;
    private String eventStatus;
    private String currentAction;
    private EventDetailsInfo eventDetails;
}
