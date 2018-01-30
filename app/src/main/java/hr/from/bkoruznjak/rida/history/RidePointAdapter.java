package hr.from.bkoruznjak.rida.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.root.Constants;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class RidePointAdapter extends RecyclerView.Adapter<RidePointAdapter.RideViewHolder> {

    private List<RidePoint> mRidePointList;

    public RidePointAdapter() {
        this.mRidePointList = new ArrayList<>(Constants.DEFAULT_COLLECTION_MEMORY_SIZE);
    }

    @Override
    public RidePointAdapter.RideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rideView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride_point_layout, parent, false);
        return new RidePointAdapter.RideViewHolder(rideView);
    }

    @Override
    public void onBindViewHolder(RidePointAdapter.RideViewHolder holder, int position) {
        holder.textViewTime.setText(mRidePointList.get(position).time);
        holder.textViewLatitude.setText(mRidePointList.get(position).latitude);
        holder.textViewLongitude.setText(mRidePointList.get(position).longitude);
        holder.textViewStatus.setText(mRidePointList.get(position).status);
    }

    @Override
    public int getItemCount() {
        return mRidePointList.size();
    }

    public void addSingle(@NonNull RidePoint ridePoint) {
        for (RidePoint existingRidePoint : mRidePointList) {
            //in case of LiveData returning objects updated we replace the ones with new up to date items
            if (existingRidePoint.id == ridePoint.id) {
                mRidePointList.remove(existingRidePoint);
                break;
            }
        }
        mRidePointList.add(ridePoint);
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {

        protected TextView textViewTime;
        protected TextView textViewLatitude;
        protected TextView textViewLongitude;
        protected TextView textViewStatus;

        public RideViewHolder(View itemView) {
            super(itemView);
            this.textViewTime = itemView.findViewById(R.id.textViewTimeValue);
            this.textViewLatitude = itemView.findViewById(R.id.textViewLatitudeValue);
            this.textViewLongitude = itemView.findViewById(R.id.textViewLongitudeValue);
            this.textViewStatus = itemView.findViewById(R.id.textViewStatusValue);
        }
    }
}