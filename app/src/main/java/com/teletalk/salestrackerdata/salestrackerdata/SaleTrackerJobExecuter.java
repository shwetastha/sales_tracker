package com.teletalk.salestrackerdata.salestrackerdata;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

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

public class SaleTrackerJobExecuter extends AsyncTask<Void, Void, Boolean> {
    private static final boolean TEST = true;
    private static final String TEST_URL = "http://202.166.205.39/sales_tracker_test/insert.php";
    private static final String PRODUCTION_URL= "http://202.166.205.39/sales_tracker/insert.php";
    private static final String TRUE = "true";

    String imei, model, operator, country_iso, longitude, latitude, location;
    private final Context context;

    public SaleTrackerJobExecuter(Context context){
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        imei = AndroidUtils.getImei(context);
        model = AndroidUtils.getModel().replace("Colors", "");
        operator = AndroidUtils.getOperator(context);
        country_iso = AndroidUtils.getCountryIso(context);
        latitude = AndroidUtils.getLATITUDE(context);
        longitude = AndroidUtils.getLONGITUDE(context);
        location = AndroidUtils.getLocation(context);

        //TODO
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String response = "N/A";
        String msgSentStatus = AndroidUtils.getfileContent(context, AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String toggleStatus = AndroidUtils.getfileContent(context, AndroidUtils.TOGGLE_STATUS_FILE, AndroidUtils.TOGGLE_STATUS_ENABLED);

        Log.w("SalesTrackerData:", "msgSentStatus == N ==" + msgSentStatus);
        Log.w("SalesTrackerData:", "toggleStatus == Enabled ==  " + toggleStatus);
        Log.w("SalesTrackerData:", "location != N/A ==  " + location);

        if (msgSentStatus.equalsIgnoreCase(AndroidUtils.MSG_STATUS_N)
                && toggleStatus.equalsIgnoreCase(AndroidUtils.TOGGLE_STATUS_ENABLED)
                && AndroidUtils.simExists(context)
//                && AndroidUtils.isLocationServicesEnabled(location, context)
                ) {
            response = login();
            Log.w("SalesTrackerData", "Response= " + response);
            SaleTrackerTestClass.showMessageInToast(context, "Sent to Server= "+response);
            if (TRUE.equalsIgnoreCase(response)) {
                AndroidUtils.dataSentTrigger(context);
                return true;
            }
                /*Intent service = new Intent(context, Network.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(service);
                } else {
                    context.startService(service);
                }*/
        }
        return false;
    }

    public String login() {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = null;
        if(SaleTrackerJobExecuter.TEST){
            httppost = new HttpPost(TEST_URL);
        }else{
            httppost = new HttpPost(PRODUCTION_URL);
        }

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
            Log.w("SalesTrackerData", "response= " + response);
            return response;
        } catch (Exception e) {
            Log.w("SalesTrackerData", "ExceptionOccured= " + e.toString());
            return e.toString();
        }
    }
}
