package com.teletalk.salestrackerdata.salestrackerdata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by ShwePC on 5/6/2015.
 */
public class USSDCodeReciever extends BroadcastReceiver {
    public USSDCodeReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.w("TEST", "Reciever");
        String phoneNumber = getResultData();
        if (phoneNumber == null) {
            Log.w("TEST","Null");

            // No reformatted number, use the original
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }

        if(phoneNumber.equals("*#*#2581*#")){ // DialedNumber checking.
            Log.w("TEST","phno=>"+phoneNumber);

            // My app will bring up, so cancel the broadcast
            setResultData(null);

            // Start my app
            Intent i=new Intent(context,MainActivity.class);
            i.putExtra("extra_phone", phoneNumber);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}
