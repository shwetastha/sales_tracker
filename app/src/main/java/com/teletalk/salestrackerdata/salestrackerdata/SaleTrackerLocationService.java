package com.teletalk.salestrackerdata.salestrackerdata;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

public class SaleTrackerLocationService extends JobService {
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0;
    private LocationManager mLocationManager = null;
    private SalesTrackerLocationListener salesTrackerLocationListener = null;

    @Override
    public boolean onStartJob(JobParameters params) {
        salesTrackerLocationListener = new SalesTrackerLocationListener();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                salesTrackerLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                salesTrackerLocationListener);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}