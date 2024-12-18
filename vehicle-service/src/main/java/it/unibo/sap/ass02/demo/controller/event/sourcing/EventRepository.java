package it.unibo.sap.ass02.demo.controller.event.sourcing;

import it.unibo.sap.ass02.demo.controller.event.sourcing.events.*;
import it.unibo.sap.ass02.demo.domain.P2d;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventRepository {
    final Map<String, Queue<Event>> repository;
    public EventRepository() {
        this.repository = new HashMap<>();
    }

    public void createNewEBike(final String id, final P2d newPosition) {
        final Queue<Event> events = new PriorityQueue<>((e1, e2) -> e2.getEventDate().compareTo(e1.getEventDate()));
        events.add(new NewEBikePositionEvent(id, newPosition));
        this.repository.put(id, events);
    }

    public void updateEBikePosition(final String id, final P2d newPosition) {
        if (this.repository.containsKey(id)) {
            System.out.println("UPDATE POSITION");
            this.repository.get(id).add(new NewEBikePositionEvent(id, newPosition));
        }
    }

    public Optional<P2d> getLatestPosition(final String id) {
        try {
            if (this.repository.containsKey(id)) {
                return Optional.ofNullable(((NewEBikePositionEvent) this.repository.get(id).element()).getNewPosition());
            }
        }catch (NoSuchElementException ex) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}
