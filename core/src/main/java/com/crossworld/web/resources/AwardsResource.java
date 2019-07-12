package com.crossworld.web.resources;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.repositories.AwardsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "id") String id) {
        return awardsRepository
                .getAwardsById(id)
                .doOnSuccess(responseBody -> log.info("Outgoing response {} with body {}", requestId, responseBody))
                .doOnError(throwable -> log.error("Unable to get awards { request_id: {}, awards_id: {} }",
                        requestId, id, throwable));
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Mono<Awards> createAwards(
            @RequestHeader(value = "request_id") String requestId,
            @RequestBody Awards awards) {
        return awardsRepository
                .saveAwards(awards)
                .doOnSuccess(responseBody -> log.info("Outgoing response {} with body {}", requestId, responseBody))
                .doOnError(throwable -> log.error("Unable to create awards { request_id: {}, request_body: {} }",
                        requestId, awards, throwable));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "{id}")
    public Mono<Void> deleteAwards(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "id") String id) {
        return awardsRepository
                .deleteAwards(id)
                .doOnSuccess(aVoid -> log.info("Outgoing response {}", requestId))
                .doOnError(throwable -> log.error("Unable to delete awards { request_id: {}, awards_id: {} }",
                        requestId, id, throwable));
    }
}
