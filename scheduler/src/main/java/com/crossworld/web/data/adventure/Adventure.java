package com.crossworld.web.data.adventure;

import lombok.Data;

import java.util.List;

@Data
public class Adventure {
    private String id;
    private String description;
    private String awardId;
    private String currentAction;
    private List<String> adventureEvents;
    private int step;

}
