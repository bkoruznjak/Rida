package hr.from.bkoruznjak.rida.root;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

@Module
public class AppModule {
    private RideApp mApplication;
    private SharedPreferences mSharedPreferences;

    public AppModule(RideApp context) {
        this.mApplication = context;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @AppScope
    public RideApp providesApplication() {
        return this.mApplication;
    }

    @Provides
    @AppScope
    public SharedPreferences providesSharedPreferences() {
        return this.mSharedPreferences;
    }
}