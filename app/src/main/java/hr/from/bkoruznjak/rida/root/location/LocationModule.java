package hr.from.bkoruznjak.rida.root.location;

import dagger.Module;
import dagger.Provides;
import hr.from.bkoruznjak.rida.root.AppScope;
import hr.from.bkoruznjak.rida.root.RidaApp;


/**
 * Created by bkoruznjak on 28/01/2018.
 */


@Module
public class LocationModule {


    public LocationModule() {
    }

    @AppScope
    @Provides
    LocationProvider providesLocationProvider(RidaApp context) {
        return new LocationProvider(context, 5000l); //5 seconds as per spec
    }

}