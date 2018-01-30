package hr.from.bkoruznjak.rida.current;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import hr.from.bkoruznjak.rida.current.contract.CurrentRideRepository;
import hr.from.bkoruznjak.rida.current.model.Ride;
import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.root.database.RidaDatabase;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class CurrentRideRoomRepository implements CurrentRideRepository {

    private RidaDatabase mDatabase;

    public CurrentRideRoomRepository(RidaDatabase database) {
        mDatabase = database;
    }

    @Override
    public void saveRide(@NonNull Ride currentRide, @Nullable OnDataSavedListener callback) {
        new Thread(() -> {
            long rideId = mDatabase.rideDao().insertRide(currentRide);
            currentRide.id = rideId;
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
        new Thread(() -> mDatabase.ridePointDao().insertRidePoint(ridePoint)).start();
    }

    /**
     * CALL OF THE UI THREAD
     *
     * @param ridePoint
     * @return ridePointId
     */
    @Override
    public long saveRidePointSync(@NonNull RidePoint ridePoint) {
        return mDatabase.ridePointDao().insertRidePoint(ridePoint);
    }

    @Override
    public void updateRidePoint(@NonNull RidePoint ridePoint) {
        new Thread(() -> mDatabase.ridePointDao().updateRidePoint(ridePoint)).start();
    }

    /**
     * CALL OF THE UI THREAD
     *
     * @param ridePoint
     */
    @Override
    public void updateRidePointSync(@NonNull RidePoint ridePoint) {
        mDatabase.ridePointDao().updateRidePoint(ridePoint);
    }
}