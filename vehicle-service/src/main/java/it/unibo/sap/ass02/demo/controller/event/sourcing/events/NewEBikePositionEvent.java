package it.unibo.sap.ass02.demo.controller.event.sourcing.events;

import it.unibo.sap.ass02.demo.domain.P2d;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewEBikePositionEvent extends Event{
    private String ebikeId;
    private P2d newPosition;
}
