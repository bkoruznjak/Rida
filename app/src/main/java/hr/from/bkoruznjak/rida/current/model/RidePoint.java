package hr.from.bkoruznjak.rida.current.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bkoruznjak on 29/01/2018.
 */

@Entity(foreignKeys = {
        @ForeignKey(entity = Ride.class,
                parentColumns = "RIDE_ID",
                childColumns = "RIDE_ID")})
public class RidePoint {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_ID")
    public long id;
    @SerializedName("bookingId")
    @Expose
    @ColumnInfo(name = "RIDE_ID")
    public long rideId;
    @SerializedName("lat")
    @Expose
    @ColumnInfo(name = "LATITUDE")
    public String latitude;
    @SerializedName("lng")
    @Expose
    @ColumnInfo(name = "LONGITUDE")
    public String longitude;
    @ColumnInfo(name = "IS_UPLOADED")
    public int isUploaded;
    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "STATUS")
    public String status;
    @SerializedName("time")
    @Expose
    @ColumnInfo(name = "TIME")
    public String time;

    @Override
    public String toString() {
        return "RidePoint{" +
                "id=" + id +
                ", rideId=" + rideId +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", isUploaded=" + isUploaded +
                ", status='" + status + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}