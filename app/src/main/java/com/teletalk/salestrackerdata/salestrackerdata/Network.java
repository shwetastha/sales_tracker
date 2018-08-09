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

import static android.content.ContentValues.TAG;

public class Network extends JobService {

    private static final int JOB_DATABASE_WRITE_ID = 3478;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;


    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        // Calling the service class.
        ComponentName componentName = new ComponentName(getApplicationContext(), InternetIntentService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_DATABASE_WRITE_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        jobInfo = builder.build();
        jobScheduler = ( JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters){
        return false;
    }
}
