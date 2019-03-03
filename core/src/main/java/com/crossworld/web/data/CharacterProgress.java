package com.crossworld.web.data;

import lombok.Data;

@Data
public class CharacterProgress {
    private long currentExp;
    private int level;
    private long targetExp;
}
