package hr.from.bkoruznjak.rida.jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import hr.from.bkoruznjak.rida.current.CurrentRideRoomRepository;
import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.root.RidaApp;
import hr.from.bkoruznjak.rida.root.database.RidaDatabase;
import hr.from.bkoruznjak.rida.root.network.RidaWebAPI;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class NonUploadedJob extends JobService {

    public static final String LOG_TAG = "UploadJob";
    public static final int MAX_NUMBER_OF_REQUESTS_PER_JOB = 100;
    public static final int BACKGROUND_AUTH_JOB_ID = 1339;
    public static final long HTTP_REQUEST_DELAY_IN_MILLIS = 25L;
    public static final long JOB_PERIOD_IN_MILLIS = 5 * 60 * 10 * 1000; //5 minutes delay

    @Inject
    RidaDatabase database;
    @Inject
    @Named("simpleWebApi")
    RidaWebAPI webAPI;
    @Inject
    CurrentRideRoomRepository roomRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        ((RidaApp) getApplication()).getAppComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        new Thread(() -> {
            List<RidePoint> nonUploadedRidePointList = database.ridePointDao().getAllRidePointsPendingUpload();
            if (MAX_NUMBER_OF_REQUESTS_PER_JOB < nonUploadedRidePointList.size()) {
                nonUploadedRidePointList = nonUploadedRidePointList.subList(0, MAX_NUMBER_OF_REQUESTS_PER_JOB);
            }

            for (RidePoint nonUploadedRidePoint : nonUploadedRidePointList) {
                Call<JsonObject> postRidePointCall = webAPI.sendRideInfo(1, nonUploadedRidePoint);
                try {
                    Response<JsonObject> response = postRidePointCall.execute();
                    if (response.isSuccessful()) {
                        nonUploadedRidePoint.isUploaded = 1;
                        roomRepository.updateRidePointSync(nonUploadedRidePoint);
                    } else {
                        Log.e(LOG_TAG, response.message());
                    }
                    Thread.sleep(HTTP_REQUEST_DELAY_IN_MILLIS);
                } catch (IOException ioEx) {
                    Log.e(LOG_TAG, ioEx.getMessage());
                } catch (InterruptedException intEx) {
                    Log.e(LOG_TAG, intEx.getMessage());
                }
            }
            jobFinished(params, false);
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database = null;
        webAPI = null;
        roomRepository = null;
    }
}