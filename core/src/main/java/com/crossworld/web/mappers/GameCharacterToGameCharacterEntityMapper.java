package com.crossworld.web.mappers;

import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.entities.GameCharacterEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface GameCharacterToGameCharacterEntityMapper extends Converter<GameCharacter, GameCharacterEntity> {
    @Override
    GameCharacterEntity convert(GameCharacter source);
}
