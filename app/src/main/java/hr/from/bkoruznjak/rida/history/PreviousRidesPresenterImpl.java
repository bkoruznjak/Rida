package hr.from.bkoruznjak.rida.history;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import hr.from.bkoruznjak.rida.current.model.Ride;
import hr.from.bkoruznjak.rida.history.contract.PreviousRidePresenter;
import hr.from.bkoruznjak.rida.history.contract.PreviousRideView;
import hr.from.bkoruznjak.rida.root.AppComponent;
import hr.from.bkoruznjak.rida.root.database.RidaDatabase;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class PreviousRidesPresenterImpl implements PreviousRidePresenter, LifecycleOwner {

    @Inject
    RidaDatabase database;
    private PreviousRideView mPreviousRideView;
    private LifecycleRegistry mLifecycleRegistry;

    public PreviousRidesPresenterImpl(@NonNull PreviousRideView previousRideView, @NonNull AppComponent appComponent) {
        this.mPreviousRideView = previousRideView;
        appComponent.inject(this);
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.INITIALIZED);
    }

    @Override
    public void onResume() {
        mLifecycleRegistry.markState(Lifecycle.State.RESUMED);
        loadPreviousRides();
    }

    @Override
    public void onDestroy() {
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        mLifecycleRegistry = null;
        database = null;
        mPreviousRideView = null;
    }

    @Override
    public void loadPreviousRides() {
        LiveData<List<Ride>> rideData = database.rideDao().loadAllRides();
        rideData.observe(this, (@Nullable final List<Ride> rides) -> {
            if (rides == null) {
                mPreviousRideView.showNoRidesMessage();
                return;
            }

            if (rides.isEmpty()) {
                mPreviousRideView.showNoRidesMessage();
                return;
            }

            mPreviousRideView.hideNoRideMessage();
            for (Ride existingRide : rides) {
                mPreviousRideView.addRide(existingRide);
            }
        });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
