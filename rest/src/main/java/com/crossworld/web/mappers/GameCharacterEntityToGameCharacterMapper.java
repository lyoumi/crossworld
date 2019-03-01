package com.crossworld.web.mappers;

import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.entities.GameCharacterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface GameCharacterEntityToGameCharacterMapper extends Converter<GameCharacterEntity, GameCharacter> {
    @Mapping(source = "gameInventory", target = "gameInventory")
    @Override
    GameCharacter convert(GameCharacterEntity source);
}
