package com.crossworld.web.resources;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.errors.exceptions.AdventureNotFoundException;
import com.crossworld.web.repositories.AdventureRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("private/adventure")
public class AdventureResource {

    private final AdventureRepository adventureRepository;

    @ResponseStatus(OK)
    @GetMapping(value = "{id}")
    public Mono<Adventure> getAdventureById(
            @PathVariable(value = "id") String id) {
        return adventureRepository.getAdventureById(id)
                .switchIfEmpty(Mono.error(new AdventureNotFoundException(format("Adventure with id %s not found", id))));
    }

    @ResponseStatus(OK)
    @GetMapping(value = "character/{character_id}")
    public Flux<Adventure> getAdventureByCharacterId(
            @PathVariable(value = "character_id") String characterId) {
        return adventureRepository.getAdventureByCharacterId(characterId)
                .switchIfEmpty(Mono.error(new AdventureNotFoundException(
                        format("Adventure with character id %s not found", characterId))));
    }

    @ResponseStatus(OK)
    @GetMapping(value = "active/character/{character_id}")
    public Mono<Adventure> getActiveAdventureByCharacterId(
            @PathVariable(value = "character_id") String characterId) {
        return adventureRepository.getActiveAdventureByCharacterId(characterId)
                .switchIfEmpty(Mono.error(new AdventureNotFoundException(
                        format("Active adventure with character id %s not found", characterId))));
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Mono<Adventure> createAdventure(
            @RequestBody Adventure adventure) {
        return adventureRepository.saveAdventure(adventure);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "{id}")
    public Mono<Void> deleteAdventure(
            @PathVariable(value = "id") String id) {
        return adventureRepository.deleteAdventure(id);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "character/{character_id}")
    public Mono<Void> deleteAdventureByCharacterId(
            @PathVariable(value = "character_id") String characterId) {
        return adventureRepository.deleteAdventureByCharacterId(characterId);
    }
}
