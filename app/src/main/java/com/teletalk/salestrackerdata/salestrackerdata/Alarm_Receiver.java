package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
//import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Alarm_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
//  Toast.makeText(context, "Started after some time", Toast.LENGTH_LONG).show();
        Log.w("SalesTrackerData:", "AlarmReciever");
        String timerComplete = AndroidUtils.getfileContent(context, AndroidUtils.TIMER_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        AndroidUtils.getCurrentLocation(context);

//        if (timerComplete.equalsIgnoreCase("n")) {
//            Toast.makeText(context, "Timer Finished", Toast.LENGTH_LONG).show();
        Intent service = new Intent(context, Network.class);
        context.startService(service);
//        }
    }

}
