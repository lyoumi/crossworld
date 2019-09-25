package com.cwd.tg.gss.converters;

import com.cwd.tg.gss.data.character.GameCharacter;
import com.cwd.tg.gss.data.events.adventure.Adventure;
import com.cwd.tg.gss.data.events.awards.Awards;
import com.cwd.tg.gss.data.events.battle.BattleInfo;
import com.cwd.tg.gss.data.events.battle.Monster;
import com.cwd.tg.gss.entities.AdventureEntity;
import com.cwd.tg.gss.entities.AwardsEntity;
import com.cwd.tg.gss.entities.BattleEntity;
import com.cwd.tg.gss.entities.GameCharacterEntity;
import com.cwd.tg.gss.entities.MonsterEntity;

public interface ConverterService {

    <T> T convert(Object source, Class<T> type);

    default GameCharacter convert(GameCharacterEntity entity) {
        return convert(entity, GameCharacter.class);
    }

    default GameCharacterEntity convert(GameCharacter gameCharacter) {
        return convert(gameCharacter, GameCharacterEntity.class);
    }

    default Awards convert(AwardsEntity entity) {
        return convert(entity, Awards.class);
    }

    default AwardsEntity convert(Awards awards) {
        return convert(awards, AwardsEntity.class);
    }

    default Monster convert(MonsterEntity entity) {
        return convert(entity, Monster.class);
    }

    default MonsterEntity convert(Monster monster) {
        return convert(monster, MonsterEntity.class);
    }

    default BattleInfo convert(BattleEntity entity) {
        return convert(entity, BattleInfo.class);
    }

    default BattleEntity convert(BattleInfo battleInfo) {
        return convert(battleInfo, BattleEntity.class);
    }

    default Adventure convert(AdventureEntity entity) {
        return convert(entity, Adventure.class);
    }

    default AdventureEntity convert(Adventure adventure) {
        return convert(adventure, AdventureEntity.class);
    }
}
