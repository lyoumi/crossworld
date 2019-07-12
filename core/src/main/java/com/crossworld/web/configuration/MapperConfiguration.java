package com.crossworld.web.configuration;

import com.crossworld.web.mappers.AwardsEntityToAwardsMapper;
import com.crossworld.web.mappers.AwardsToAwardsEntityMapper;
import com.crossworld.web.mappers.BattleEntityToBattleInfoMapper;
import com.crossworld.web.mappers.BattleInfoToBattleEntityMapper;
import com.crossworld.web.mappers.GameCharacterEntityToGameCharacterMapper;
import com.crossworld.web.mappers.GameCharacterToGameCharacterEntityMapper;
import com.crossworld.web.mappers.MonsterEntityToMonsterMapper;
import com.crossworld.web.mappers.MonsterToMonsterEntityMapper;
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
        converterRegistry.addConverter(Mappers.getMapper(GameCharacterEntityToGameCharacterMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(GameCharacterToGameCharacterEntityMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(AwardsToAwardsEntityMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(AwardsEntityToAwardsMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(MonsterToMonsterEntityMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(MonsterEntityToMonsterMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(BattleInfoToBattleEntityMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(BattleEntityToBattleInfoMapper.class));
    }
}
