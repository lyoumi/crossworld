package com.crossworld.web.resources;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.errors.exceptions.AwardsNotFoundException;
import com.crossworld.web.repositories.AwardsRepository;
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
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("private/awards")
public class AwardsResource {

    private final AwardsRepository awardsRepository;

    @ResponseStatus(OK)
    @GetMapping(value = "{id}")
    public Mono<Awards> getAwards(
            @PathVariable(value = "id") String id) {
        return awardsRepository.getAwardsById(id)
                .switchIfEmpty(Mono.error(new AwardsNotFoundException(format("Awards with id %s not found", id))));
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Mono<Awards> createAwards(
            @RequestBody Awards awards) {
        return awardsRepository.saveAwards(awards);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "{id}")
    public Mono<Void> deleteAwards(
            @PathVariable(value = "id") String id) {
        return awardsRepository.deleteAwards(id);
    }
}
