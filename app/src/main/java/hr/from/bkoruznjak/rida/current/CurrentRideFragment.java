package hr.from.bkoruznjak.rida.current;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.current.contract.CurrentRidePresenter;
import hr.from.bkoruznjak.rida.current.contract.CurrentRideView;
import hr.from.bkoruznjak.rida.databinding.FragmentCurrentRideBinding;
import hr.from.bkoruznjak.rida.root.RideApp;
import hr.from.bkoruznjak.rida.root.location.GPSService;

import static hr.from.bkoruznjak.rida.root.Constants.TRANSLATION_DISTANCE;

public class CurrentRideFragment extends Fragment implements CurrentRideView, View.OnClickListener {

    private FragmentCurrentRideBinding mBinding;
    private CurrentRidePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_current_ride, container, false);
        mPresenter = new CurrentRidePresenterImpl(this, ((RideApp) getActivity().getApplicationContext()).getAppComponent());
        setupListeners();
        return mBinding.getRoot();
    }

    private void setupListeners() {
        mBinding.buttonMainAction.setOnClickListener(this);
        mBinding.buttonSecondaryAction.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @MainThread
    @Override
    public void showToast(int messageResourceId) {
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), messageResourceId, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void goBack() {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonMainAction:
                mPresenter.mainActionClicked();
                break;
            case R.id.buttonSecondaryAction:
                mPresenter.secondaryActionClicked();
                break;
            default:
                throw new IllegalStateException("no such button");
        }
    }

    @Nullable
    @Override
    public String getStartLocation() {
        return mBinding.editTextStartLocation.getText().toString();
    }

    @MainThread
    @Override
    public void setStartLocation(@NonNull String startLocation) {
        getActivity().runOnUiThread(() -> mBinding.editTextStartLocation.setText(startLocation));

    }

    @Nullable
    @Override
    public String getEndLocation() {
        return mBinding.editTextEndLocation.getText().toString();
    }

    @MainThread
    @Override
    public void setEndLocation(@NonNull String endLocation) {
        getActivity().runOnUiThread(() -> mBinding.editTextEndLocation.setText(endLocation));
    }

    @Override
    public String getNumberOfPassengers() {
        return (String) mBinding.spinnerPassengers.getSelectedItem();
    }

    @Override
    public void setNumberOfPassengers(int numberOfPassengers) {
        getActivity().runOnUiThread(() -> mBinding.spinnerPassengers.setSelection(numberOfPassengers));
    }

    @MainThread
    @Override
    public void setMainActionButtonText(int actionTextRes) {
        getActivity().runOnUiThread(() -> mBinding.buttonMainAction.setText(getString(actionTextRes)));
    }

    @MainThread
    @Override
    public void setSecondaryActionButtonText(int actionTextRes) {
        getActivity().runOnUiThread(() -> mBinding.buttonSecondaryAction.setText(getString(actionTextRes)));
    }

    @MainThread
    @Override
    public void showSecondaryActionButton(long duration) {
        getActivity().runOnUiThread(() ->
                mBinding.buttonSecondaryAction
                        .animate()
                        .setDuration(duration)
                        .translationYBy(-TRANSLATION_DISTANCE));
    }

    @MainThread
    @Override
    public void hideSecondaryActionButton(long duration) {
        getActivity().runOnUiThread(() ->
                mBinding.buttonSecondaryAction
                        .animate()
                        .setDuration(duration)
                        .translationYBy(TRANSLATION_DISTANCE));
    }

    @Override
    public void disableMainActionButtonClick(long duration) {
        getActivity().runOnUiThread(() -> {
            mBinding.buttonMainAction.setOnClickListener(null);
            new Handler().postDelayed(() -> mBinding.buttonMainAction.setOnClickListener(this), duration);
        });
    }

    @Override
    public void disableSecondaryActionButtonClick(long duration) {
        getActivity().runOnUiThread(() -> {
            mBinding.buttonSecondaryAction.setOnClickListener(null);
            new Handler().postDelayed(() -> mBinding.buttonMainAction.setOnClickListener(this), duration);
        });
    }

    @Override
    public void startGPSService() {
        GPSService.getInstance().start(getActivity());
    }

    @Override
    public void stopGPSService() {
        GPSService.getInstance().stop(getActivity());
    }
}