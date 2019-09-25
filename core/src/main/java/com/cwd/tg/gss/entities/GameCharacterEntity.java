package com.cwd.tg.gss.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class GameCharacterEntity implements Serializable {
    @Id
    private String id;
    private String name;
    private boolean inAdventure;
    private boolean isFighting;
    private boolean isResting;
    private String currentAction;
    private CharacterProgressInfo progress;
    private CharacterStatsData stats;
    private GameInventoryData gameInventory;
    private String userId;
}
