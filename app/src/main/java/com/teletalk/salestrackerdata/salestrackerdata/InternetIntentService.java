package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

//import android.widget.Toast;

public class InternetIntentService extends JobService {

    private static final int JOB_NETWORK_ID = 23487;
    private SaleTrackerJobExecuter saleTrackerJobExecuter;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        saleTrackerJobExecuter = new SaleTrackerJobExecuter(getApplicationContext()){

            @Override
            protected void onPostExecute(Boolean success) {
                if(!success){
                    ComponentName componentName = new ComponentName(getApplicationContext(), Network.class);
                    JobInfo.Builder builder = new JobInfo.Builder(JOB_NETWORK_ID, componentName);
                    builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                    jobInfo = builder.build();
                    jobScheduler = ( JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
                    jobScheduler.schedule(jobInfo);
                }
                jobFinished(jobParameters, false);
            }
        };

        saleTrackerJobExecuter.execute();
        //Sincing the task is running on a different thread return true.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //If its interrupted.
        saleTrackerJobExecuter.cancel(true);
        ComponentName componentName = new ComponentName(getApplicationContext(), Network.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_NETWORK_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        jobInfo = builder.build();
        jobScheduler = ( JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
        return false;
    }
}