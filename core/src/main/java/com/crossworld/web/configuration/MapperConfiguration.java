package com.crossworld.web.configuration;

import com.crossworld.web.mappers.EventEntityToGameEventMapper;
import com.crossworld.web.mappers.GameCharacterEntityToGameCharacterMapper;
import com.crossworld.web.mappers.GameCharacterToGameCharacterEntityMapper;
import com.crossworld.web.mappers.GameEventToEventEntityMapper;

import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterRegistry;

import javax.annotation.PostConstruct;

@Configuration
@AllArgsConstructor
public class MapperConfiguration {

    private final ConverterRegistry converterRegistry;

    @PostConstruct
    public void init() {
        converterRegistry.addConverter(Mappers.getMapper(EventEntityToGameEventMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(GameCharacterEntityToGameCharacterMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(GameCharacterToGameCharacterEntityMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(GameEventToEventEntityMapper.class));
    }
}
