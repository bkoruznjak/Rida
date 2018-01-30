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
import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.history.contract.RideDetailPresenter;
import hr.from.bkoruznjak.rida.history.contract.RideDetailView;
import hr.from.bkoruznjak.rida.root.AppComponent;
import hr.from.bkoruznjak.rida.root.Constants;
import hr.from.bkoruznjak.rida.root.database.RidaDatabase;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class PreviousRideDetailPresenter implements RideDetailPresenter, LifecycleOwner {

    @Inject
    RidaDatabase database;
    private RideDetailView mDetailView;
    private LifecycleRegistry mLifecycleRegistry;

    public PreviousRideDetailPresenter(@NonNull RideDetailView detailView, @NonNull AppComponent appComponent) {
        this.mDetailView = detailView;
        appComponent.inject(this);
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.INITIALIZED);
    }

    @Override
    public void onResume() {
        mLifecycleRegistry.markState(Lifecycle.State.RESUMED);
        long rideId = mDetailView.getBundle().getLong(Constants.KEY_BUNDLE_RIDE_ID);
        loadRidePointsForRide(rideId);
    }

    @Override
    public void onDestroy() {
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        mDetailView = null;
        database = null;
    }

    @Override
    public void loadRidePointsForRide(long rideId) {
        LiveData<Ride> selectedRide = database.rideDao().loadRideById(rideId);
        selectedRide.observe(this, (@Nullable final Ride ride) -> {
            if (ride == null) {
                return;
            }

            mDetailView.setBookingId(Long.toString(ride.id));
            mDetailView.setNumberOfPassengers(Integer.toString(ride.numberOfPassengers));
            mDetailView.setStartLocation(ride.startLocation);
            mDetailView.setEndLocation(ride.endLocation);
        });

        LiveData<List<RidePoint>> ridePointData = database.ridePointDao().loadRidePointForRide(rideId);
        ridePointData.observe(this, (@Nullable final List<RidePoint> ridePointList) -> {
            if (ridePointList == null) {
                mDetailView.showNoRidePointsMessage();
                return;
            }

            if (ridePointList.isEmpty()) {
                mDetailView.showNoRidePointsMessage();
                return;
            }

            mDetailView.hideNoRidePointsMessage();
            for (RidePoint singleRidePoint : ridePointList) {
                mDetailView.addRidePoint(singleRidePoint);
            }
        });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}