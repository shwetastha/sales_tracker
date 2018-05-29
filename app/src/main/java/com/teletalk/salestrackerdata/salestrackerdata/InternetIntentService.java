package com.teletalk.salestrackerdata.salestrackerdata;

/**
 * Created by ShwePC on 5/20/2015.
 */

import android.annotation.TargetApi;
import android.app.IntentService;
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

public class InternetIntentService extends IntentService {

    String imei, model, operator, country_iso, longitude, latitude, location;

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public InternetIntentService() {
        super("InternetItentService");
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onHandleIntent(Intent arg0) {
        Log.w("SalesTrackerIntent:", "Network: Internet is Working.");

        imei = AndroidUtils.getImei(getApplicationContext());
        model = AndroidUtils.getModel().replace("Colors", "");
        operator = AndroidUtils.getOperator(getApplicationContext());
        country_iso = AndroidUtils.getCountryIso(getApplicationContext());
        latitude = AndroidUtils.getLATITUDE(getApplicationContext());
        longitude = AndroidUtils.getLONGITUDE(getApplicationContext());
        location = AndroidUtils.getLocation(getApplicationContext());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String response;
        String msgSentStatus = AndroidUtils.getfileContent(getApplicationContext(), AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String toggleStatus = AndroidUtils.getfileContent(getApplicationContext(), AndroidUtils.TOGGLE_STATUS_FILE, AndroidUtils.TOGGLE_STATUS_ENABLED);
        Log.w("SalesTrackerData:", "msgSentStatus == N ==" + msgSentStatus);
        Log.w("SalesTrackerData:", "toggleStatus == Enabled ==  " + toggleStatus);
        Log.w("SalesTrackerData:", "location != N/A ==  " + location);
//        Toast.makeText(getApplicationContext(), "address= "+location, Toast.LENGTH_LONG).show();

        if (msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N)
                && toggleStatus.equalsIgnoreCase(AndroidUtils.TOGGLE_STATUS_ENABLED)
                && AndroidUtils.simExists(getApplicationContext())
                && !location.equalsIgnoreCase("N/A")) {

            response = login();
            Log.w("SalesTrackerData:", "opearator " + operator);

            if (response.equalsIgnoreCase("true")) {
                AndroidUtils.dataSentTrigger(getApplicationContext());
            } else {
                Intent service = new Intent(getApplicationContext(), Network.class);
                getApplicationContext().startService(service);
            }
            Log.w("SalesTrackerData", "Response= " + response);
        }
    }

    public String login() {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://202.166.205.39/sales_tracker_test/insert.php");
//        HttpPost httppost = new HttpPost("http://192.168.37.1/Sales_Tracker/insert.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("imei", imei));
            nameValuePairs.add(new BasicNameValuePair("model", model));
            nameValuePairs.add(new BasicNameValuePair("operator", operator));
            nameValuePairs.add(new BasicNameValuePair("country_iso", country_iso));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
            nameValuePairs.add(new BasicNameValuePair("location", location));
            Log.w("SalesTrackerData", "imei= " + imei + ", model=" + model);
            Log.w("SalesTrackerData", "latitude= " + latitude + ", longitude=" + longitude);
            Log.w("SalesTrackerData", "location= " + location );

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.w("SalesTrackerData", "httppost= " + httppost);

            // Execute HTTP Post Request
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            return response;
        } catch (Exception e) {
            Log.w("SalesTrackerData", "ExceptionOccured= " + e.toString());
            return e.toString();
        }
    }
}