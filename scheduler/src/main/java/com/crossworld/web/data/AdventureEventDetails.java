package com.crossworld.web.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdventureEventDetails extends EventDetails {

    private List<String> adventureEvents;
    private int step;
}
