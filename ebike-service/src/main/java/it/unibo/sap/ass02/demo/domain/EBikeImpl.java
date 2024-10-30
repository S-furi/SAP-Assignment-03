package it.unibo.sap.ass02.demo.domain;

public class EBikeImpl implements EBike<String>{
    private final String id;
    private EBikeState state;
    private P2d loc;
    private V2d direction;
    private double speed;
    private int batteryLevel;

    public EBikeImpl(String id) {
        this(id, EBikeState.AVAILABLE, new P2d(0, 0), 0, 100);
    }
    @Override
    public String getID() {
        return null;
    }

    @Override
    public EBikeState getState() {
        return null;
    }

    @Override
    public void rechargeBattery() {

    }

    @Override
    public int getBatteryLevel() {
        return 0;
    }

    @Override
    public void decreaseBatteryLevel(int delta) {

    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void updateState(EBikeState state) {

    }

    @Override
    public void updateLocation(P2d newLoc) {

    }

    @Override
    public void updateSpeed(double speed) {

    }

    @Override
    public void updateDirection(V2d dir) {

    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public V2d getDirection() {
        return null;
    }

    @Override
    public P2d getLocation() {
        return null;
    }
}
