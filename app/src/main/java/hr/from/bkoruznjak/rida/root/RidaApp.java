package hr.from.bkoruznjak.rida.root;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import hr.from.bkoruznjak.rida.BuildConfig;
import hr.from.bkoruznjak.rida.current.CurrentRideModule;
import hr.from.bkoruznjak.rida.root.database.DatabaseModule;
import hr.from.bkoruznjak.rida.root.location.LocationModule;
import hr.from.bkoruznjak.rida.root.network.NetworkModule;
import hr.from.bkoruznjak.rida.root.network.RidaWebAPI;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class RidaApp extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        //USE ONLY FOR DEBUGGING SINCE IT HOGS MEMORY LIKE ŽAK HOUDEK
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(RidaWebAPI.BASE_URL))
                .databaseModule(new DatabaseModule())
                .locationModule(new LocationModule())
                .currentRideModule(new CurrentRideModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return this.mAppComponent;
    }
}
