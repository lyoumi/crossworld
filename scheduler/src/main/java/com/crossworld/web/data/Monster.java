package com.crossworld.web.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monster {

    private long hitPoints;
    private long attack;

    public long getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(long hitPoints) {
        this.hitPoints = hitPoints;
    }

    public long getAttack() {
        return attack;
    }

    public void setAttack(long attack) {
        this.attack = attack;
    }
}
