package com.rpg.web.crossworld.converters;

import com.rpg.web.crossworld.data.GameCharacter;
import com.rpg.web.crossworld.data.GameEvent;
import com.rpg.web.crossworld.entities.EventEntity;
import com.rpg.web.crossworld.entities.GameCharacterEntity;

public interface ConverterService {

    <T> T convert(Object source, Class<T> type);

    default GameCharacter convert(GameCharacterEntity entity) {
        return convert(entity, GameCharacter.class);
    }

    default GameCharacterEntity convert(GameCharacter gameCharacter) {
        return convert(gameCharacter, GameCharacterEntity.class);
    }

    default EventEntity convert(GameEvent gameEvent) {
        return convert(gameEvent, EventEntity.class);
    }

    default GameEvent convert(EventEntity eventEntity) {
        return convert(eventEntity, GameEvent.class);
    }
}
