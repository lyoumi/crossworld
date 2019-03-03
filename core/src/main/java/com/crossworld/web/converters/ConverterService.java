package com.crossworld.web.converters;

import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.entities.EventEntity;
import com.crossworld.web.entities.GameCharacterEntity;

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
