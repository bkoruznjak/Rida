package hr.from.bkoruznjak.rida.root.location;

/**
 * Created by bkoruznjak on 28/01/2018.
 */

import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationListener;

import java.lang.ref.WeakReference;

public class WeakLocationListener implements LocationListener {

    private final WeakReference<LocationListener> locationListenerRef;

    public WeakLocationListener(@NonNull LocationListener locationListener) {
        locationListenerRef = new WeakReference<>(locationListener);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        if (locationListenerRef.get() == null) {
            return;
        }
        locationListenerRef.get().onLocationChanged(location);
    }
}