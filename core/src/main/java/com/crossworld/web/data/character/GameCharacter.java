package com.crossworld.web.data.character;

import lombok.Data;

@Data
public class GameCharacter {
    private String id;
    private String name;
    private boolean inAdventure;
    private boolean isFighting;
    private boolean isResting;
    private String currentAction;
    private CharacterProgress progress;
    private CharacterStats stats;
    private GameInventory gameInventory;
    private String userId;
}
