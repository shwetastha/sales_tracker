package com.teletalk.salestrackerdata.salestrackerdata;

import android.widget.Toast;
import android.content.Context;

public class SaleTrackerTestClass {

    public static final boolean TEST = false;

    /*
        This method shows toast while debugging the code.
     */
    public static void showMessageInToast(Context context, String message){
        if(TEST){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
