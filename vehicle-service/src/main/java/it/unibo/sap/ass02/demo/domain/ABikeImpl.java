package it.unibo.sap.ass02.demo.domain;

import it.unibo.sap.ass02.demo.domain.utils.Distance;
import it.unibo.sap.ass02.demo.domain.utils.Station;
import it.unibo.sap.ass02.demo.domain.utils.World;

import java.util.*;

public class ABikeImpl implements Bike<String> {
    private P2d position;
    private int batteryLevel;
    private EBikeState state;
    private double speed;
    private final String id = UUID.randomUUID().toString();

    public ABikeImpl(final P2d position, final int batteryLevel) {
        this.position = position;
        this.batteryLevel = batteryLevel;
        this.state = EBikeState.AVAILABLE;
        this.speed = 0;
    }

    private List<Station> getStations() {
        for (int radius = 0; radius <= 100; radius += 5) {
            final List<Station> stations = World.getStations(this.position, radius);
            if (!stations.isEmpty()) {
                return stations;
            }
        }
        return Collections.emptyList();
    }

    public void reachStation() {
        System.out.println("Automatically going to the closest Station near me.");
        final List<Station > stations = this.getStations();
        final Integer minDistance = Integer.MAX_VALUE;
        final P2d bestPosition = stations.stream()
                .map(Station::position)
                .min((p1, p2) ->
                        Distance.manatthanDistance(this.position, p1)
                                .compareTo(Distance.manatthanDistance(this.position, p2)))
                .orElse(this.position);
        this.updateLocation(bestPosition);
    }

    public void reachUser(final P2d userPosition) {
        System.out.println("A user asked for me, I am going towards him.");
        this.updateLocation(userPosition);
    }


    @Override
    public EBikeState getState() {
        return this.state;
    }

    @Override
    public void rechargeBattery() {
        // Do something
    }

    @Override
    public int getBatteryLevel() {
        return this.batteryLevel;
    }

    @Override
    public void decreaseBatteryLevel(int delta) {
        this.batteryLevel -= delta;
    }

    @Override
    public boolean isAvailable() {
        return this.state.equals(EBikeState.AVAILABLE);
    }

    @Override
    public void updateState(EBikeState state) {
        this.state = state;
    }

    @Override
    public void updateLocation(P2d newLoc) {
        System.out.println("I am moving towards a new Position.");
        this.position = newLoc;
    }

    @Override
    public void updateSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void updateDirection(V2d dir) {

    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

    @Override
    public V2d getDirection() {
        return null;
    }

    @Override
    public P2d getLocation() {
        return this.position;
    }

    @Override
    public String getID() {
        return this.id;
    }
}
