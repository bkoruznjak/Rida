package hr.from.bkoruznjak.rida.current.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import hr.from.bkoruznjak.rida.current.model.RidePoint;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by bkoruznjak on 29/01/2018.
 */

@Dao
public interface RidePointDao {
    @Query("select * from RidePoint")
    LiveData<List<RidePoint>> loadAllRidePoints();

    @Query("select * from RidePoint where RIDE_ID = :rideId")
    LiveData<List<RidePoint>> loadRidePointForRide(long rideId);

    @Query("select * from RidePoint where IS_UPLOADED = 0")
    LiveData<List<RidePoint>> loadAllRidePointsPendingUpload();

    @Query("select * from RidePoint where IS_UPLOADED = 0")
    List<RidePoint> getAllRidePointsPendingUpload();

    @Insert(onConflict = REPLACE)
    long insertRidePoint(RidePoint RidePoint);

    @Insert(onConflict = REPLACE)
    void insertOrReplaceRidePoints(RidePoint... RidePoints);

    @Insert(onConflict = REPLACE)
    void insertOrReplaceRidePoints(List<RidePoint> RidePoints);

    @Update
    void updateRidePoint(RidePoint RidePoint);

    @Delete
    void deleteRidePoint(RidePoint RidePoint);

    @Query("DELETE FROM RidePoint")
    void deleteAll();
}
