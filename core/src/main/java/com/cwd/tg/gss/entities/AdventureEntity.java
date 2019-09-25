package com.cwd.tg.gss.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document
public class AdventureEntity implements Serializable {
    @Id
    private String id;
    private String characterId;
    private String description;
    private String awardsId;
    private String status;
    private List<String> adventureEvents;
    private int step;
}
