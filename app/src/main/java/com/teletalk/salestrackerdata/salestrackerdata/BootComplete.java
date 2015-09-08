package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
//import android.widget.Toast;

import java.util.Calendar;

public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
//        Toast.makeText(context, "Boot Complete.", Toast.LENGTH_LONG).show();

//        Toast.makeText(context,"PowerOn",Toast.LENGTH_SHORT).show();
        Log.w("SalesTrackerData:", "BootComplete.");
        String timerComplete = AndroidUtils.getfileContent(context, AndroidUtils.TIMER_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String msgSentStatus = AndroidUtils.getfileContent(context, AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String toggleStatus = AndroidUtils.getfileContent(context, AndroidUtils.TOGGLE_STATUS_FILE, AndroidUtils.TOGGLE_STATUS_ENABLED);
        Integer TIMER = Integer.parseInt(AndroidUtils.getTimer(context));//60 min

        if (msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N) && toggleStatus.equalsIgnoreCase(AndroidUtils.TOGGLE_STATUS_ENABLED)) {

            //firt tym ho vane 0 natra jati cha teti return garne.
            AndroidUtils.getfileContent(context, AndroidUtils.TIMER_TO_SUBTRACT, "0");
            String timerToSubtract= AndroidUtils.getfileContent(context, AndroidUtils.TIMER_TO_SUBTRACT, "0");
            TIMER = TIMER - Integer.valueOf(timerToSubtract);
            if (TIMER < 0 ){TIMER=0;}
            AndroidUtils.getfileContent(context, AndroidUtils.TIMER_STARTED_ON, String.valueOf(SystemClock.elapsedRealtime()));
           Intent i = new Intent(context, Alarm_Receiver.class);
//
              AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//          set the alarm for particular time
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
                    + TIMER, pending);

            Log.w("SalesTrackerData:", "BootComplete.Timer=" + TIMER);

            //Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.MILLISECOND, TIMER);
            //Intent i = new Intent(context, Alarm_Receiver.class);
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
  //          PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//            set the alarm for particular time
    //        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pending);



        }else{
            Log.w("SalesTracker:Boot_Completion_Receiver", "BootComplete msgSentStatus=>" + msgSentStatus + "|toggleStatus=>" + toggleStatus);
        }

    }

}