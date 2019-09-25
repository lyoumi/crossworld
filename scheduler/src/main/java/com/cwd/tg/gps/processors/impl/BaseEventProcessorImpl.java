package com.cwd.tg.gps.processors.impl;

import com.cwd.tg.gps.client.CoreWebClient;
import com.cwd.tg.gps.data.character.GameCharacter;
import com.cwd.tg.gps.data.events.EventType;
import com.cwd.tg.gps.data.events.awards.Awards;
import com.cwd.tg.gps.data.events.battle.BattleInfo;
import com.cwd.tg.gps.data.events.battle.Monster;
import com.cwd.tg.gps.data.events.battle.MonsterType;
import com.cwd.tg.gps.processors.BaseEventProcessor;
import com.cwd.tg.gps.processors.EventProcessor;
import com.cwd.tg.gps.processors.util.EventGeneratorUtils;

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
public class BaseEventProcessorImpl implements BaseEventProcessor {

    private static final Random RANDOM = new Random();

    private static final Map<EventType, List<EventType>> EVENT_GENERATION_RESTRICTION_MAP =
            Map.of(EventType.ADVENTURE, Collections.emptyList(),
                    EventType.BATTLE, List.of(EventType.ADVENTURE),
                    EventType.REGENERATION, List.of(EventType.ADVENTURE));
    private final Map<EventType, Consumer<GameCharacter>> eventGenerator;

    private final Map<EventType, Consumer<GameCharacter>> eventProcessors;

    private final CoreWebClient coreWebClient;
    private final EventGeneratorUtils eventGeneratorUtils;

    public BaseEventProcessorImpl(CoreWebClient coreWebClient,
            EventProcessor battleEventProcessor,
            EventProcessor adventureEventProcessor,
            EventProcessor healingEventProcessor,
            EventGeneratorUtils eventGeneratorUtils) {
        eventProcessors = Map.of(
                EventType.ADVENTURE, adventureEventProcessor::processEvent,
                EventType.BATTLE, battleEventProcessor::processEvent,
                EventType.REGENERATION, healingEventProcessor::processEvent
        );
        eventGenerator = Map.of(EventType.ADVENTURE, this::generateAdventure,
                EventType.BATTLE, this::generateBattle,
                EventType.REGENERATION, this::generateRegenerationEvent);

        this.coreWebClient = coreWebClient;
        this.eventGeneratorUtils = eventGeneratorUtils;
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

            if (!gc.hasEvent()) {
                generateNewEvents(gc, existingCharacterEvents, activeEvents);
            }
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
                    && activeEvents.containsAll(eventGenerationRestrictions) && RANDOM.nextBoolean()) {
                eventGenerator.get(eventType).accept(gc);
                coreWebClient.saveGameCharacter(gc).subscribe();
            }
        });
    }
    //TODO: rewrite to generation by character stats/progress

    private void generateAdventure(GameCharacter gameCharacter) {
        var awards = new Awards(UUID.randomUUID().toString(),
                RANDOM.nextInt(gameCharacter.getProgress().getLevel() * 10)
                        + gameCharacter.getProgress().getTargetExp() / 4,
                RANDOM.nextInt(gameCharacter.getProgress().getLevel() * 10));
        eventGeneratorUtils.generateAdventure(gameCharacter.getId(), awards.getId())
                .doOnSuccess(adventure -> gameCharacter.setInAdventure(true))
                .doOnSuccess(adventure -> coreWebClient.createAdventure(adventure).subscribe())
                .subscribe(adventure -> coreWebClient.createAwards(awards).subscribe());
    }

    private void generateRegenerationEvent(GameCharacter gameCharacter) {
        //TODO: implement regeneration generation
    }

    private void generateBattle(GameCharacter gameCharacter) {
        var monster = new Monster(UUID.randomUUID().toString(),
                gameCharacter.getStats().getHitPoints() / 2,
                gameCharacter.getStats().getAttack() / 2,
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
