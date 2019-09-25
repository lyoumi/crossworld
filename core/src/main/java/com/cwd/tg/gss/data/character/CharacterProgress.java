package com.cwd.tg.gss.data.character;

import lombok.Data;

@Data
public class CharacterProgress {
    private long currentExp;
    private int level;
    private long targetExp;
}
