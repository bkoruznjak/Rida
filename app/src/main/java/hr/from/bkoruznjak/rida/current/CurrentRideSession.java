package hr.from.bkoruznjak.rida.current;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import hr.from.bkoruznjak.rida.current.model.RideStatus;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class CurrentRideSession {

    public static final long NO_DATABASE_ID_ASSIGNED = -1;

    private long mDatabaseId;
    private String mStartLocation;
    private String mEndLocation;
    private int mNumberOfPassengers;
    private RideStatus mStatus;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public CurrentRideSession() {
        purgeSession();
    }

    public long getDatabaseId() {
        return mDatabaseId;
    }

    public void setDatabaseId(long databaseId) {
        this.mDatabaseId = databaseId;
    }

    public String getStartLocation() {
        return mStartLocation;
    }

    public void setStartLocation(String startLocation) {
        this.mStartLocation = startLocation;
    }

    public String getEndLocation() {
        return mEndLocation;
    }

    public void setEndLocation(String endLocation) {
        this.mEndLocation = endLocation;
    }

    public int getNumberOfPassengers() {
        return mNumberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.mNumberOfPassengers = numberOfPassengers;
    }

    public RideStatus getStatus() {
        return mStatus;
    }

    public void setStatus(RideStatus status) {
        this.mStatus = status;
    }

    /**
     * Method should be called when current ride is ended and we want to re-use the session for
     * future rides
     */
    public void purgeSession() {
        this.mDatabaseId = NO_DATABASE_ID_ASSIGNED;
        this.mStartLocation = "trnsko";
        this.mEndLocation = "siget";
        this.mNumberOfPassengers = 1;
        this.mStatus = RideStatus.IDLE;
    }

    /**
     * Convenience method for fetching current time
     *
     * @return time formatted as "dd.MM.yyyy hh24:mm"
     */
    @NonNull
    public String getCurrentTime() {
        return mSimpleDateFormat.format(new Date());
    }
}