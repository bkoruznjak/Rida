package hr.from.bkoruznjak.rida.current;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import javax.inject.Inject;

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.current.contract.CurrentRidePresenter;
import hr.from.bkoruznjak.rida.current.contract.CurrentRideView;
import hr.from.bkoruznjak.rida.current.model.RideStatus;
import hr.from.bkoruznjak.rida.root.AppComponent;
import hr.from.bkoruznjak.rida.root.Constants;

import static hr.from.bkoruznjak.rida.root.Constants.NO_ANIMATION;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class CurrentRidePresenterImpl implements CurrentRidePresenter, LifecycleOwner {

    private final String LOG_TAG = "Ride_Pres";
    @Inject
    CurrentRideSession currentRideSession;
    private LifecycleRegistry mLifecycleRegistry;
    private CurrentRideView mView;
    private boolean mSecondaryButtonVisible;

    public CurrentRidePresenterImpl(@NonNull CurrentRideView currentRideView, @NonNull AppComponent appComponent) {
        this.mView = currentRideView;
        appComponent.inject(this);
        this.mLifecycleRegistry = new LifecycleRegistry(this);
        this.mLifecycleRegistry.markState(Lifecycle.State.INITIALIZED);
    }

    @Override
    public void onResume() {
        mLifecycleRegistry.markState(Lifecycle.State.RESUMED);

        determineSecondaryActionButtonVisiblity(currentRideSession);
        updateCurrentRideUi(NO_ANIMATION);
    }

    @Override
    public void onDestroy() {
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        mView = null;
    }

    @Override
    public void mainActionClicked() {
        mView.disableMainActionButtonClick(Constants.BUTTON_DISABLE_COOLDOWN_IN_MILLIS);
        switch (currentRideSession.getStatus()) {
            case IDLE:
                startNewRide();
                break;
            case STARTED:
            case STOP_OVER:
                currentRideSession.setStatus(RideStatus.PASSENGER_PICKED_UP);
                break;
            case PASSENGER_PICKED_UP:
                currentRideSession.setStatus(RideStatus.IDLE);
                break;
            default:
                throw new IllegalArgumentException("Invalid ride status");
        }
        updateCurrentRideUi(Constants.ANIMATION_DURATION_IN_MILLIS);
    }


    @Override
    public void secondaryActionClicked() {
        mView.disableSecondaryActionButtonClick(Constants.BUTTON_DISABLE_COOLDOWN_IN_MILLIS);
        switch (currentRideSession.getStatus()) {
            case PASSENGER_PICKED_UP:
                currentRideSession.setStatus(RideStatus.STOP_OVER);
                break;
            default:
                throw new IllegalArgumentException("Invalid ride status");
        }
        updateCurrentRideUi(Constants.ANIMATION_DURATION_IN_MILLIS);
    }

    private void startNewRide() {
        String startLocation = mView.getStartLocation();
        String endLocation = mView.getEndLocation();
        if (TextUtils.isEmpty(startLocation)) {
            mView.showToast(R.string.toast_start_location_empty);
            return;
        }

        if (TextUtils.isEmpty(endLocation)) {
            mView.showToast(R.string.toast_end_location_empty);
            return;
        }

        currentRideSession.setStartLocation(mView.getStartLocation());
        currentRideSession.setEndLocation(mView.getEndLocation());
        currentRideSession.setNumberOfPassengers(Integer.valueOf(mView.getNumberOfPassengers()));
        currentRideSession.setStatus(RideStatus.STARTED);

    }

    private void determineSecondaryActionButtonVisiblity(CurrentRideSession rideSession) {
        switch (rideSession.getStatus()) {
            case PASSENGER_PICKED_UP:
                mSecondaryButtonVisible = true;
                break;
            default:
                mSecondaryButtonVisible = false;
                mView.hideSecondaryActionButton(NO_ANIMATION);
                break;
        }
    }

    private void updateCurrentRideUi(long duration) {
        RideStatus status = currentRideSession.getStatus();
        mView.setStartLocation(currentRideSession.getStartLocation());
        mView.setEndLocation(currentRideSession.getEndLocation());
        mView.setNumberOfPassengers(currentRideSession.getNumberOfPassengers() - 1);

        switch (status) {
            case IDLE:
                mView.setMainActionButtonText(R.string.action_start_ride);
                if (mSecondaryButtonVisible) {
                    mSecondaryButtonVisible = false;
                    mView.hideSecondaryActionButton(duration);
                }
                break;
            case STARTED:
                mView.setMainActionButtonText(R.string.action_passengers_picked_up);
                if (mSecondaryButtonVisible) {
                    mSecondaryButtonVisible = false;
                    mView.hideSecondaryActionButton(duration);
                }
                break;
            case PASSENGER_PICKED_UP:
                mView.setMainActionButtonText(R.string.action_end_ride);
                if (!mSecondaryButtonVisible) {
                    mSecondaryButtonVisible = true;
                    mView.showSecondaryActionButton(duration);
                }
                break;
            case STOP_OVER:
                mView.setMainActionButtonText(R.string.action_continue_ride);
                if (mSecondaryButtonVisible) {
                    mSecondaryButtonVisible = false;
                    mView.hideSecondaryActionButton(duration);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid ride status");
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

}