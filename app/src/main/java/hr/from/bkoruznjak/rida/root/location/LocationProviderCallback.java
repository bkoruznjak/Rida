package hr.from.bkoruznjak.rida.root.location;

import android.location.Location;

/**
 * Created by bkoruznjak on 28/01/2018.
 */

public interface LocationProviderCallback {

    int ERROR_LACKING_PERMISSION = 1;
    int ERROR_LOCATION_FETCH_FAILED = 2;

    void onLocationChanged(Location location);

    void onLocationProviderError(int errorCode);
}