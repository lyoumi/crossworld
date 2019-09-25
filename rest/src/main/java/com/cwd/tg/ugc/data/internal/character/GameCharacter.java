package com.cwd.tg.ugc.data.internal.character;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
