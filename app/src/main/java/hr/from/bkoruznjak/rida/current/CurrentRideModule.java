package hr.from.bkoruznjak.rida.current;

import dagger.Module;
import dagger.Provides;
import hr.from.bkoruznjak.rida.root.AppScope;


/**
 * Created by bkoruznjak on 29/01/2018.
 */

@Module
public class CurrentRideModule {

    @AppScope
    @Provides
    public CurrentRideSession provideCurrentRideSession() {
        return new CurrentRideSession();
    }

}
