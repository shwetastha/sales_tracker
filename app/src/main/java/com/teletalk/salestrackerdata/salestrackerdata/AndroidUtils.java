package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mediatek.telephony.SmsManagerEx;
import com.mediatek.telephony.TelephonyManagerEx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AndroidUtils {
    protected static String TOGGLE_STATUS_FILE = "toggle";
    protected static String TOGGLE_STATUS_ENABLED = "Enabled";
    protected static String TOGGLE_STATUS_DISABLED = "Disabled";

    protected static String TIMER_STATUS_FILE = "timerComplete";
    protected static String MSG_STATUS_FILE = "test";
    protected static String MSG_STATUS_N = "n";
    protected static String MSG_STATUS_Y = "y";

    protected static String TIMER_TO_SUBTRACT = "timerToSub";
    protected static String TIMER_STARTED_ON = "timerStartedOn";

    protected static String TIMER = "1";//in minutes

    protected static void create_file(Context context) {
        try {
//            Toast.makeText(context, "CreateFile", Toast.LENGTH_LONG).show();
            Log.w("SalesTrackerData:", "AndroidUtils: CreateFile");

            FileOutputStream f = context.openFileOutput("test.txt", Context.MODE_PRIVATE);
            f.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
//            Toast.makeText(context, "FileNotFound", Toast.LENGTH_LONG).show();
            Log.w("SalesTrackerData:", "AndroidUtils: FilenotFound");

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
//            Toast.makeText(context, "IOException", Toast.LENGTH_LONG).show();
            Log.w("SalesTrackerData:", "AndroidUtils: IOException");

            e.printStackTrace();
        }

    }

    protected static String getfileContent(Context context, String filename, String fileContent) {

        String temp = "";
        try {
            FileInputStream fin = context.openFileInput(filename + ".txt");
            int c;
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            //string temp contains all the data of the file.
            fin.close();
            Log.w("SalesTracker:AndroidUtils", "Get File " + filename + ".txt value " + temp);
            return temp;

        } catch (Exception e) {
            try {
                FileOutputStream f = context.openFileOutput(filename + ".txt", Context.MODE_PRIVATE);
                f.write(fileContent.getBytes());
                Log.w("SalesTracker:AndroidUtils", "Get File File created " + filename + ".txt value "+temp);

                f.close();
//                Toast.makeText(context, "File is created for the first time", Toast.LENGTH_LONG).show();
                return fileContent;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        return temp;

    }

    protected static void writeToFile(Context context, String key, String value) throws IOException {
        FileOutputStream file = context.openFileOutput(key + ".txt", Context.MODE_PRIVATE);
        try {
            file.write(value.getBytes());
            Log.w("SalesTracker:AndroidUtils", "Write To File " + key + ".txt value "+value);

        } catch (IOException e) {
            e.printStackTrace();
            Log.w("SalesTracker:AndroidUtils", "Excpetion occured. Write To File " + key + ".txt value "+value+". Exception= "+e.toString());

        } finally {
            file.flush();
            file.close();
        }
    }

    protected static String getImei(Context context) {
        TelephonyManagerEx tm = new TelephonyManagerEx(context);
        return tm.getDeviceId(0);
    }

    protected static String getOperator(Context c){
        TelephonyManagerEx tm = new TelephonyManagerEx(c);
//        tm.getDataActivity(0)
        if (!tm.getSimOperator(0).equalsIgnoreCase("")||!(tm.getSimOperator(0)==null))
            return tm.getSimOperatorName(0);
        else if (!tm.getSimOperator(1).equalsIgnoreCase("")||!(tm.getSimOperator(1)==null))
            return tm.getSimOperatorName(1);
        else
            return "";
    }
    protected static boolean simExists(Context c){
        TelephonyManagerEx tm = new TelephonyManagerEx(c);
//        tm.getDataActivity(0)
        if (tm.getSimState(0)==TelephonyManager.SIM_STATE_READY
                || tm.getSimState(1)==TelephonyManager.SIM_STATE_READY )
            return true;
        else
            return false;
    }

    protected static String getCountryIso(Context c){
        TelephonyManagerEx tm = new TelephonyManagerEx(c);
//        tm.getDataActivity(0)
        if (tm.getSimState(0)== TelephonyManager.SIM_STATE_READY)
            return tm.getSimCountryIso(0);
        else if (tm.getSimState(1)== TelephonyManager.SIM_STATE_READY)
            return tm.getSimCountryIso(1);
        else
            return "";
    }

    protected static boolean isSimConnected(Context c){
        TelephonyManagerEx tm = new TelephonyManagerEx(c);
        if (!tm.getSimOperator(0).equalsIgnoreCase("")||!(tm.getSimOperator(0)==null))
            return true;
        else if (!tm.getSimOperator(1).equalsIgnoreCase("")||!(tm.getSimOperator(1)==null))
            return true;
        else
            return false;
    }


    protected static String getModel() {
        return Build.MODEL;
    }

    protected static void dataSentTrigger(Context context) {
        try {
            FileOutputStream fTest = context.openFileOutput(AndroidUtils.MSG_STATUS_FILE + ".txt", Context.MODE_PRIVATE);
            FileOutputStream fToggle = context.openFileOutput(AndroidUtils.TOGGLE_STATUS_FILE + ".txt", Context.MODE_PRIVATE);
            String temp = AndroidUtils.MSG_STATUS_Y;
            String disabled = AndroidUtils.TOGGLE_STATUS_DISABLED;
            fTest.write(temp.getBytes());
            fToggle.write(disabled.getBytes());
            fTest.close();
            fToggle.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static void setTIMER(Context c) throws IOException{
        AndroidUtils.setToFile(c, "timer", TIMER);

    }
    public static void setTIMER(String timer, Context c) throws IOException {
        AndroidUtils.setToFile(c, "timer", timer);
    }

    protected static String getTimer(Context c) {
        //            Toast.makeText(c, "Timer="+obj.getString("timer"),Toast.LENGTH_LONG).show();
        String timerString = getfileContent(c, "timer", TIMER);
        Log.w("SalesTrackerSMS", "Timer="+TIMER);

        int timerInt = Integer.parseInt(timerString) * 1000 *60;
        Log.w("SalesTrackerSMS", "Timer=" + String.valueOf(timerInt));
        return String.valueOf(timerInt);
    }
    protected static String getTimerHome(Context c) {
        //            Toast.makeText(c, "Timer="+obj.getString("timer"),Toast.LENGTH_LONG).show();
        return getfileContent(c, "timer", TIMER);
    }

    protected static void setToFile(Context context, String key, String value) throws IOException {
        try {
            FileInputStream fin = context.openFileInput(key + ".txt");
            fin.close();

            Log.w("SalesTracker:AndroidUtils", "Already Exists!!Set To File " + key + ".txt with value "+value);
        } catch (IOException e) {
            try {
                FileOutputStream f = context.openFileOutput(key + ".txt", Context.MODE_PRIVATE);
                f.write(value.getBytes());
                f.close();
                Log.w("SalesTracker:AndroidUtils", "Created and Set To File " + key + ".txt with value "+value);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    protected static String getfile(Context context, String filename, String fileContent) {

        String temp = "0";
        try {
            FileInputStream fin = context.openFileInput(filename + ".txt");
            int c;
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            //string temp contains all the data of the file.
            fin.close();
            Log.w("SalesTracker:AndroidUtils", "Get File: " + filename + ".txt value "+temp);
            return temp;

        } catch (Exception e) {
            Log.w("SalesTracker:AndroidUtils", "Get File : " + filename + ".txt value "+temp+". Exception "+e.toString());
            return temp;
        }
    }


}
