package com.crossworld.web.data.internal;

import lombok.Data;

import java.util.List;

@Data
public class AdventureEventDetails extends EventDetails {

    private List<String> adventureEvents;
    private int step;
}