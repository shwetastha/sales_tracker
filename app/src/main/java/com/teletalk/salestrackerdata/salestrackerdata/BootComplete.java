package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
//import android.widget.Toast;


public class BootComplete extends BroadcastReceiver {
    private static final int JOB_NETWORK_ID = 13487;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;

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
        //AndroidUtils.getCurrentLocation(context);
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
            AndroidUtils.getCurrentLocation(context);

//            ComponentName componentName = new ComponentName(context, SaleTrackerLocationService.class);
//            JobInfo.Builder builder = new JobInfo.Builder(JOB_NETWORK_ID, componentName);
//            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//            builder.setPeriodic(1*1000);
//            jobInfo = builder.build();
//            jobScheduler = ( JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
//            jobScheduler.schedule(jobInfo);
        }else{
            Log.w("SalesTraBootCompletion", "BootComplete msgSentStatus=>" + msgSentStatus + "|toggleStatus=>" + toggleStatus);
        }
    }
}