package hr.from.bkoruznjak.rida.main.contract;

import android.support.annotation.MainThread;

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
    void setupDriverInfo(int resourceId);

    @MainThread
    void showLocationPermissionExplanation();

    @MainThread
    void showNoGpsAlert();

    void requestLocationPermission();

    boolean checkLocationPermission();
}
