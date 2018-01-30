package hr.from.bkoruznjak.rida.current.contract;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import hr.from.bkoruznjak.rida.root.contract.RootView;


/**
 * Created by bkoruznjak on 28/01/2018.
 */

public interface CurrentRideView extends RootView {

    @Nullable
    String getStartLocation();

    @MainThread
    void setStartLocation(@NonNull String startLocation);

    @Nullable
    String getEndLocation();

    @MainThread
    void setEndLocation(@NonNull String endLocation);

    String getNumberOfPassengers();

    @MainThread
    void setNumberOfPassengers(int numberOfPassengers);

    @MainThread
    void setMainActionButtonText(int actionTextRes);

    @MainThread
    void setSecondaryActionButtonText(int actionTextRes);

    @MainThread
    void showSecondaryActionButton(long duration);

    @MainThread
    void hideSecondaryActionButton(long duration);

    @MainThread
    void disableMainActionButtonClick(long duration);

    @MainThread
    void disableSecondaryActionButtonClick(long duration);

    void startGPSService();

    void stopGPSService();
}
