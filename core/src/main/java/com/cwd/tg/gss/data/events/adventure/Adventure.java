package com.cwd.tg.gss.data.events.adventure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Adventure {
    private String id;
    private String characterId;
    private String description;
    private String awardsId;
    private AdventureStatus status;
    private List<String> adventureEvents;
    private int step;

    private DateTime createdAt;
    private DateTime lastModifiedAt;
}
