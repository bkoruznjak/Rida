package hr.from.bkoruznjak.rida.main.contract;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import hr.from.bkoruznjak.rida.root.contract.RootView;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface MainView extends RootView {

    @MainThread
    void setupPageFragments(int selectedFragmentId);

    @MainThread
    void setupBottomNavigation(int selectedItemId);

    @MainThread
    void setupNavigationDrawer();

    @MainThread
    void loadDriverInfo(int profilePictureResourceId,
                        @NonNull String name,
                        @NonNull String surname,
                        @NonNull String age,
                        @NonNull String license);

    @MainThread
    void showLocationPermissionExplanation();

    @MainThread
    void showEditProfileDialog();

    @MainThread
    void showNoGpsAlert();

    void requestLocationPermission();

    boolean checkLocationPermission();
}
