package hr.from.bkoruznjak.rida.root.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import hr.from.bkoruznjak.rida.current.dao.RideDao;
import hr.from.bkoruznjak.rida.current.dao.RidePointDao;
import hr.from.bkoruznjak.rida.current.model.Ride;
import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.root.Constants;


/**
 * Created by bkoruznjak on 29/01/2018.
 */

@Database(entities = {
        Ride.class,
        RidePoint.class
}, version = 1)
public abstract class RideDatabase extends RoomDatabase {

    private static RideDatabase INSTANCE;

    public static RideDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, RideDatabase.class, Constants.DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    //LOGIN & USER
    public abstract RideDao rideDao();

    public abstract RidePointDao ridePointDao();
}
