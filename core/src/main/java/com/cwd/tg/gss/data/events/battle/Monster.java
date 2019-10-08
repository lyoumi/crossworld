package com.cwd.tg.gss.data.events.battle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monster {
    private String id;
    private long hitPoints;
    private long attack;
    private MonsterType monsterType;

    private DateTime createdAt;
    private DateTime lastModifiedAt;
}
