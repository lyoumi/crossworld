package com.cwd.tg.gps.processors.util;

import com.cwd.tg.gps.data.events.adventure.Adventure;

import reactor.core.publisher.Mono;

public interface EventGeneratorUtils {

    Mono<Adventure> generateAdventure(String characterId, String awardsId);
}
