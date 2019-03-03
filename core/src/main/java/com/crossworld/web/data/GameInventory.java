package com.crossworld.web.data;

import lombok.Data;

import java.util.Map;

@Data
public class GameInventory {
    private long gold;
    private long experience;
    private long healingHitPointItems;
    private long healingManaPointItems;
    private Map<EquipmentSlot, EquipmentItem> equipemnt;
}
