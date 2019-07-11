package com.crossworld.web.data;

import lombok.Data;

@Data
public class EventDetails {
    private EventType eventType;
    private long experience;
    private long gold;
}
