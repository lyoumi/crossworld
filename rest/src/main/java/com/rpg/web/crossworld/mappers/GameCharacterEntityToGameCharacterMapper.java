package com.rpg.web.crossworld.mappers;

import com.rpg.web.crossworld.data.GameCharacter;
import com.rpg.web.crossworld.entities.GameCharacterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface GameCharacterEntityToGameCharacterMapper extends Converter<GameCharacterEntity, GameCharacter> {
    @Mapping(source = "gameInventory", target = "gameInventory")
    @Override
    GameCharacter convert(GameCharacterEntity source);
}
