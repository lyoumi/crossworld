package com.crossworld.web.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class BattleEntity implements Serializable {
    @Id
    private String id;
    private String characterId;
    private String monsterId;
    private String awardsId;
}
