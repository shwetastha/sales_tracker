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

    private static final int JOB_DATABASE_WRITE_ID = 3478;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;
    private BroadcastReceiver receiver;


    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.e(TAG, "network CLass Onstartjob: ");

        final IntentFilter intentFilters = new IntentFilter();
        intentFilters.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilters.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                int status = NetworkUtil.getConnectivityStatusString(context);
                String msgSentStatus = AndroidUtils.getfileContent(context, AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
                SaleTrackerTestClass.showMessageInToast(context, "Network: in");
                Log.w("SalesTrackerData:", "Network: In");

                Log.w("SalesTrackerData:", "Network: in");
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

                    unregisterReceiver(receiver);
                    jobFinished(jobParameters, false);


                }
            }
        };

//        jobFinished(jobParameters, false);
        registerReceiver(receiver, intentFilters);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters){
        return false;
    }
}
