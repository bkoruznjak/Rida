package hr.from.bkoruznjak.rida.main;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Named;

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.main.contract.MainPresenter;
import hr.from.bkoruznjak.rida.main.contract.MainView;
import hr.from.bkoruznjak.rida.root.AppComponent;
import hr.from.bkoruznjak.rida.root.Constants;
import hr.from.bkoruznjak.rida.root.PageAdapter;

import static hr.from.bkoruznjak.rida.root.Constants.KEY_DRIVER_AGE;
import static hr.from.bkoruznjak.rida.root.Constants.KEY_DRIVER_LICENSE;
import static hr.from.bkoruznjak.rida.root.Constants.KEY_DRIVER_NAME;
import static hr.from.bkoruznjak.rida.root.Constants.KEY_DRIVER_SURNAME;
import static hr.from.bkoruznjak.rida.root.Constants.LOCATION_PERMISSION_ID;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class MainPresenterImpl implements MainPresenter {

    @Inject
    JobScheduler jobScheduler;
    @Inject
    @Named("uploadJob")
    JobInfo nonUploadedJob;
    @Inject
    SharedPreferences preferences;
    private MainView mView;

    public MainPresenterImpl(@NonNull MainView mainView, @NonNull AppComponent appComponent) {
        this.mView = mainView;
        appComponent.inject(this);
    }

    @Override
    public void onResume() {
        mView.setupPageFragments(PageAdapter.PAGE_CURRENT_RIDE);
        mView.setupBottomNavigation(R.id.menu_current);
        mView.setupNavigationDrawer();
        mView.loadDriverInfo(R.drawable.img_profile_jensen,
                preferences.getString(KEY_DRIVER_NAME, ""),
                preferences.getString(KEY_DRIVER_SURNAME, ""),
                preferences.getString(KEY_DRIVER_AGE, ""),
                preferences.getString(KEY_DRIVER_LICENSE, ""));

        if (!mView.checkLocationPermission()) {
            mView.showLocationPermissionExplanation();
        }

        scheduleUploadJob();
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void handlePermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    mView.goBack();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void scheduleUploadJob() {
        jobScheduler.schedule(nonUploadedJob);
    }

    @Override
    public void editProfileClicked() {
        mView.showEditProfileDialog();
    }

    @Override
    public void updateDriverInfo(@Nullable String name, @Nullable String surname, @Nullable String age, @Nullable String license) {
        if (!TextUtils.isEmpty(name)) {
            preferences.edit().putString(Constants.KEY_DRIVER_NAME, name).apply();
        }

        if (!TextUtils.isEmpty(surname)) {
            preferences.edit().putString(Constants.KEY_DRIVER_SURNAME, surname).apply();
        }

        if (!TextUtils.isEmpty(age)) {
            preferences.edit().putString(Constants.KEY_DRIVER_AGE, age).apply();
        }

        if (!TextUtils.isEmpty(license)) {
            preferences.edit().putString(Constants.KEY_DRIVER_LICENSE, license).apply();
        }

        mView.loadDriverInfo(R.drawable.img_profile_jensen,
                preferences.getString(KEY_DRIVER_NAME, ""),
                preferences.getString(KEY_DRIVER_SURNAME, ""),
                preferences.getString(KEY_DRIVER_AGE, ""),
                preferences.getString(KEY_DRIVER_LICENSE, ""));
    }
}