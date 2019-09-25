package com.cwd.tg.gss.mappers;

import com.cwd.tg.gss.data.character.GameCharacter;
import com.cwd.tg.gss.entities.GameCharacterEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface GameCharacterEntityToGameCharacterMapper extends Converter<GameCharacterEntity, GameCharacter> {
    @Override
    GameCharacter convert(GameCharacterEntity source);
}
