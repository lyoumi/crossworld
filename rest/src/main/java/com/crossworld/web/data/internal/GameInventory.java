package com.crossworld.web.data.internal;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GameInventory {
    private long gold;
    private long experience;
    private long healingHitPointItems;
    private long healingManaPointItems;
    private Map<EquipmentSlot, EquipmentItem> equipment = new HashMap<>();
}
