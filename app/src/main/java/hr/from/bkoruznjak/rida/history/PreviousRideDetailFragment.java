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

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.databinding.FragmentPreviousRideDetailBinding;
import hr.from.bkoruznjak.rida.history.contract.RideDetailView;
import hr.from.bkoruznjak.rida.root.Constants;
import hr.from.bkoruznjak.rida.root.RidaApp;

import static hr.from.bkoruznjak.rida.current.CurrentRideSession.NO_DATABASE_ID_ASSIGNED;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class PreviousRideDetailFragment extends Fragment implements RideDetailView {

    private FragmentPreviousRideDetailBinding mBinding;
    private PreviousRideDetailPresenter mPresenter;
    private RidePointAdapter mRidePointAdapter;

    public PreviousRideDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArguments().getLong(Constants.KEY_BUNDLE_RIDE_ID, NO_DATABASE_ID_ASSIGNED);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_previous_ride_detail, container, false);
        init();
        return mBinding.getRoot();
    }

    private void init() {
        mPresenter = new PreviousRideDetailPresenter(this, ((RidaApp) getActivity().getApplicationContext()).getAppComponent());
        mRidePointAdapter = new RidePointAdapter();
        mBinding.recyclerViewRides.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewRides.setItemAnimator(new DefaultItemAnimator());
        mBinding.recyclerViewRides.setAdapter(mRidePointAdapter);
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
    public void addRidePoint(@NonNull RidePoint ridePoint) {
        getActivity().runOnUiThread(() -> {
            mRidePointAdapter.addSingle(ridePoint);
            mRidePointAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void showNoRidePointsMessage() {
        getActivity().runOnUiThread(() -> mBinding.textNoRidePointsToShow.setVisibility(View.VISIBLE));
    }

    @Override
    public void hideNoRidePointsMessage() {
        getActivity().runOnUiThread(() -> mBinding.textNoRidePointsToShow.setVisibility(View.GONE));
    }

    @Override
    public void setBookingId(@NonNull String bookingId) {
        getActivity().runOnUiThread(() -> mBinding.textViewBookingIdValue.setText(bookingId));
    }

    @Override
    public void setNumberOfPassengers(@NonNull String numberOfPassengers) {
        getActivity().runOnUiThread(() -> mBinding.textViewPassengersValue.setText(numberOfPassengers));
    }

    @Override
    public void setStartLocation(@NonNull String startLocation) {
        getActivity().runOnUiThread(() -> mBinding.textViewStartLocationValue.setText(startLocation));

    }

    @Override
    public void setEndLocation(@NonNull String endLocation) {
        getActivity().runOnUiThread(() -> mBinding.textViewEndLocaationValue.setText(endLocation));
    }


    @NonNull
    @Override
    public Bundle getBundle() {
        return getArguments();
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