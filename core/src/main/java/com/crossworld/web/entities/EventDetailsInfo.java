package com.crossworld.web.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventDetailsInfo implements Serializable {
    private String eventType;
    private long experience;
    private long gold;
}
