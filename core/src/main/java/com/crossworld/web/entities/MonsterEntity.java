package com.crossworld.web.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class MonsterEntity implements Serializable {
    @Id
    private String id;
    private long hitPoints;
    private long attack;
    private String monsterType;
}
