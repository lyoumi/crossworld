package com.crossworld.web.mappers;

import com.crossworld.web.data.GameEvent;
import com.crossworld.web.entities.EventEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface EventEntityToGameEventMapper extends Converter<EventEntity, GameEvent> {

    @Override
    GameEvent convert(EventEntity source);
}
