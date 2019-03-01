package com.rpg.web.crossworld.entities;

import lombok.Data;

import java.util.List;

@Data
public class AdventureEventInfo extends EventDetailsInfo {

    private List<String> adventureEvents;
    private int step;
}
