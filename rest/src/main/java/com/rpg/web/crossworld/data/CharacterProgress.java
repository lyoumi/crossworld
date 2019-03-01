package com.rpg.web.crossworld.data;

import lombok.Data;

@Data
public class CharacterProgress {
    private long currentExp;
    private int level;
    private long targetExp;
}
