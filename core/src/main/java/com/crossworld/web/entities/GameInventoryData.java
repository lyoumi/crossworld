package com.crossworld.web.entities;

import com.crossworld.web.data.character.EquipmentItem;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class GameInventoryData implements Serializable {
    private long gold;
    private long healingHitPointItems;
    private long healingManaPointItems;
    private Map<String, EquipmentItem> equipment;
}
