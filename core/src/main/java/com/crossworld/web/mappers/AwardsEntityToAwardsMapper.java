package com.crossworld.web.mappers;

import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.entities.AwardsEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface AwardsEntityToAwardsMapper extends Converter<AwardsEntity, Awards> {
    @Override
    Awards convert(AwardsEntity source);
}
