package com.crossworld.web.converters;

import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.entities.AdventureEntity;
import com.crossworld.web.entities.AwardsEntity;
import com.crossworld.web.entities.BattleEntity;
import com.crossworld.web.entities.GameCharacterEntity;
import com.crossworld.web.entities.MonsterEntity;

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
