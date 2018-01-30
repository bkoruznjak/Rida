package hr.from.bkoruznjak.rida.current.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by bkoruznjak on 29/01/2018.
 */

@Entity
public class Ride {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "RIDE_ID")
    public long id;
    @ColumnInfo(name = "START_LOCATION")
    public String startLocation;
    @ColumnInfo(name = "END_LOCATION")
    public String endLocation;
    @ColumnInfo(name = "NUMBER_OF_PASSENGERS")
    public int numberOfPassengers;

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", startLocation='" + startLocation + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", numberOfPassengers=" + numberOfPassengers +
                '}';
    }
}
