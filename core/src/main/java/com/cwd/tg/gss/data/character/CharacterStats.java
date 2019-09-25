package com.cwd.tg.gss.data.character;

import lombok.Data;

@Data
public class CharacterStats {
    private int power;
    private int agility;
    private int intelligence;

    private long hitPoints;
    private long manaPoints;
    private long attack;
}
