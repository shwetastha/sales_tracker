package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Network extends Service {

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            int status = NetworkUtil.getConnectivityStatusString(context);
            String msgSentStatus = AndroidUtils.getfileContent(context, AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
            Toast.makeText(getApplication(), "Network class pugyo", Toast.LENGTH_LONG).show();

            if (status == 1 && msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N) && AndroidUtils.isSimConnected(context)) {
                Toast.makeText(context, "Internet is working", Toast.LENGTH_LONG).show();
                Log.w("SalesTrackerData:", "Network: Internet is Working.");
//                Toast.makeText(context, "network class ko if ni pugyo", Toast.LENGTH_LONG).show();

                Intent service = new Intent(context, InternetIntentService.class);
                context.startService(service);
//                new InternetAsyncTask().execute();
            }

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        final IntentFilter rece = new IntentFilter();
        {
            rece.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            rece.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        }


        registerReceiver(receiver, rece);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
