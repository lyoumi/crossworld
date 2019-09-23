package com.crossworld.web.processors.util;

import com.crossworld.web.data.events.adventure.Adventure;
import reactor.core.publisher.Mono;

public interface EventGeneratorUtils {

    Mono<Adventure> generateAdventure(String characterId, String awardsId);
}
