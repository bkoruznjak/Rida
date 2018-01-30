package hr.from.bkoruznjak.rida.root;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface Constants {

    //MEMORY
    int DEFAULT_COLLECTION_MEMORY_SIZE = 4;
    //SHARED PREFERENCES
    long NO_CURRENT_RIDE = -1;
    String KEY_CURRENT_RIDE_ID = "key.current.ride.id";
    String KEY_BUNDLE_RIDE_ID = "rideId";
    //PERMISSIONS
    int LOCATION_PERMISSION_ID = 1337;
    int SERVICE_NOTIFICATION_ID = 1338;
    //NOTIFICATIONS
    String NOTIFICATION_CHANNEL_DESCRIPTION = "rida_app_notifications";
    String RIDE_NOTIFICATION_CHANNEL_ID = "ongoing_rida_notification";
    String RIDE_NOTIFICATION_CHANNEL_NAME = "rida_location_notifications";
    //DATABASE
    String DATABASE_NAME = "rida.db";
    //ANIMATIONS
    int TRANSLATION_DISTANCE = 200;
    long NO_ANIMATION = 0L;
    long ANIMATION_DURATION_IN_MILLIS = 300L;
    //DISABLE COOLDOWN
    long BUTTON_DISABLE_COOLDOWN_IN_MILLIS = 500L;
}