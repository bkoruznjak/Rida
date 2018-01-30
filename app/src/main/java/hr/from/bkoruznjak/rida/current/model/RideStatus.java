package hr.from.bkoruznjak.rida.current.model;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public enum RideStatus {
    IDLE("idle"),
    STARTED("started"),
    PASSENGER_PICKED_UP("pickedUp"),
    STOP_OVER("stopOver");

    private final String NAME;

    RideStatus(String name) {
        NAME = name;
    }

    public boolean eqaulsName(String otherName) {
        return NAME.equals(otherName);
    }

    public String toString() {
        return this.NAME;
    }
}