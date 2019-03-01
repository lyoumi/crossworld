package com.crossworld.web.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
public class GameCharacterEntity implements Serializable {
    private String id;
    private String name;
    private boolean hasEvent;
    private CharacterProgressInfo progress;
    private CharacterStatsInfo stats;
    private GameInventoryInfo gameInventory;
}
