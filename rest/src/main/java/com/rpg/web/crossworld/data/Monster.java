package com.rpg.web.crossworld.data;

import lombok.Data;

@Data
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
