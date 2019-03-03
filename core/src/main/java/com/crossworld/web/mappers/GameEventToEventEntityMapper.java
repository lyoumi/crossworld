package com.crossworld.web.mappers;

import com.crossworld.web.data.GameEvent;
import com.crossworld.web.entities.EventEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface GameEventToEventEntityMapper extends Converter<GameEvent, EventEntity> {

    @Override
    EventEntity convert(GameEvent source);
}
