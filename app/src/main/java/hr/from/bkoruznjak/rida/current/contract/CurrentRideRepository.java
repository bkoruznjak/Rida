package hr.from.bkoruznjak.rida.current.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import hr.from.bkoruznjak.rida.current.model.Ride;
import hr.from.bkoruznjak.rida.current.model.RidePoint;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface CurrentRideRepository {

    void saveRide(@NonNull Ride currentRide, @Nullable OnDataSavedListener callback);

    void updateRide(@NonNull Ride currentRide);

    void saveRidePoint(@NonNull RidePoint ridePoint);

    void updateRidePoint(@NonNull RidePoint ridePoint);

    interface OnDataSavedListener {
        void onSuccess(long id);

        void onError(@NonNull Throwable error);
    }
}
