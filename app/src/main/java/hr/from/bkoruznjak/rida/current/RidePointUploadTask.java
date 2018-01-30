package hr.from.bkoruznjak.rida.current;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;

import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.root.network.NetworkManager;
import hr.from.bkoruznjak.rida.root.network.RidaWebAPI;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class RidePointUploadTask extends AsyncTask<Void, Void, Integer> {

    public static final int CODE_TASK_DONE_OK = 1;
    public static final int CODE_TASK_FAILED_NO_NET = 2;
    public static final int CODE_TASK_FAILED_API_ERROR = 3;
    private final String LOG_TAG = "UploadTask";
    private RidePoint mRidePoint;
    private CurrentRideRoomRepository mRoomRepository;
    private NetworkManager mNetworkManager;
    private RidaWebAPI mWebAPI;

    public RidePointUploadTask(@NonNull RidePoint ridePoint,
                               @NonNull CurrentRideRoomRepository roomRepository,
                               @NonNull NetworkManager networkManager,
                               @NonNull RidaWebAPI webAPI) {
        this.mRidePoint = ridePoint;
        this.mRoomRepository = roomRepository;
        this.mNetworkManager = networkManager;
        this.mWebAPI = webAPI;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        mRoomRepository.saveRidePointSync(mRidePoint);
        boolean deviceHasActiveInternetConnection = mNetworkManager.isInternetReachable();

        if (deviceHasActiveInternetConnection) {
            Call<JsonObject> postRidePointCall = mWebAPI.sendRideInfo(1, mRidePoint);
            try {
                Response<JsonObject> response = postRidePointCall.execute();
                if (response.isSuccessful()) {
                    mRidePoint.isUploaded = 1;
                    mRoomRepository.updateRidePointSync(mRidePoint);
                    return CODE_TASK_DONE_OK;

                } else {
                    Log.e(LOG_TAG, response.message());
                    return CODE_TASK_FAILED_API_ERROR;
                }
            } catch (IOException ioEx) {
                Log.e(LOG_TAG, ioEx.getMessage());
                return CODE_TASK_FAILED_API_ERROR;
            }
        } else {
            return CODE_TASK_FAILED_NO_NET;
        }
    }

    @Override
    protected void onPostExecute(Integer finishCode) {
        super.onPostExecute(finishCode);
        mRidePoint = null;
        mRoomRepository = null;
        mNetworkManager = null;
        mWebAPI = null;
        switch (finishCode) {
            case CODE_TASK_DONE_OK:
                Log.i(LOG_TAG, "all ok");
                break;
            case CODE_TASK_FAILED_API_ERROR:
                Log.i(LOG_TAG, "api error happened");
                break;
            case CODE_TASK_FAILED_NO_NET:
                Log.i(LOG_TAG, "no internet on device");
                break;
            default:
                throw new IllegalStateException("task finished with unknown outcome");
        }
    }
}