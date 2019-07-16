package com.crossworld.web.resources;

import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.repositories.MonsterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public Mono<Monster> getMonster(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "id") String id) {
        return monsterRepository
                .getMonsterById(id)
                .doOnError(throwable -> log.error("Unable to get monster { request_id: {}, monster_id: {} }",
                                requestId, id, throwable));
    }

    @PutMapping
    public Mono<Monster> createMonster(
            @RequestHeader(value = "request_id") String requestId,
            @RequestBody Monster monster) {
        return monsterRepository
                .saveMonster(monster)
                .doOnError(throwable -> log.error("Unable to create monster { request_id: {}, request_body: {} }",
                        requestId, monster, throwable));
    }

    @PostMapping
    public Mono<Monster> updateMonster(
            @RequestHeader(value = "request_id") String requestId,
            @RequestBody Monster monster) {
        return monsterRepository
                .saveMonster(monster)
                .doOnError(throwable -> log.error("Unable to create monster { request_id: {}, request_body: {} }",
                                requestId, monster, throwable));
    }

    @DeleteMapping(value = "{id}")
    public Mono<Void> deleteMonster(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "id") String id) {
        return monsterRepository
                .deleteMonster(id)
                .doOnError(throwable -> log.error("Unable to delete monster { request_id: {}, monster_id: {} }",
                        requestId, id, throwable));
    }

}
