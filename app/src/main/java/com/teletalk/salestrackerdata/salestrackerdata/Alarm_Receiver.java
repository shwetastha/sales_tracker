package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class Alarm_Receiver extends BroadcastReceiver {

    private static final int JOB_NETWORK_ID = 23487;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;

    @Override
    public void onReceive(Context context, Intent intent) {

        SaleTrackerTestClass.showMessageInToast(context, "Alram recived after the timer was set.");
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.w("SalesTrackerData:", "AlarmReciever"+pInfo.versionName);

        String timerComplete = AndroidUtils.getfileContent(context, AndroidUtils.TIMER_STATUS_FILE, AndroidUtils.MSG_STATUS_N);

        SaleTrackerTestClass.showMessageInToast(context, "Timer Finished");

        ComponentName componentName = new ComponentName(context, Network.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_NETWORK_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        jobInfo = builder.build();
        jobScheduler = ( JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

}
