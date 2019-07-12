package com.crossworld.web.data.events.battle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monster {
    private String id;
    private long hitPoints;
    private long attack;
    private MonsterType monsterType;
}
