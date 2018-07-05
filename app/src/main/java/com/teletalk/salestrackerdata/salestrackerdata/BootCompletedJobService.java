package com.teletalk.salestrackerdata.salestrackerdata;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

/**
 * Created by ShwePC on 6/7/2018.
 */

public class BootCompletedJobService extends JobIntentService {

    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
        Log.w("SalesTraBootCompletion", "BootCompletedJobService=>enqueueWork");
        if (Intent.ACTION_BOOT_COMPLETED.equals(work.getAction())) {
            Log.w("SalesTrackerData:", "BootComplete.V5.enque");
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
                enqueueWork(context, BootCompletedJobService.class, JOB_ID, work);

                Log.w("SalesTrackerData:", "BootComplete.Timer=" + TIMER);
            }else{
                Log.w("SalesTraBootCompletion", "BootComplete msgSentStatus=>" + msgSentStatus + "|toggleStatus=>" + toggleStatus);
            }


        }else {
                int status = NetworkUtil.getConnectivityStatusString(context);
                String msgSentStatus = AndroidUtils.getfileContent(context, AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);

                if (status == 1 && msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N)
                        && AndroidUtils.isSimConnected(context)) {
                    Log.w("SalesTrackerData:", "BootCompletedJobService:=>InternetJobIntentService");
                    Log.w("SalesTrackerData:", "Network: Internet is Working.");
                    InternetJobIntentService.enqueueWork(context, new Intent());

//                Toast.makeText(context, "Network: Internet is Working.", Toast.LENGTH_LONG).show();
//                    Intent service = new Intent(context, InternetIntentService.class);
//                    context.startService(service);
                }
        }
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Context context = getApplicationContext();
        // your code
        Log.w("SalesTraBootCompletion", "BootCompletedJobService=>onHandleWork");
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
}
