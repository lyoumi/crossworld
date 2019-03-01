package com.rpg.web.crossworld.mappers;

import com.rpg.web.crossworld.data.GameEvent;
import com.rpg.web.crossworld.entities.EventEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface GameEventToEventEntityMapper extends Converter<GameEvent, EventEntity> {

    @Override
    EventEntity convert(GameEvent source);
}
