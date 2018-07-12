package com.teletalk.salestrackerdata.salestrackerdata;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class SalesTrackerLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location);
        if (location != null) {
            Log.i("SuperMap", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
            AndroidUtils.setLATITUDE(String.valueOf(location.getLatitude()));
            AndroidUtils.setLONGITUDE(String.valueOf(location.getLongitude()));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
