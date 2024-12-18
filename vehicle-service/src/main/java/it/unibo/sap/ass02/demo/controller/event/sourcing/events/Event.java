package it.unibo.sap.ass02.demo.controller.event.sourcing.events;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public abstract class Event {
    public final UUID id = UUID.randomUUID();
    public final Date eventDate = new Date();
}
