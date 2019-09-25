package com.cwd.tg.gss.mappers;

import com.cwd.tg.gss.data.events.awards.Awards;
import com.cwd.tg.gss.entities.AwardsEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface AwardsEntityToAwardsMapper extends Converter<AwardsEntity, Awards> {
    @Override
    Awards convert(AwardsEntity source);
}
