package com.cwd.tg.gss.entities;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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

    @CreatedDate
    private DateTime createdAt;
    @LastModifiedDate
    private DateTime lastModifiedAt;
}
