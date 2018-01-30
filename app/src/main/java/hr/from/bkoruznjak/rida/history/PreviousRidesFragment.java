package hr.from.bkoruznjak.rida.history;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.current.model.Ride;
import hr.from.bkoruznjak.rida.databinding.FragmentPreviousRidesBinding;
import hr.from.bkoruznjak.rida.history.contract.PreviousRidePresenter;
import hr.from.bkoruznjak.rida.history.contract.PreviousRideView;
import hr.from.bkoruznjak.rida.root.RideApp;

public class PreviousRidesFragment extends Fragment implements PreviousRideView {

    private FragmentPreviousRidesBinding mBinding;
    private PreviousRidePresenter mPresenter;
    private PreviousRideAdapterImpl mPreviousRideAdapter;

    public PreviousRidesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_previous_rides, container, false);
        init();
        return mBinding.getRoot();
    }

    private void init() {
        mPresenter = new PreviousRidesPresenterImpl(this, ((RideApp) getActivity().getApplicationContext()).getAppComponent());
        mPreviousRideAdapter = new PreviousRideAdapterImpl(new WeakReference<>(this));
        mBinding.recyclerViewRides.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewRides.setItemAnimator(new DefaultItemAnimator());
        mBinding.recyclerViewRides.setAdapter(mPreviousRideAdapter);
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
        mPresenter = null;
    }

    @Override
    public void addRide(@NonNull Ride ride) {
        getActivity().runOnUiThread(() -> {
            mPreviousRideAdapter.addSingle(ride);
            mPreviousRideAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void showNoRidesMessage() {
        getActivity().runOnUiThread(() -> mBinding.textViewNoRidesToShow.setVisibility(View.VISIBLE));
    }

    @Override
    public void hideNoRideMessage() {
        getActivity().runOnUiThread(() -> mBinding.textViewNoRidesToShow.setVisibility(View.GONE));
    }

    @Override
    public void goToRideDetails(long rideId) {
        //todo
    }

    @Override
    public void showToast(int messageResourceId) {
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), messageResourceId, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void goBack() {
        getActivity().finish();
    }
}
