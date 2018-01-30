package hr.from.bkoruznjak.rida.current.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import hr.from.bkoruznjak.rida.current.model.Ride;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by bkoruznjak on 29/01/2018.
 */

@Dao
public interface RideDao {
    @Query("select * from Ride")
    LiveData<List<Ride>> loadAllRides();

    @Query("select * from Ride where RIDE_ID = :id")
    LiveData<Ride> loadRideById(long id);

    @Query("select * from Ride where RIDE_ID = :id")
    Ride getRideById(long id);

    @Insert(onConflict = REPLACE)
    long insertRide(Ride Ride);

    @Insert(onConflict = REPLACE)
    void insertOrReplaceRides(Ride... Rides);

    @Insert(onConflict = REPLACE)
    void insertOrReplaceRides(List<Ride> Rides);

    @Update
    void updateRide(Ride ride);

    @Delete
    void deleteRide(Ride Ride);

    @Query("DELETE FROM Ride")
    void deleteAll();
}
