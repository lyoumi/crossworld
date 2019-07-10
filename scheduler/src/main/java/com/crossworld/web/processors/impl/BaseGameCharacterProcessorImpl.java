package com.crossworld.web.processors.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.AdventureEventDetails;
import com.crossworld.web.data.BattleEventDetails;
import com.crossworld.web.data.EventDetails;
import com.crossworld.web.data.EventStatus;
import com.crossworld.web.data.EventType;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.data.HealingEventDetails;
import com.crossworld.web.data.battle.Monster;
import com.crossworld.web.processors.BaseGameCharacterProcessor;
import com.crossworld.web.processors.EventProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
public class BaseGameCharacterProcessorImpl implements BaseGameCharacterProcessor {

    private static final Random RANDOM = new Random();
    private static final Map<EventType, Function<GameCharacter, EventDetails>> EVENT_DETAILS_GENERATORS =
            Map.of(EventType.ADVENTURE, BaseGameCharacterProcessorImpl::generateAdventureEventDetails,
                    EventType.BATTLE, BaseGameCharacterProcessorImpl::generateBattleEventDetails,
                    EventType.HEALING, BaseGameCharacterProcessorImpl::generateHealingEventDetails);

    private final Map<EventType, BiConsumer<GameCharacter, GameEvent>> eventProcessors;

    private final CoreWebClient coreWebClient;

    public BaseGameCharacterProcessorImpl(CoreWebClient coreWebClient,
            EventProcessor battleEventProcessor,
            EventProcessor adventureEventProcessor,
            EventProcessor healingEventProcessor) {
        this.coreWebClient = coreWebClient;
        eventProcessors = Map.of(
                EventType.ADVENTURE, adventureEventProcessor::processEvent,
                EventType.BATTLE, battleEventProcessor::processEvent,
                EventType.HEALING, healingEventProcessor::processEvent
        );
    }

    @Override
    public void processCharacterEvents(GameCharacter gameCharacter) {
        Optional.ofNullable(gameCharacter).ifPresent(gc -> {
            if (!gc.isHasEvent()) {
                createCharacterEvent(gc);
            }
            progressCharacterEvent(gc);
        });

    }

    private void createCharacterEvent(GameCharacter gameCharacter) {
        GameEvent gameEvent = new GameEvent(UUID.randomUUID().toString(),
                gameCharacter.getId(),
                "",
                EventStatus.IN_PROGRESS,
                generateRandomEventDetails(gameCharacter));
        gameCharacter.setHasEvent(true);
        coreWebClient.saveGameCharacter(gameCharacter).subscribe();
        coreWebClient.saveGameEvent(gameEvent).subscribe();
    }

    private void progressCharacterEvent(GameCharacter gameCharacter) {
        coreWebClient.getGameEventByCharacterId(gameCharacter.getId())
                .log()
                .subscribe(
                        gameEvent -> eventProcessors
                                .get(gameEvent.getEventDetails().getEventType())
                                .accept(gameCharacter, gameEvent));
    }

    private EventDetails generateRandomEventDetails(GameCharacter gameCharacter) {
        EventType eventType = List.of(EventType.values()).get(RANDOM.nextInt(EventType.values().length - 1));
        EventDetails eventDetails = EVENT_DETAILS_GENERATORS.get(eventType).apply(gameCharacter);
        eventDetails.setEventType(eventType);
        return eventDetails;
    }

    //TODO: rewrite to generation by character stats/progress
    private static EventDetails generateAdventureEventDetails(GameCharacter gameCharacter) {
        AdventureEventDetails eventDetails;
        eventDetails = new AdventureEventDetails();
        eventDetails.setAdventureEvents(List.of("Some event"));
        eventDetails.setGold(2000);
        eventDetails.setExperience(2000);
        return eventDetails;
    }

    private static EventDetails generateHealingEventDetails(GameCharacter gameCharacter) {
        EventDetails eventDetails;
        eventDetails = new HealingEventDetails();
        eventDetails.setExperience(42);
        return eventDetails;
    }

    private static EventDetails generateBattleEventDetails(GameCharacter gameCharacter) {
        BattleEventDetails eventDetails;
        eventDetails = new BattleEventDetails();
        eventDetails.setMonster(new Monster(42, 24));
        eventDetails.setGold(100);
        eventDetails.setExperience(300);
        return eventDetails;
    }
}
