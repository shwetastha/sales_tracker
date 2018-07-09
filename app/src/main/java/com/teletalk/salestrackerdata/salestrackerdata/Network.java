package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class Network extends JobService {

    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private static final int JOB_DATABASE_WRITE_ID = 3478;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;


    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = NetworkUtil.getConnectivityStatusString(context);
            String msgSentStatus = AndroidUtils.getfileContent(context, AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);

            if (status == 1 && msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N)
                    && AndroidUtils.isSimConnected(context)) {
                Log.w("SalesTrackerData:", "Network: Internet is Working.");
                SaleTrackerTestClass.showMessageInToast(context, "Network: Internet is Working.");

                // Calling the service class.
                ComponentName componentName = new ComponentName(context, InternetIntentService.class);
                JobInfo.Builder builder = new JobInfo.Builder(JOB_DATABASE_WRITE_ID, componentName);
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                jobInfo = builder.build();
                jobScheduler = ( JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                jobScheduler.schedule(jobInfo);
            }
        }
    };
    private LocationManager mLocationManager = null;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        initializeLocationManager();
        final IntentFilter intentFilters = new IntentFilter();
        intentFilters.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilters.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(receiver, intentFilters);

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        unregisterReceiver(receiver);
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
        return false;
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            if (location != null) {
                Log.i("SuperMap", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
                AndroidUtils.setLATITUDE(String.valueOf(location.getLatitude()));
                AndroidUtils.setLONGITUDE(String.valueOf(location.getLongitude()));
                Log.i("latitude,longitude", "" + AndroidUtils.LATITUDE + "," + AndroidUtils.LONGITUDE);
                int status = NetworkUtil.getConnectivityStatusString(getApplicationContext());
                String msgSentStatus = AndroidUtils.getfileContent(getApplicationContext(), AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);

                if (status == 1 && msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N)
                        && AndroidUtils.isSimConnected(getApplicationContext())) {
                    Log.w("SalesTrackerData:", "Network: Internet is Working.");

                    Intent service = new Intent(getApplicationContext(), InternetIntentService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getApplicationContext().startForegroundService(service);
                    } else {
                        getApplicationContext().startService(service);
                    }
                }

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
}
