package hr.from.bkoruznjak.rida.history.contract;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import hr.from.bkoruznjak.rida.current.model.Ride;
import hr.from.bkoruznjak.rida.root.contract.RootView;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface PreviousRideView extends RootView {

    @MainThread
    void addRide(@NonNull Ride ride);

    @MainThread
    void showNoRidesMessage();

    @MainThread
    void hideNoRideMessage();

    void goToRideDetails(long rideId);
}