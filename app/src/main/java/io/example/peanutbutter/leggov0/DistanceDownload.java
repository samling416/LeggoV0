package io.example.peanutbutter.leggov0;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Samuel on 18/07/2017.
 */

public class DistanceDownload extends AsyncTask<String, Void, String> {

    public static final String TAG = "DistanceDownload";
    RecyclerAdapter.ViewHolder mHolder;
    public static Context mContext;


    private String mDuration;
    private String mDistance;

    public DistanceDownload(RecyclerAdapter.ViewHolder holder) {
        this.mHolder = holder;
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground: ");
        String result = "";
        URL url;
        HttpURLConnection mURLConnection = null;

        try {

            //Read input url
            url = new URL(params[0]);
            // Open connection
            mURLConnection = (HttpURLConnection) url.openConnection();
            // Get input stream from connection, read it and store in result string
            InputStream in = mURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            // Data will equal -1 when reader has been finished reading.
            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            // Return JSON file
            //Log.d(TAG, "doInBackground: " + result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPostExecute(String result) {

        Log.d(TAG, "onPostExecute: " + result);

        super.onPostExecute(result);

        // Time to do some parsing
        try {
            //Log.d(TAG, "onPostExecute: Try statement");
            JSONObject jsonObject = new JSONObject(result);

            // Get JSON Array
            //Log.d(TAG, "onPostExecute listData: " + jsonObject.getString("routes"));
            JSONArray listData = jsonObject.getJSONArray("routes");

            // Parse JSON Array
            for (int i = 0; i < listData.length(); i++) {
                JSONObject dataind = listData.getJSONObject(i);
                Log.d(TAG, "onPostExecute: bounds is " + dataind.getString("legs"));
                JSONArray legs = dataind.getJSONArray("legs");
                for (int y = 0; y < legs.length(); y ++){
                    JSONObject legsind = legs.getJSONObject(i);
                    //Log.d(TAG, "onPostExecute: distnace is " + legsind.get("distance") );
                    JSONObject distance = legsind.getJSONObject("distance");
                    JSONObject duration = legsind.getJSONObject("duration");
                    Log.d(TAG, "onPostExecute: Distance is " + distance.getString("text"));
                    Log.d(TAG, "onPostExecute: Duration is " + duration.getString("text"));
                    mDistance = distance.getString("text");
                    mDuration = duration.getString("text");
                }

                mHolder.mTimeTextView.setText(mDuration);
                mHolder.mDistTextView.setText(mDistance);
            }

        } catch (
                JSONException e)

        {
            //Log.d(TAG, "onPostExecute: Exception");
            e.printStackTrace();
        }
    }
}
