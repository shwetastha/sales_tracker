package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
//import android.widget.Toast;

import java.util.Calendar;

public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SaleTrackerTestClass.showMessageInToast(context, "Boot Complete.");
        SaleTrackerTestClass.showMessageInToast(context,"PowerOn");
//        BootCompletedJobService.enqueueWork(context, new Intent());
        Log.w("SalesTrackerData:", "BootComplete.V5");

        //Reading content from file.
        String timerComplete = AndroidUtils.getfileContent(context, AndroidUtils.TIMER_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String msgSentStatus = AndroidUtils.getfileContent(context, AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String toggleStatus = AndroidUtils.getfileContent(context, AndroidUtils.TOGGLE_STATUS_FILE, AndroidUtils.TOGGLE_STATUS_ENABLED);
        Integer TIMER = Integer.parseInt(AndroidUtils.getTimer(context));//60 min

        //Getting current location of user.
        AndroidUtils.getCurrentLocation(context);
        // if message is not sent and the toggle button is enabled.
        if (msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N)
                && toggleStatus.equalsIgnoreCase(AndroidUtils.TOGGLE_STATUS_ENABLED)) {

            //If the application is running for the first time, add timer to zero.
            //AndroidUtils.getfileContent(context, AndroidUtils.TIMER_TO_SUBTRACT, "0");
            String timerToSubtract= AndroidUtils.getfileContent(context, AndroidUtils.TIMER_TO_SUBTRACT, "0");

            //Checking if the timer is completed.
            TIMER = TIMER - Integer.valueOf(timerToSubtract);

            if (TIMER < 0 ){
                TIMER=0;
            }

            AndroidUtils.getfileContent(context, AndroidUtils.TIMER_STARTED_ON, String.valueOf(SystemClock.elapsedRealtime()));

            // stating the intent to run after the timer is set.
            Intent alramReceiverIntent = new Intent(context, Alarm_Receiver.class);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // pending intent that is used when the time is met.
            PendingIntent pending = PendingIntent.getBroadcast(context, 0, alramReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //sets the alarm for particular time such that the pending intent can start running.
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
                    + TIMER, pending);
            Log.w("SalesTrackerData:", "BootComplete.Timer=" + TIMER);
        }else{
            Log.w("SalesTraBootCompletion", "BootComplete msgSentStatus=>" + msgSentStatus + "|toggleStatus=>" + toggleStatus);
        }
    }
}