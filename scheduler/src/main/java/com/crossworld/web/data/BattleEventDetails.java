package com.crossworld.web.data;

import com.crossworld.web.data.battle.Monster;
import lombok.Data;

@Data
public class BattleEventDetails extends EventDetails {
    private Monster monster;
}
