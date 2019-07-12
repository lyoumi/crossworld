package com.crossworld.web.mappers;

import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.entities.AdventureEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface AdventureEntityToAdventureMapper extends Converter<AdventureEntity, Adventure> {
    @Override
    Adventure convert(AdventureEntity source);
}
