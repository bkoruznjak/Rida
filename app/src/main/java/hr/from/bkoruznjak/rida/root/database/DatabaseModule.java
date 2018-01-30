package hr.from.bkoruznjak.rida.root.database;

import dagger.Module;
import dagger.Provides;
import hr.from.bkoruznjak.rida.root.AppScope;
import hr.from.bkoruznjak.rida.root.RidaApp;

/**
 * Created by bkoruznjak on 29/01/2018.
 */

@Module
public class DatabaseModule {

    public DatabaseModule() {
    }

    @Provides
    @AppScope
    public RidaDatabase provideDatabase(RidaApp context) {
        return RidaDatabase.getDatabase(context);
    }
}
