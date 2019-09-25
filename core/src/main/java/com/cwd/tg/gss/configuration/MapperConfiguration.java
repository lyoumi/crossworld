package com.cwd.tg.gss.configuration;

import com.cwd.tg.gss.mappers.AdventureEntityToAdventureMapper;
import com.cwd.tg.gss.mappers.AdventureToAdventureEntityMapper;
import com.cwd.tg.gss.mappers.AwardsEntityToAwardsMapper;
import com.cwd.tg.gss.mappers.AwardsToAwardsEntityMapper;
import com.cwd.tg.gss.mappers.BattleEntityToBattleInfoMapper;
import com.cwd.tg.gss.mappers.BattleInfoToBattleEntityMapper;
import com.cwd.tg.gss.mappers.GameCharacterEntityToGameCharacterMapper;
import com.cwd.tg.gss.mappers.GameCharacterToGameCharacterEntityMapper;
import com.cwd.tg.gss.mappers.MonsterEntityToMonsterMapper;
import com.cwd.tg.gss.mappers.MonsterToMonsterEntityMapper;
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
        converterRegistry.addConverter(Mappers.getMapper(AdventureEntityToAdventureMapper.class));
        converterRegistry.addConverter(Mappers.getMapper(AdventureToAdventureEntityMapper.class));
    }
}
