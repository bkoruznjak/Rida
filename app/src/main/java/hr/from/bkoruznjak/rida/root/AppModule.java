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
    private RidaApp mApplication;
    private SharedPreferences mSharedPreferences;

    public AppModule(RidaApp context) {
        this.mApplication = context;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @AppScope
    public RidaApp providesApplication() {
        return this.mApplication;
    }

    @Provides
    @AppScope
    public SharedPreferences providesSharedPreferences() {
        return this.mSharedPreferences;
    }
}