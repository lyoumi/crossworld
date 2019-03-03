package com.crossworld.web.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventDetailsInfo implements Serializable {
    private long experience;
    private long gold;
}
