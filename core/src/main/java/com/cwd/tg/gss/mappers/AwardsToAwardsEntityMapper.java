package com.cwd.tg.gss.mappers;

import com.cwd.tg.gss.data.events.awards.Awards;
import com.cwd.tg.gss.entities.AwardsEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface AwardsToAwardsEntityMapper extends Converter<Awards, AwardsEntity> {
    @Override
    AwardsEntity convert(Awards source);
}
