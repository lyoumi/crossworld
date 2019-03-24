package com.crossworld.web.listners;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.AdventureEventDetails;
import com.crossworld.web.data.BattleEventDetails;
import com.crossworld.web.data.EventDetails;
import com.crossworld.web.data.EventStatus;
import com.crossworld.web.data.EventType;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.data.Monster;
import com.crossworld.web.processors.EventProcessor;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
public class AsyncGameCharacterProcessor {

    private static final Random RANDOM = new Random();
    private static final Map<EventType, Function<GameCharacter, EventDetails>> EVENT_DETAILS_GENERATORS =
            Map.of(EventType.ADVENTURE, AsyncGameCharacterProcessor::generateAdventureEventDetails,
                    EventType.BATTLE, AsyncGameCharacterProcessor::generateBattleEventDetails,
                    EventType.HEALING, AsyncGameCharacterProcessor::generateHealingEventDetails);

    private final Map<EventType, BiConsumer<GameCharacter, GameEvent>> eventProcessors;

    private final CoreWebClient coreWebClient;

    public AsyncGameCharacterProcessor(CoreWebClient coreWebClient,
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

    @Async
    @EventListener
    public void processCharacterEvents(GameCharacter gameCharacter) {
        if (!gameCharacter.isHasEvent()) {
            createCharacterEvent(gameCharacter);
        }
        progressCharacterEvent(gameCharacter);
    }

    private void createCharacterEvent(GameCharacter gameCharacter) {
        GameEvent gameEvent = new GameEvent(UUID.randomUUID().toString(),
                gameCharacter.getId(),
                "",
                EventStatus.IN_PROGRESS,
                generateRandomEventDetails(gameCharacter));
        coreWebClient.saveGameEvent(gameEvent).log();
    }

    private void progressCharacterEvent(GameCharacter gameCharacter) {
        coreWebClient.getGameEventByCharacterId(gameCharacter.getId())
                .log()
                .doOnSuccess(gameEvent -> eventProcessors
                        .get(gameEvent.getEventDetails().getEventType())
                        .accept(gameCharacter, gameEvent));
    }

    private EventDetails generateRandomEventDetails(GameCharacter gameCharacter) {
        EventType eventType = List.of(EventType.values()).get(RANDOM.nextInt(EventType.values().length - 1));
        return EVENT_DETAILS_GENERATORS.get(eventType).apply(gameCharacter);
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
        eventDetails = new EventDetails();
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
