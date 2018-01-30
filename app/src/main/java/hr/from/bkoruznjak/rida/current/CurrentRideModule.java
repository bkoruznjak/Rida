package hr.from.bkoruznjak.rida.current;

import dagger.Module;
import dagger.Provides;
import hr.from.bkoruznjak.rida.root.AppScope;
import hr.from.bkoruznjak.rida.root.database.RidaDatabase;


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

    @AppScope
    @Provides
    public CurrentRideRoomRepository provideCurrentRideRoomRepository(RidaDatabase database) {
        return new CurrentRideRoomRepository(database);
    }

}
