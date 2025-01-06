package it.unibo.sap.ass02.demo.domain.utils;

import it.unibo.sap.ass02.demo.domain.P2d;

public class Distance {
    private Distance() {}
    public static Double manatthanDistance(final P2d pos1, final P2d pos2) {
        return Math.abs(pos1.x() - pos2.x()) + Math.abs(pos1.y() - pos2.y());
    }
}
