package com.crossworld.web.processors.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.events.adventure.AdventureStatus;
import com.crossworld.web.data.events.EventType;
import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.data.events.battle.MonsterType;
import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.processors.BaseGameCharacterProcessor;
import com.crossworld.web.processors.EventProcessor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class BaseGameCharacterProcessorImpl implements BaseGameCharacterProcessor {

    private static final Random RANDOM = new Random();

    //TODO: FTCW-12. Rewrite it using real stories e.g. stored in DB
    private static final String ADVENTURE_DESCRIPTION = "Some description";
    private static final List<String> ADVENTURE_EVENTS = List.of("1st event", "2nd event", "3rd event", "Last event");

    private static final Map<EventType, List<EventType>> EVENT_GENERATION_RESTRICTION_MAP =
            Map.of(EventType.ADVENTURE, Collections.emptyList(),
                    EventType.BATTLE, List.of(EventType.ADVENTURE),
                    EventType.REGENERATION, List.of(EventType.ADVENTURE));
    private final Map<EventType, Consumer<GameCharacter>> eventGenerator;

    private final Map<EventType, Consumer<GameCharacter>> eventProcessors;

    private final CoreWebClient coreWebClient;

    public BaseGameCharacterProcessorImpl(CoreWebClient coreWebClient,
            EventProcessor battleEventProcessor,
            EventProcessor adventureEventProcessor,
            EventProcessor healingEventProcessor) {
        this.coreWebClient = coreWebClient;

        eventProcessors = Map.of(
                EventType.ADVENTURE, adventureEventProcessor::processEvent,
                EventType.BATTLE, battleEventProcessor::processEvent,
                EventType.REGENERATION, healingEventProcessor::processEvent
        );
        eventGenerator = Map.of(EventType.ADVENTURE, this::generateAdventure,
                EventType.BATTLE, this::generateBattle,
                EventType.REGENERATION, this::generateRegenerationEvent);;
    }

    @Override
    public void processCharacterEvents(GameCharacter gameCharacter) {
        Optional.ofNullable(gameCharacter).ifPresent(gc -> {
            var existingCharacterEvents = Map.of(
                    EventType.ADVENTURE, gc.isInAdventure(),
                    EventType.BATTLE, gc.isFighting(),
                    EventType.REGENERATION, gc.isResting());

            var activeEvents = existingCharacterEvents.entrySet()
                    .stream()
                    .filter(Entry::getValue)
                    .map(Entry::getKey)
                    .collect(Collectors.toList());

            generateNewEvents(gc, existingCharacterEvents, activeEvents);
            progressExistingEvents(gc, activeEvents);
        });

    }

    private void progressExistingEvents(GameCharacter gc, List<EventType> activeEvents) {
        activeEvents.forEach(activeEvent -> eventProcessors.get(activeEvent).accept(gc));
    }

    private void generateNewEvents(GameCharacter gc, Map<EventType, Boolean> characterEvents,
            List<EventType> activeEvents) {
        var missingEvents = characterEvents.entrySet()
                .stream()
                .filter(entry -> !entry.getValue())
                .map(Entry::getKey)
                .collect(Collectors.toList());

        missingEvents.forEach(eventType -> {
            var eventGenerationRestrictions = EVENT_GENERATION_RESTRICTION_MAP.get(eventType);
            if (eventGenerationRestrictions.containsAll(activeEvents)
                    && activeEvents.containsAll(eventGenerationRestrictions)) {
                eventGenerator.get(eventType).accept(gc);
                coreWebClient.saveGameCharacter(gc).subscribe();
            }
        });
    }
    //TODO: rewrite to generation by character stats/progress

    private void generateAdventure(GameCharacter gameCharacter) {
        var awards = new Awards(UUID.randomUUID().toString(),
                RANDOM.nextInt(gameCharacter.getProgress().getLevel() * 10),
                RANDOM.nextInt(gameCharacter.getProgress().getLevel() * 10));
        var adventure = new Adventure(UUID.randomUUID().toString(), gameCharacter.getId(),
                ADVENTURE_DESCRIPTION, awards.getId(), AdventureStatus.IN_PROGRESS, ADVENTURE_EVENTS, 0);

        gameCharacter.setInAdventure(true);

        coreWebClient.createAdventure(adventure).subscribe();
        coreWebClient.createAwards(awards).subscribe();
    }

    private void generateRegenerationEvent(GameCharacter gameCharacter) {

    }

    private void generateBattle(GameCharacter gameCharacter) {
        var monster = new Monster(UUID.randomUUID().toString(),
                gameCharacter.getStats().getHitPoints() /2,
                gameCharacter.getStats().getAttack() /2,
                MonsterType.SOLDIER);
        var awards = new Awards(UUID.randomUUID().toString(), 42, 73);
        var battleInfo = new BattleInfo(UUID.randomUUID().toString(), gameCharacter.getId(),
                monster.getId(), awards.getId());

        gameCharacter.setFighting(true);

        coreWebClient.createMonster(monster).subscribe(m -> {
            coreWebClient.createBattleInfo(battleInfo).subscribe(bI -> {
                coreWebClient.createAwards(awards).subscribe();
            });
        });
    }
}
