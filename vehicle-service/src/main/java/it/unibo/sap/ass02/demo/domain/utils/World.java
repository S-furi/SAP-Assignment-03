package it.unibo.sap.ass02.demo.domain.utils;

import it.unibo.sap.ass02.demo.domain.P2d;

import java.util.Collections;
import java.util.List;

public class World {
    private World() {}
    public static List<Station> getStations(final P2d position, final int radius) {
        System.out.println("Querying the World, accessing all the stations near the input position in the given radius");
        return Collections.emptyList();
    }

    public static List<Station> getAllStations() {
        System.out.println("Querying the World and asking for all the different stations");
        return Collections.emptyList();
    }
}
