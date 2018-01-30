package hr.from.bkoruznjak.rida.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.databinding.ActivityMainBinding;
import hr.from.bkoruznjak.rida.main.contract.MainPresenter;
import hr.from.bkoruznjak.rida.main.contract.MainView;
import hr.from.bkoruznjak.rida.root.PageAdapter;
import hr.from.bkoruznjak.rida.root.RideApp;

import static hr.from.bkoruznjak.rida.root.Constants.LOCATION_PERMISSION_ID;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private MainPresenter mPresenter;
    private ActivityMainBinding mBinding;
    private PageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mPresenter = new MainPresenterImpl(this, ((RideApp) getApplication()).getAppComponent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    @MainThread
    public void setupPageFragments(int currentPageIndex) {
        runOnUiThread(() -> {
            mPageAdapter = new PageAdapter(getSupportFragmentManager(), PageAdapter.MAX_NUMBER_OF_PAGES);
            mBinding.mainAppBar.mainContent.viewPager.setAdapter(mPageAdapter);
            mBinding.mainAppBar.mainContent.bottomNavigationView.setSelectedItemId(currentPageIndex);
            mBinding.mainAppBar.mainContent.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (position) {
                        case PageAdapter.PAGE_CURRENT_RIDE:
                            mBinding.mainAppBar.mainContent.bottomNavigationView.setSelectedItemId(R.id.menu_current);
                            break;
                        case PageAdapter.PAGE_RIDE_HISTORY:
                            mBinding.mainAppBar.mainContent.bottomNavigationView.setSelectedItemId(R.id.menu_history);
                            break;
                        default:
                            throw new IllegalStateException("no such page");
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        });
    }

    @MainThread
    @Override
    public void setupBottomNavigation(int selectedItemId) {
        runOnUiThread(() -> {
            mBinding.mainAppBar.mainContent.bottomNavigationView.setOnNavigationItemSelectedListener((@NonNull MenuItem item) -> {
                switch (item.getItemId()) {
                    case R.id.menu_current:
                        mBinding.mainAppBar.mainContent.viewPager.setCurrentItem(PageAdapter.PAGE_CURRENT_RIDE);
                        break;
                    case R.id.menu_history:
                        mBinding.mainAppBar.mainContent.viewPager.setCurrentItem(PageAdapter.PAGE_RIDE_HISTORY);
                        break;
                    default:
                        throw new IllegalStateException("no such page");
                }
                return true;
            });
            mBinding.mainAppBar.mainContent.bottomNavigationView.setSelectedItemId(selectedItemId);
        });
    }

    @MainThread
    @Override
    public void setupNavigationDrawer() {
        runOnUiThread(() -> {
            setSupportActionBar(mBinding.mainAppBar.toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this,
                    mBinding.drawerLayout,
                    mBinding.mainAppBar.toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            );
            mBinding.drawerLayout.addDrawerListener(toggle);
            mBinding.navView.setNavigationItemSelectedListener(this);
            toggle.syncState();
        });
    }

    @MainThread
    @Override
    public void setupDriverInfo(int resourceId) {
        runOnUiThread(() -> {
            View headerView = mBinding.navView.getHeaderView(0);
            ImageView image = headerView.findViewById(R.id.imageViewProfilePic);
            Glide.with(this).load(resourceId).apply(RequestOptions.circleCropTransform()).into(image);
        });
    }

    @MainThread
    @Override
    public void showLocationPermissionExplanation() {
        runOnUiThread(() -> {
            android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(this);
            alertBuilder.setTitle(R.string.dialog_location_permission_title);
            alertBuilder.setMessage(R.string.dialog_location_permission_content);
            alertBuilder.setPositiveButton(R.string.dialog_confirmation, (DialogInterface dialog, int which) -> requestLocationPermission());
            alertBuilder.setCancelable(false);
            android.app.AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        });
    }

    @MainThread
    @Override
    public void showNoGpsAlert() {
        runOnUiThread(() -> {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setTitle(getString(R.string.dialog_no_gps_title));
            dialog.setMessage(getString(R.string.dialog_no_gps_content));
            dialog.setPositiveButton(getResources().getString(R.string.dialog_no_gps_confirm), (DialogInterface paramDialogInterface, int paramInt) -> {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
            );
            dialog.setNegativeButton(getString(R.string.dialog_no_gps_cancel), (DialogInterface paramDialogInterface, int paramInt) -> {
                finish();
            });
            dialog.show();
        });
    }

    @Override
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
    }

    @Override
    public boolean checkLocationPermission() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.handlePermissionResult(requestCode, permissions, grantResults);
    }

    @MainThread
    @Override
    public void showToast(int messageResourceId) {
        runOnUiThread(() -> Toast.makeText(this, messageResourceId, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void goBack() {
        finish();
    }
}
