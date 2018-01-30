package hr.from.bkoruznjak.rida.main;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Named;

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.main.contract.MainPresenter;
import hr.from.bkoruznjak.rida.main.contract.MainView;
import hr.from.bkoruznjak.rida.root.AppComponent;
import hr.from.bkoruznjak.rida.root.PageAdapter;

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
        mView.setupDriverInfo(R.drawable.img_profile_jensen);

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
}