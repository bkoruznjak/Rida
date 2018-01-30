package hr.from.bkoruznjak.rida.history.contract;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.root.contract.RootView;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface RideDetailView extends RootView {

    @MainThread
    void addRidePoint(@NonNull RidePoint ridePoint);

    @MainThread
    void showNoRidePointsMessage();

    @MainThread
    void hideNoRidePointsMessage();

    @MainThread
    void setBookingId(@NonNull String bookingId);

    @MainThread
    void setStartLocation(@NonNull String startLocation);

    @MainThread
    void setEndLocation(@NonNull String endLocation);

    @NonNull
    Bundle getBundle();
}