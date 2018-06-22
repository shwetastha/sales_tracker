package com.teletalk.salestrackerdata.salestrackerdata;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

/**
 * Created by ShwePC on 6/7/2018.
 */

public class BootCompletedJobService extends JobIntentService {

    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, BootCompletedJobService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // your code
    }

}
