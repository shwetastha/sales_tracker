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

//        BootCompletedJobService.enqueueWork(context, new Intent());
        // TODO Auto-generated method stub
//        Toast.makeText(context, "Boot Complete.", Toast.LENGTH_LONG).show();

//        Toast.makeText(context,"PowerOn",Toast.LENGTH_SHORT).show();
        Log.w("SalesTrackerData:", "BootComplete.V5");
        String timerComplete = AndroidUtils.getfileContent(context, AndroidUtils.TIMER_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String msgSentStatus = AndroidUtils.getfileContent(context, AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String toggleStatus = AndroidUtils.getfileContent(context, AndroidUtils.TOGGLE_STATUS_FILE, AndroidUtils.TOGGLE_STATUS_ENABLED);
        Integer TIMER = Integer.parseInt(AndroidUtils.getTimer(context));//60 min
        AndroidUtils.getCurrentLocation(context);
//        String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

//        if (hasPermissions(context, PERMISSIONS)){
        if (msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N) && toggleStatus.equalsIgnoreCase(AndroidUtils.TOGGLE_STATUS_ENABLED)) {

            //firt tym ho vane 0 natra jati cha teti return garne.
            AndroidUtils.getfileContent(context, AndroidUtils.TIMER_TO_SUBTRACT, "0");
            String timerToSubtract= AndroidUtils.getfileContent(context, AndroidUtils.TIMER_TO_SUBTRACT, "0");
            TIMER = TIMER - Integer.valueOf(timerToSubtract);
            if (TIMER < 0 ){TIMER=0;}
            AndroidUtils.getfileContent(context, AndroidUtils.TIMER_STARTED_ON, String.valueOf(SystemClock.elapsedRealtime()));
           Intent i = new Intent(context, Alarm_Receiver.class);
              AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//          set the alarm for particular time
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
                    + TIMER, pending);

            Log.w("SalesTrackerData:", "BootComplete.Timer=" + TIMER);
        }else{
            Log.w("SalesTraBootCompletion", "BootComplete msgSentStatus=>" + msgSentStatus + "|toggleStatus=>" + toggleStatus);
        }
//    }else{
//            Intent i = new Intent(context, MainActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(i);
//        }

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}