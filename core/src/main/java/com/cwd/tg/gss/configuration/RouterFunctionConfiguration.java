package com.cwd.tg.gss.configuration;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.cwd.tg.gss.data.character.GameCharacter;
import com.cwd.tg.gss.data.events.adventure.Adventure;
import com.cwd.tg.gss.data.events.awards.Awards;
import com.cwd.tg.gss.data.events.battle.BattleInfo;
import com.cwd.tg.gss.data.events.battle.Monster;
import com.cwd.tg.gss.handlers.AdminRequestHandler;
import com.cwd.tg.gss.handlers.AdventureRequestHandler;
import com.cwd.tg.gss.handlers.AwardsRequestHandler;
import com.cwd.tg.gss.handlers.BattleInfoRequestHandler;
import com.cwd.tg.gss.handlers.GameCharacterRequestHandler;
import com.cwd.tg.gss.handlers.MonsterRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouterFunctionConfiguration {

    @Bean
    public RouterFunction routerFunction(
            AdminRequestHandler adminRequestHandler,
            GameCharacterRequestHandler gameCharacterRequestHandler,
            AdventureRequestHandler adventureRequestHandler,
            AwardsRequestHandler awardsRequestHandler,
            BattleInfoRequestHandler battleInfoRequestHandler,
            MonsterRequestHandler monsterRequestHandler) {
        return route(GET("/private/character/user/{id}/"),
                request -> ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                        .flatMap(gameCharacterRequestHandler::getUsersGameCharacter), GameCharacter.class))
                .andRoute(POST("/private/character/"),
                        request -> ServerResponse.ok().body(request.body(toMono(GameCharacter.class))
                                .flatMap(gameCharacterRequestHandler::createCharacter), GameCharacter.class))
                .andRoute(PUT("/private/character/"),
                        request -> ServerResponse.ok().body(request.body(toMono(GameCharacter.class))
                                .flatMap(gameCharacterRequestHandler::updateCharacter), GameCharacter.class))
                .andRoute(GET("/private/character/all"),
                        request -> ServerResponse.ok()
                                .body(gameCharacterRequestHandler.getAllGameCharacters(), GameCharacter.class))

                .andRoute(GET("/private/adventure/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(adventureRequestHandler::getAdventureById), Adventure.class))
                .andRoute(GET("/private/adventure/character/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .flux()
                                .flatMap(adventureRequestHandler::getAdventureByCharacterId), Adventure.class))
                .andRoute(GET("/private/adventure/active/character/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(adventureRequestHandler::getActiveAdventureByCharacterId), Adventure.class))
                .andRoute(POST("/private/adventure"), request ->
                        ServerResponse.ok().body(request.bodyToMono(Adventure.class)
                                .flatMap(adventureRequestHandler::createAdventure), Adventure.class))
                .andRoute(PUT("/private/adventure"), request ->
                        ServerResponse.ok().body(request.bodyToMono(Adventure.class)
                                .flatMap(adventureRequestHandler::updateAdventure), Adventure.class))
                .andRoute(DELETE("/private/adventure/{id}"), request ->
                        ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(adventureRequestHandler::deleteAdventure)))
                .andRoute(DELETE("/private/adventure/character/{id}"), request ->
                        ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(adventureRequestHandler::deleteAdventureByCharacterId)))

                .andRoute(GET("/private/awards/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(awardsRequestHandler::getAwards), Awards.class))
                .andRoute(POST("/private/awards"), request ->
                        ServerResponse.ok().body(request.bodyToMono(Awards.class)
                                .flatMap(awardsRequestHandler::createAwards), Awards.class))
                .andRoute(DELETE("/private/awards/{id}"), request ->
                        ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(awardsRequestHandler::deleteAwards)))

                .andRoute(GET("/private/battle/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(battleInfoRequestHandler::getBattleInfo), BattleInfo.class))
                .andRoute(GET("/private/battle/character/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(battleInfoRequestHandler::getBattleInfoByCharacterId), BattleInfo.class))
                .andRoute(POST("/private/battle"), request ->
                        ServerResponse.ok().body(request.bodyToMono(BattleInfo.class)
                                .flatMap(battleInfoRequestHandler::createBattleInfo), BattleInfo.class))
                .andRoute(DELETE("/private/battle/{id}"), request ->
                        ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(battleInfoRequestHandler::deleteBattleInfo)))
                .andRoute(DELETE("/private/battle/character/{id}"), request ->
                        ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(battleInfoRequestHandler::deleteBattleInfoByCharacterId)))

                .andRoute(GET("/private/monster/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(monsterRequestHandler::getMonster), Monster.class))
                .andRoute(POST("/private/monster"), request ->
                        ServerResponse.ok().body(request.bodyToMono(Monster.class)
                                .flatMap(monsterRequestHandler::createMonster), Monster.class))
                .andRoute(PUT("/private/monster"), request ->
                        ServerResponse.ok().body(request.bodyToMono(Monster.class)
                                .flatMap(monsterRequestHandler::updateMonster), Monster.class))
                .andRoute(DELETE("/private/monster/{id}"), request ->
                        ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(monsterRequestHandler::deleteMonster)))

                .andRoute(DELETE("/admin/character/delete"), request -> ServerResponse.noContent()
                        .build(adminRequestHandler.deleteAllCharacters()));
    }

}
