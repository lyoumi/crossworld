package com.crossworld.web.entities;

import com.crossworld.web.data.EquipmentItem;
import com.crossworld.web.data.EquipmentSlot;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class GameInventoryInfo implements Serializable {
    private long gold;
    private long experience;
    private long healingHitPointItems;
    private long healingManaPointItems;
    private Map<EquipmentSlot, EquipmentItem> equipemnt;
}
