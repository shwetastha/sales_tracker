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
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

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

    protected static String TIMER = "60";//in minutes

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
                Log.w("SalesTracker:AndroidUtils", "Get File File created " + filename + ".txt value " + temp);

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
            Log.w("SalesTracker:AndroidUtils", "Write To File " + key + ".txt value " + value);

        } catch (IOException e) {
            e.printStackTrace();
            Log.w("SalesTracker:AndroidUtils", "Excpetion occured. Write To File " + key + ".txt value " + value + ". Exception= " + e.toString());

        } finally {
            file.flush();
            file.close();
        }
    }

    protected static String getSpectrumImeiNo(){
        File imei1 = new File("/sdcard/.IMEI1.txt");
        File imei2 = new File("/sdcard/.IMEI2.txt");

        try {
            imei1.createNewFile();
            imei2.createNewFile();
            FileOutputStream fOut = new FileOutputStream(imei1);
            FileOutputStream fOut2 = new FileOutputStream(imei2);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            OutputStreamWriter myOutWriter2 =
                    new OutputStreamWriter(fOut2);
            myOutWriter.close();
            myOutWriter2.close();
            fOut.close();
            fOut2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Exception";
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception";

        }


        Process p;
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
            outputStream.writeBytes("dumpsys iphonesubinfo0 | grep 'Device ID' > /sdcard/.IMEI1.txt\n");
            outputStream.writeBytes("dumpsys iphonesubinfo1 | grep 'Device ID' > /sdcard/.IMEI2.txt\n");
            outputStream.flush();
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
        }catch(IOException e){
            try {
                throw new Exception(e);
            } catch (Exception e1) {
                e1.printStackTrace();
                return "Exception";

            }
        }catch(InterruptedException e){
            try {
                throw new Exception(e);
            } catch (Exception e1) {
                e1.printStackTrace();
                return "Exception";

            }
        }

        try {
            FileInputStream fIn = new FileInputStream(imei1);
//            FileInputStream fIn2 = new FileInputStream(imei2);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
//            BufferedReader myReader2 = new BufferedReader(
//                    new InputStreamReader(fIn2));
            String temp = "";
            String c = "";
            while ((c = myReader.readLine()) != null) {
                temp += c;
            }
//            String str_imei1 = temp.substring(temp.indexOf("Device ID ="),15);
            String temp2 = "";
            String c2 = "";
            while ((c2 = myReader.readLine()) != null) {
                temp2 += c2;
            }
//            String str_imei2 = temp2.substring(temp2.indexOf("Device ID ="),15);
            //string temp contains all the data of the file.
//            tv.setText("imei1=" + temp.replace("Device ID = ", "") + "\n==imei2=" + temp2.replace("Device ID = ", ""));
            myReader.close();
            return temp.replace("Device ID = ", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Exception";
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception";
        }
    }

    protected static String getImei(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            return tm.getDeviceId(0);
        } catch (NoClassDefFoundError e) {
            try{
                if (getSpectrumImeiNo().equalsIgnoreCase("Exception"))
                    throw new Exception(e);
                else
                    return getSpectrumImeiNo() ;
            }catch (Exception e1){
                TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
                return tm.getDeviceId();
            }
        }
    }

    protected static String getOperator(Context c) {
//        try {
//            TelephonyManager tm = (TelephonyManager) c.getSystemService(c.TELEPHONY_SERVICE);
//            if (tm.getPhoneCount()==2){
//
//            }
//
//                    if (tm.getSimState(0) == TelephonyManager.SIM_STATE_READY)
//                        return tm.getSimOperatorName(0);
//
//                    if (tm.getSimState(1) == TelephonyManager.SIM_STATE_READY)
//                        return tm.getSimOperatorName(1);
//
//
//
//            return "";
//        } catch (NoClassDefFoundError e) {
            TelephonyManager tm = (TelephonyManager) c.getSystemService(c.TELEPHONY_SERVICE);
            if (tm.getSimState() == TelephonyManager.SIM_STATE_READY)
                return tm.getSimOperatorName();

            return "";
//        }

    }

    protected static boolean simExists(Context c) {
//        try {
//            TelephonyManager tm = (TelephonyManager) c.getSystemService(c.TELEPHONY_SERVICE);
//            if (tm.getSimState(0) == TelephonyManager.SIM_STATE_READY
//                    || tm.getSimState(1) == TelephonyManager.SIM_STATE_READY)
//                return true;
//            else
//                return false;
//        } catch (NoClassDefFoundError e) {
            TelephonyManager tm = (TelephonyManager) c.getSystemService(c.TELEPHONY_SERVICE);
            if (tm.getSimState() == TelephonyManager.SIM_STATE_READY)
                return true;
            else
                return false;
//        }

    }

    protected static String getCountryIso(Context c) {
//        try {
//            TelephonyManager tm = (TelephonyManager) c.getSystemService(c.TELEPHONY_SERVICE);
//            if (tm.getSimState(0) == TelephonyManager.SIM_STATE_READY)
//                return tm.getSimCountryIso(0);
//            else if (tm.getSimState(1) == TelephonyManager.SIM_STATE_READY)
//                return tm.getSimCountryIso(1);
//            else
//                return "";
//        } catch (NoClassDefFoundError e) {
            TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm.getSimState() == TelephonyManager.SIM_STATE_READY)
                return tm.getSimCountryIso();
            else
                return "";
//        }
    }

    protected static boolean isSimConnected(Context c) {
//        try {
//            TelephonyManager tm = (TelephonyManager) c.getSystemService(c.TELEPHONY_SERVICE);
//            if (!tm.getSimOperator(0).equalsIgnoreCase("") || !(tm.getSimOperator(0) == null))
//                return true;
//            else if (!tm.getSimOperator(1).equalsIgnoreCase("") || !(tm.getSimOperator(1) == null))
//                return true;
//            else
//                return false;
//        } catch (NoClassDefFoundError noClassDefFoundError) {
            TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            if (!tm.getSimOperator().equalsIgnoreCase("") || !(tm.getSimOperator() == null))
                return true;
            else
                return false;
//        }
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


    public static void setTIMER(Context c) throws IOException {
        AndroidUtils.setToFile(c, "timer", TIMER);

    }

    public static void setTIMER(String timer, Context c) throws IOException {
        AndroidUtils.setToFile(c, "timer", timer);
    }

    protected static String getTimer(Context c) {
        //            Toast.makeText(c, "Timer="+obj.getString("timer"),Toast.LENGTH_LONG).show();
        String timerString = getfileContent(c, "timer", TIMER);
        Log.w("SalesTrackerSMS", "Timer=" + TIMER);

        int timerInt = Integer.parseInt(timerString) * 1000 * 60;
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

            Log.w("SalesTracker:AndroidUtils", "Already Exists!!Set To File " + key + ".txt with value " + value);
        } catch (IOException e) {
            try {
                FileOutputStream f = context.openFileOutput(key + ".txt", Context.MODE_PRIVATE);
                f.write(value.getBytes());
                f.close();
                Log.w("SalesTracker:AndroidUtils", "Created and Set To File " + key + ".txt with value " + value);
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
            Log.w("SalesTracker:AndroidUtils", "Get File: " + filename + ".txt value " + temp);
            return temp;

        } catch (Exception e) {
            Log.w("SalesTracker:AndroidUtils", "Get File : " + filename + ".txt value " + temp + ". Exception " + e.toString());
            return temp;
        }
    }


}
