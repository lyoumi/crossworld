package com.rpg.web.crossworld.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class CharacterStatsInfo implements Serializable {
    private int power;
    private int agility;
    private int intelligence;

    private long hitPoints;
    private long manaPoints;
    private long attack;
}
