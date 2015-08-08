package com.teletalk.salestrackerdata.salestrackerdata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;

/**
 * Created by ShwePC on 3/14/2015.
 */
public class ShutDownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w("SalesTracker:ShutDownReciever", "Class");

        Integer prevTime = Integer.valueOf(AndroidUtils.getfile(context, AndroidUtils.TIMER_STARTED_ON, "0"));
        Integer curTime = Integer.valueOf(String.valueOf(SystemClock.elapsedRealtime()));
        Integer difference = curTime - prevTime;
        Log.w("SalesTracker:ShutDownReciever", "TimeDiff" + difference.toString());
        try {
            if (difference < 0) {
                AndroidUtils.writeToFile(context, AndroidUtils.TIMER_TO_SUBTRACT, "0");
            } else {
                AndroidUtils.writeToFile(context, AndroidUtils.TIMER_TO_SUBTRACT, difference.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String toggle = AndroidUtils.getfileContent(context, "binamra", "shutdown");

        //Log.w("TimerDiff", difference.toString());

    }
}
