package hr.from.bkoruznjak.rida.root.location;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import hr.from.bkoruznjak.rida.R;
import hr.from.bkoruznjak.rida.current.CurrentRidePointUploadTask;
import hr.from.bkoruznjak.rida.current.CurrentRideRoomRepository;
import hr.from.bkoruznjak.rida.current.CurrentRideSession;
import hr.from.bkoruznjak.rida.current.contract.CurrentRideRepository;
import hr.from.bkoruznjak.rida.current.model.Ride;
import hr.from.bkoruznjak.rida.current.model.RidePoint;
import hr.from.bkoruznjak.rida.main.MainActivity;
import hr.from.bkoruznjak.rida.root.Constants;
import hr.from.bkoruznjak.rida.root.RidaApp;
import hr.from.bkoruznjak.rida.root.network.NetworkManager;
import hr.from.bkoruznjak.rida.root.network.RidaWebAPI;

import static hr.from.bkoruznjak.rida.root.Constants.SERVICE_NOTIFICATION_ID;

/**
 * Created by bkoruznjak on 29/01/2018.
 */

public class GPSService extends Service implements LocationProviderCallback, CurrentRideRepository.OnDataSavedListener {

    private static GPSService INSTANCE = new GPSService();
    private final String LOG_TAG = "GPSService";
    @Inject
    LocationProvider locationProvider;
    @Inject
    CurrentRideSession currentRideSession;
    @Inject
    CurrentRideRoomRepository currentRideRoomRepository;
    @Inject
    NetworkManager networkManager;
    @Inject
    @Named("simpleWebApi")
    RidaWebAPI webAPI;
    private boolean mIsRunning;

    public static GPSService getInstance() {
        return INSTANCE;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Intent newIntent(Context context) {
        return new Intent(context, GPSService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((RidaApp) getApplication()).getAppComponent().inject(this);
        buildNotificationAndStartService();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    private void buildNotificationAndStartService() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        0
                );
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(Constants.RIDE_NOTIFICATION_CHANNEL_ID, Constants.RIDE_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription(Constants.NOTIFICATION_CHANNEL_DESCRIPTION);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, Constants.RIDE_NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_app_notification)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_ongoing_ride))
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent)
                        .setOngoing(true);
        startForeground(SERVICE_NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIsRunning = true;
        if (locationProvider.isGPSenabledOnDevice() && !locationProvider.isConnected()) {
            locationProvider.addCallback(this);
            locationProvider.connect();
        }

        if (CurrentRideSession.NO_DATABASE_ID_ASSIGNED == currentRideSession.getDatabaseId()) {
            Ride currentRide = new Ride();
            currentRide.numberOfPassengers = currentRideSession.getNumberOfPassengers();
            currentRide.startLocation = currentRideSession.getStartLocation();
            currentRide.endLocation = currentRideSession.getEndLocation();
            currentRideRoomRepository.saveRide(currentRide, this);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsRunning = false;
        if (locationProvider.isConnected()) {
            locationProvider.removeCallback(this);
            locationProvider.disconnect();
        }
        currentRideSession.purgeSession();
    }

    public void start(@NonNull Context context) {
        context.startService(newIntent(context));
    }

    public void stop(@NonNull Context context) {
        context.stopService(newIntent(context));
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    @Override
    public void onLocationChanged(Location location) {
        //create a new location point and save it to db
        if (CurrentRideSession.NO_DATABASE_ID_ASSIGNED != currentRideSession.getDatabaseId()) {
            RidePoint ridePoint = new RidePoint();
            ridePoint.rideId = currentRideSession.getDatabaseId();
            ridePoint.isUploaded = 0;
            ridePoint.latitude = Double.toString(location.getLatitude());
            ridePoint.longitude = Double.toString(location.getLongitude());
            ridePoint.status = currentRideSession.getStatus().name();
            ridePoint.time = currentRideSession.getCurrentTime();
            new CurrentRidePointUploadTask(ridePoint, currentRideRoomRepository, networkManager, webAPI).execute();
        }
    }

    @Override
    public void onLocationProviderError(int errorCode) {
        Log.e(LOG_TAG, "location error:" + errorCode);
    }

    @Override
    public void onSuccess(long id) {
        currentRideSession.setDatabaseId(id);
    }

    @Override
    public void onError(@NonNull Throwable error) {
        Log.e(LOG_TAG, "error during db operation:" + error.toString());
    }
}
