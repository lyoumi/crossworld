package com.crossworld.web.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class CharacterStatsData implements Serializable {
    private int power;
    private int agility;
    private int intelligence;

    private long hitPoints;
    private long manaPoints;
    private long attack;
}
