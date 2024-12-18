package it.unibo.sap.ass02.demo.controller.event.sourcing;

import java.util.Date;
import java.util.UUID;

public class Event {
    public final UUID id = UUID.randomUUID();
    public final Date eventDate = new Date();
}
