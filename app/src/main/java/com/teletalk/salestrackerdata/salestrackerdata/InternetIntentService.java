package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Application;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
//import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InternetIntentService extends IntentService {

    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    String imei, model,operator,country_iso, time;
    Button b;
    int a = 0;

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public InternetIntentService() {
        super("InternetItentService");
        // TODO Auto-generated constructor stub
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onHandleIntent(Intent arg0) {
        // TODO Auto-generated method stub
//        Toast.makeText(getApplication(),"intentINtent", Toast.LENGTH_LONG).show();
        Log.w("SalesTrackerData:", "Network: Internet is Working.");

        imei = AndroidUtils.getImei(getApplicationContext());
        model = AndroidUtils.getModel().replace("Colors", "");
        operator = AndroidUtils.getOperator(getApplicationContext());
        country_iso = AndroidUtils.getCountryIso(getApplicationContext());
//        phno = AndroidUtils.getPhNo(getApplicationContext());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String response;
        String msgSentStatus = AndroidUtils.getfileContent(getApplicationContext(), AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String toggleStatus = AndroidUtils.getfileContent(getApplicationContext(), AndroidUtils.TOGGLE_STATUS_FILE, AndroidUtils.TOGGLE_STATUS_ENABLED);


        if (msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N)
                && toggleStatus.equalsIgnoreCase(AndroidUtils.TOGGLE_STATUS_ENABLED)
                && AndroidUtils.simExists(getApplicationContext()) ) {

            response = login();
//            Toast.makeText(getApplication(), "Data output=>"+response, Toast.LENGTH_LONG).show();
            Log.w("SalesTrackerData:", "opearator " + operator);

            if (response.equalsIgnoreCase("true")) {
                AndroidUtils.dataSentTrigger(getApplicationContext());
            } else {
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.SECOND, 10);
//                Intent i = new Intent(getApplicationContext(), Alarm_Receiver.class);
//                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//                //set the alarm for particular time
//                alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pending);

                Intent service = new Intent(getApplicationContext(), Network.class);
                getApplicationContext().startService(service);
            }
            Log.w("SalesTrackerData", "Response= " + response);
        }
    }

    public String login() {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://202.166.205.39/sales_tracker/insert.php");
//        HttpPost httppost = new HttpPost("http://192.168.37.1/Sales_Tracker/insert.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("imei", imei));
            nameValuePairs.add(new BasicNameValuePair("model", model));
            nameValuePairs.add(new BasicNameValuePair("operator", operator));
            nameValuePairs.add(new BasicNameValuePair("country_iso", country_iso));
//            nameValuePairs.add(new BasicNameValuePair("phno", phno));
            Log.w("SalesTrackerData", "imei= "+imei+", model="+model);

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.w("SalesTrackerData", "httppost= "+httppost);

            // Execute HTTP Post Request
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
//            Toast.makeText(getApplicationContext(),"InternetIntent Sent response= "+response, Toast.LENGTH_LONG).show();
            return response;
            //This is the response from a php application
        } catch (ClientProtocolException e) {
            Log.w("SalesTrackerData", "ExceptionOccured= "+e.toString());
            return e.toString();
        } catch (IOException e) {
            Log.w("SalesTrackerData", "ExceptionOccured= "+e.toString());
            return e.toString();
        }
    }
}