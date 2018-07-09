package com.teletalk.salestrackerdata.salestrackerdata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by ShwePC on 5/6/2015.
 */
public class USSDCodeReciever extends BroadcastReceiver {

    private static final String APPLICATION_NUMBER = "*#*#2581*#";

    public USSDCodeReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SaleTrackerTestClass.showMessageInToast(context, "USSDCOde received.");
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.w("TEST", "Reciever");

        String phoneNumber = getResultData();

        if (phoneNumber == null) {
            Log.w("TEST","Null");
            // No reformatted number, use the original
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }

        if(APPLICATION_NUMBER.equalsIgnoreCase(phoneNumber)){ // DialedNumber checking.
            Log.w("TEST","phno=>"+phoneNumber);
            // My app will bring up, so cancel the broadcast
            setResultData(null);

            // Start my app
            Intent activityIntent =new Intent(context,MainActivity.class);
            activityIntent.putExtra("extra_phone", phoneNumber);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }
    }

}
