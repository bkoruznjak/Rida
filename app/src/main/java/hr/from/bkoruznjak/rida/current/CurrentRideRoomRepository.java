package hr.from.bkoruznjak.rida.current;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import hr.from.bkoruznjak.rida.current.contract.CurrentRideRepository;
import hr.from.bkoruznjak.rida.current.model.Ride;
import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.root.database.RideDatabase;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class CurrentRideRoomRepository implements CurrentRideRepository {

    private RideDatabase mDatabase;

    public CurrentRideRoomRepository(RideDatabase database) {
        mDatabase = database;
    }

    @Override
    public void saveRide(@NonNull Ride currentRide, @Nullable OnDataSavedListener callback) {
        new Thread(() -> {
            long rideId = mDatabase.rideDao().insertRide(currentRide);
            currentRide.id = rideId;
            Log.d("žžž", "ride saved:" + currentRide.toString());
            if (callback != null) {
                callback.onSuccess(rideId);
            }
        }).start();
    }

    @Override
    public void updateRide(@NonNull Ride currentRide) {
        new Thread(() -> mDatabase.rideDao().updateRide(currentRide)).start();
    }

    @Override
    public void saveRidePoint(@NonNull RidePoint ridePoint) {
        new Thread(() -> {
            long ridePointId = mDatabase.ridePointDao().insertRidePoint(ridePoint);
            ridePoint.id = ridePointId;
            Log.d("žžž", "ridepoint saved:" + ridePoint.toString());
        }).start();
    }

    @Override
    public void updateRidePoint(@NonNull RidePoint ridePoint) {
        new Thread(() -> mDatabase.ridePointDao().updateRidePoint(ridePoint)).start();
    }
}