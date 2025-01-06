package it.unibo.sap.ass02.demo;

import it.unibo.sap.ass02.demo.domain.ABikeImpl;
import it.unibo.sap.ass02.demo.domain.P2d;

public class DemoABike {
    public static void main(String[] args) {
        final ABikeImpl abike = new ABikeImpl(new P2d(0, 0), 0);
        abike.reachStation();
        abike.reachUser(new P2d(100, 100));
    }
}
