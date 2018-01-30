package hr.from.bkoruznjak.rida.root.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import hr.from.bkoruznjak.rida.root.RidaApp;


/**
 * Created by bkoruznjak on 28/01/2018.
 */

public class LocationProvider implements LocationListener, GoogleApiClient.ConnectionCallbacks {

    private final long REFRESH_LOCATION_INTERVAL_TIME_IN_MILLIS;
    private final String LOG_TAG = "LOC_PROV";

    private WeakLocationListener mWeakLocationListener;
    private GoogleApiClient mGoogleApiClient;
    private List<LocationProviderCallback> mCallbackList;
    private LocationManager mLocationManager;

    public LocationProvider(RidaApp context, long gpsRefreshInterval) {
        this.REFRESH_LOCATION_INTERVAL_TIME_IN_MILLIS = gpsRefreshInterval;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        mWeakLocationListener = new WeakLocationListener(this);
        mCallbackList = new ArrayList<>(4);
    }

    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    public boolean isGPSenabledOnDevice() {
        boolean isGpsEnabled = false;

        try {
            isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }
        return isGpsEnabled;
    }

    public void connect(@Nullable LocationProviderCallback callback) {
        if (callback != null) {
            mCallbackList.add(callback);
        }
        connect();
    }

    public void connect() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(this);
    }

    public void disconnect() {
        if (isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mWeakLocationListener);
        }
        mGoogleApiClient.disconnect();
        mGoogleApiClient.unregisterConnectionCallbacks(this);
    }

    public void addCallback(@Nullable LocationProviderCallback callback) {
        if (callback != null) {
            mCallbackList.add(callback);
        }
    }

    public void removeCallback(@Nullable LocationProviderCallback callback) {
        if (callback != null) {
            mCallbackList.remove(callback);
        }
    }

    public void removeAllCallbacks() {
        mCallbackList.clear();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        prepareLocationServices();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        for (LocationProviderCallback callback : mCallbackList) {
            callback.onLocationChanged(location);
            location.getSpeed();
        }
    }

    /**
     * We presume that we already have location permissions and need not check them here
     */
    private void prepareLocationServices() {

        try {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(REFRESH_LOCATION_INTERVAL_TIME_IN_MILLIS);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, mWeakLocationListener);
        } catch (SecurityException securityEx) {
            Log.e(LOG_TAG, securityEx.getMessage());
            for (LocationProviderCallback callback : mCallbackList) {
                callback.onLocationProviderError(LocationProviderCallback.ERROR_LACKING_PERMISSION);
            }
        } catch (IllegalStateException illegalStateEx) {
            Log.e(LOG_TAG, illegalStateEx.getMessage());
            for (LocationProviderCallback callback : mCallbackList) {
                callback.onLocationProviderError(LocationProviderCallback.ERROR_LOCATION_FETCH_FAILED);
            }
        }
    }
}