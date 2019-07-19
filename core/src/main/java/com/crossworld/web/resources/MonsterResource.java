package com.crossworld.web.resources;

import static java.lang.String.format;

import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.errors.exceptions.MonsterNotFoundException;
import com.crossworld.web.repositories.MonsterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("private/monster")
public class MonsterResource {

    private final MonsterRepository monsterRepository;

    @GetMapping(value = "{id}")
    public Mono<Monster> getMonster(@PathVariable(value = "id") String id) {
        return monsterRepository.getMonsterById(id)
                .switchIfEmpty(Mono.error(new MonsterNotFoundException(format("Monster with id %s not found", id))));
    }

    @PutMapping
    public Mono<Monster> createMonster(@RequestBody Monster monster) {
        return monsterRepository.saveMonster(monster);
    }

    @PostMapping
    public Mono<Monster> updateMonster(@RequestBody Monster monster) {
        return monsterRepository.saveMonster(monster);
    }

    @DeleteMapping(value = "{id}")
    public Mono<Void> deleteMonster(@PathVariable(value = "id") String id) {
        return monsterRepository.deleteMonster(id);
    }

}
