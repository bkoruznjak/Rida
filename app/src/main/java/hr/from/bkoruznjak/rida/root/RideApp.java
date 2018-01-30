package hr.from.bkoruznjak.rida.root;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import hr.from.bkoruznjak.rida.BuildConfig;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class RideApp extends Application {

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
                .build();
    }

    public AppComponent getAppComponent() {
        return this.mAppComponent;
    }
}
