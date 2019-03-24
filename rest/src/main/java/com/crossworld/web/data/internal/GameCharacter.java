package com.crossworld.web.data.internal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameCharacter {
    private String id;
    private String name;
    private boolean hasEvent;
    private String userId;
    private CharacterProgress progress = new CharacterProgress();
    private CharacterStats stats = new CharacterStats();
    private GameInventory gameInventory = new GameInventory();

    public GameCharacter(String id, String name, boolean hasEvent, String userId) {
        this.id = id;
        this.name = name;
        this.hasEvent = hasEvent;
        this.userId = userId;
    }
}
