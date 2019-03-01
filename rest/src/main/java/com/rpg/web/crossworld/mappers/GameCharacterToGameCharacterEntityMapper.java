package com.rpg.web.crossworld.mappers;

import com.rpg.web.crossworld.data.GameCharacter;
import com.rpg.web.crossworld.entities.GameCharacterEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface GameCharacterToGameCharacterEntityMapper extends Converter<GameCharacter, GameCharacterEntity> {
    @Override
    GameCharacterEntity convert(GameCharacter source);
}
