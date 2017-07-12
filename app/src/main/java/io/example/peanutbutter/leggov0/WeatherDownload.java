package io.example.peanutbutter.leggov0;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Samuel on 9/07/2017.
 */

public class WeatherDownload extends AsyncTask<String, Void, String> {

    public static final String TAG = "WeatherDownload";
    public static Context mContext;
    RecyclerAdapter.ViewHolder mHolder;

    public WeatherDownload(RecyclerAdapter.ViewHolder holder) {
        mHolder = holder;
    }

    @Override
    protected String doInBackground(String... params) {
        //Log.d(TAG, "doInBackground: ");
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
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPostExecute(String result) {

        //Log.d(TAG, "onPostExecute: " + result);
        super.onPostExecute(result);

        // Time to do some parsing
        try {
            //Log.d(TAG, "onPostExecute: Try statement");
            JSONObject jsonObject = new JSONObject(result);
            //Log.d(TAG, "onPostExecute listData: " + jsonObject.getString("list"));

            // Get JSON Array
            JSONArray listData = jsonObject.getJSONArray("list");

            for (int i = 0; i < listData.length(); i++) {
                JSONObject dataind = listData.getJSONObject(i);
                // Get Temperature JSONobject
                JSONObject temp = dataind.getJSONObject("temp");
                String dayTemp = temp.getString("day");
                //Log.d(TAG, "onPostExecute: dayTemp is " + dayTemp);
                String nightTemp = temp.getString("night");
                //Log.d(TAG, "onPostExecute: nightTemp is " + nightTemp);

                // Parse weather from Weather JSONobject
                JSONArray weather = dataind.getJSONArray("weather");
                for (int y = 0; y < weather.length(); y++) {
                    JSONObject weatherInd = weather.getJSONObject(y);
                    //Log.d(TAG, "onPostExecute: Sucess_2");
                    String weatherMain = weatherInd.getString("main");
                    //Log.d(TAG, "onPostExecute: Weather is " + weatherMain);
                    setWeatherForecast(weatherMain);
                    RecyclerAdapter.weathercallfinish = true;
                }
            }


        } catch (JSONException e) {
            //Log.d(TAG, "onPostExecute: Exception");
            e.printStackTrace();
        }
    }


    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    public static void setmContext(Context mContext) {
        WeatherDownload.mContext = mContext;
    }

    public void setWeatherForecast(String weatherMain) {
        // Set forecast icons
        ImageView mImageView = new ImageView(mContext);
        switch (weatherMain) {
            case "Clear":
                mImageView.setImageResource(R.drawable.ic_sunny);
                break;
            case "Clouds":
                mImageView.setImageResource(R.drawable.ic_cloudy);
                break;
            case "Snow":
                mImageView.setImageResource(R.drawable.ic_snow);
                break;
            case "Rain":
                mImageView.setImageResource(R.drawable.ic_rain);
                break;
        }

        /*Resources r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, r.getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(px, px);
        mImageView.setLayoutParams(layoutParams);*/

        //mHolder.mGridLayoutWeather.addView(mImageView);

    }
}
