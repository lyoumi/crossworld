package com.crossworld.web.data;

import lombok.Data;

@Data
public class GameCharacter {
    private String id;
    private String name;
    private boolean hasEvent;
    private CharacterProgress progress;
    private CharacterStats stats;
    private GameInventory gameInventory;
}
