package hr.from.bkoruznjak.rida.root.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class NetworkManager {

    public static final String WELL_KNOWN_HOST = "http://www.google.com";
    private final ConnectivityManager mConnectivityManager;

    public NetworkManager(@NonNull ConnectivityManager connectivityManager) {
        this.mConnectivityManager = connectivityManager;
    }

    /**
     * Returns true if a well-known host on the Internet is reachable.
     * <p>
     * NOTE: DO NOT INVOKE ON UI THREAD!
     *
     * @return true if connection is available; false otherwise
     */
    public boolean isInternetReachable() {
        // Assume that google will always be reachable.
        return isHostReachable(WELL_KNOWN_HOST);
    }

    /**
     * Returns true if the given host is reachable.
     * <p>
     * NOTE: DO NOT INVOKE ON UI THREAD!
     *
     * @return true if connection is available; false otherwise
     */
    public boolean isHostReachable(String hostname) {
        if (hasNetworkConnection()) {
            HttpURLConnection connection = null;
            boolean bStatus = false;

            try {
                final URL targetURL = new URL(hostname);

                connection = (HttpURLConnection) (targetURL.openConnection());
                connection.setConnectTimeout(1000); // 1 sec timeout.
                connection.connect();
                bStatus = (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
                connection.getInputStream().close();
            } catch (Exception exc) {
                // Assume false.
                bStatus = false;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return bStatus;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the device is connected to a network.
     * NOTE: connected to the network does not mean having an active internet connection
     * check isInternetReachable()
     *
     * @return true if network is online
     */
    public boolean hasNetworkConnection() {
        final boolean bStatus;

        if (mConnectivityManager == null || mConnectivityManager.getActiveNetworkInfo() == null) {
            bStatus = false;
        } else {
            bStatus = mConnectivityManager.getActiveNetworkInfo().isConnected();
        }

        return bStatus;
    }

    /**
     * Returns true if the device has a WiFi connection.
     *
     * @return true or false
     */
    public boolean hasWiFiConnection() {
        if (mConnectivityManager != null) {
            final NetworkInfo networkInfoWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (networkInfoWifi != null && networkInfoWifi.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the device has a cellular connection.
     *
     * @return true or false
     */
    public boolean hasMobileConnection() {
        if (mConnectivityManager != null) {
            final NetworkInfo networkInfoWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (networkInfoWifi != null && networkInfoWifi.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the device has a Ethernet connection.
     *
     * @return true or false
     */
    public boolean hasEthernetConnection() {
        if (mConnectivityManager != null) {
            final NetworkInfo ethernet = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

            if (ethernet != null && ethernet.isConnected()) {
                return true;
            }
        }
        return false;
    }
}