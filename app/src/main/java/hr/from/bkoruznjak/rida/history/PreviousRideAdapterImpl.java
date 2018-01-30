package hr.from.bkoruznjak.rida.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.current.model.Ride;
import hr.from.bkoruznjak.rida.history.contract.PreviousRideView;
import hr.from.bkoruznjak.rida.root.Constants;

/**
 * Created by bkoruznjak on 30/01/2018.
 */
public class PreviousRideAdapterImpl extends RecyclerView.Adapter<PreviousRideAdapterImpl.RideViewHolder> {

    private List<Ride> mRideList;
    private WeakReference<PreviousRideView> mView;

    public PreviousRideAdapterImpl(@NonNull WeakReference<PreviousRideView> holdingFragment) {
        this.mView = holdingFragment;
        this.mRideList = new ArrayList<>(Constants.DEFAULT_COLLECTION_MEMORY_SIZE);
    }

    @Override
    public RideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rideView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride_layout, parent, false);
        return new RideViewHolder(rideView);
    }

    @Override
    public void onBindViewHolder(RideViewHolder holder, int position) {
        holder.rideId = mRideList.get(position).id;
        holder.textBookingId.setText(Long.toString(mRideList.get(position).id));
        holder.textStartLocation.setText(mRideList.get(position).startLocation);
        holder.textEndLocation.setText(mRideList.get(position).endLocation);
    }

    @Override
    public int getItemCount() {
        return mRideList.size();
    }

    public void addSingle(@NonNull Ride ride) {
        for (Ride existingRide : mRideList) {
            //in case of LiveData returning objects updated we replace the ones with new up to date items
            if (existingRide.id == ride.id) {
                mRideList.remove(existingRide);
                break;
            }
        }
        mRideList.add(ride);
    }

    public class RideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected long rideId;
        protected ImageView imageViewDetails;
        protected TextView textBookingId;
        protected TextView textStartLocation;
        protected TextView textEndLocation;

        public RideViewHolder(View itemView) {
            super(itemView);
            this.imageViewDetails = itemView.findViewById(R.id.imageViewDetails);
            this.textBookingId = itemView.findViewById(R.id.textViewTimeValue);
            this.textStartLocation = itemView.findViewById(R.id.textViewLatitudeValue);
            this.textEndLocation = itemView.findViewById(R.id.textViewLongitudeValue);
            this.imageViewDetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                default:
                    throw new IllegalArgumentException("no button found!");
                case R.id.imageViewDetails:
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    if (mView != null && mView.get() != null) {
                        mView.get().goToRideDetails(rideId);
                    }
                    break;
            }
        }
    }
}