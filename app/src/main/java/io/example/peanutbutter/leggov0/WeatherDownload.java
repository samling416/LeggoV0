package io.example.peanutbutter.leggov0;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by Samuel on 9/07/2017.
 */

public class WeatherDownload extends AsyncTask<String, Void, String> {

    public static final String TAG = "WeatherDownload";
    public static Context mContext;
    private int dayCounter = 1;
    private boolean mainWeatherCall = true;
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
                    if (mainWeatherCall) {
                        setMainWeatherForecast(weatherMain, dayTemp);
                    } else {
                        setWeatherForecast(weatherMain, dayCounter);
                    }
                }
            }


        } catch (
                JSONException e)

        {
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

    public void setMainWeatherForecast(String weatherMain, String temp) {
        // Set forecast icon
        Log.d(TAG, "setMainWeatherForecast: For" + mHolder.getAdapterPosition() );
        switch (weatherMain) {
            case "Clear":
                mHolder.mWeatherMainImageView.setImageResource(R.drawable.main_sunny);
                break;
            case "Clouds":
                mHolder.mWeatherMainImageView.setImageResource(R.drawable.main_cloudy);
                break;
            case "Snow":
                mHolder.mWeatherMainImageView.setImageResource(R.drawable.main_snow);
                break;
            case "Rain":
                mHolder.mWeatherMainImageView.setImageResource(R.drawable.main_rain);
                break;
        }

        mHolder.mWeatherMainTemp.setText(temp + "°C");
        mHolder.mWeatherMainDesc.setText(weatherMain);
        mainWeatherCall = false;
        /*Resources r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, r.getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(px, px);
        mImageView.setLayoutParams(layoutParams);*/

    }

    public void setWeatherForecast(String weatherMain, int position) {
        // Set forecast icons
        //Log.d(TAG, "setWeatherForecast: " + position);
        CustomGridLayoutItem item = new CustomGridLayoutItem(mContext);
        switch (weatherMain) {
            case "Clear":
                ((ImageView) item.findViewById(R.id.gridlayout_image)).setImageResource(R.drawable.ic_sunny);
                break;
            case "Clouds":
                ((ImageView) item.findViewById(R.id.gridlayout_image)).setImageResource(R.drawable.ic_cloudy);
                break;
            case "Snow":
                ((ImageView) item.findViewById(R.id.gridlayout_image)).setImageResource(R.drawable.ic_snow);
                break;
            case "Rain":
                ((ImageView) item.findViewById(R.id.gridlayout_image)).setImageResource(R.drawable.ic_rain);
                break;
        }

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) + dayCounter;

        //Log.d(TAG, "setWeatherForecast: Day is " + day);

        switch (day) {
            case Calendar.SUNDAY:
                //1
                ((TextView) item.findViewById(R.id.gridlayout_text)).setText(R.string.Sun);
                break;

            case Calendar.MONDAY:
                //2
                ((TextView) item.findViewById(R.id.gridlayout_text)).setText(R.string.Mon);
                break;

            case Calendar.TUESDAY:
                //3
                ((TextView) item.findViewById(R.id.gridlayout_text)).setText(R.string.Tues);
                break;

            case Calendar.WEDNESDAY:
                //4
                ((TextView) item.findViewById(R.id.gridlayout_text)).setText(R.string.Wed);
                break;

            case Calendar.THURSDAY:
                //5
                ((TextView) item.findViewById(R.id.gridlayout_text)).setText(R.string.Thur);
                break;

            case Calendar.FRIDAY:
                //6
                ((TextView) item.findViewById(R.id.gridlayout_text)).setText(R.string.Fri);
                break;

            case Calendar.SATURDAY:
                //7
                ((TextView) item.findViewById(R.id.gridlayout_text)).setText(R.string.Sat);
                break;

        }

        /*Resources r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, r.getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(px, px);
        mImageView.setLayoutParams(layoutParams);*/
        mHolder.mGridLayoutWeather.addView(item);

        if (day == 7){
            dayCounter = -calendar.get(Calendar.DAY_OF_WEEK) + 1;
        }else {
            dayCounter++;
        }
    }
}
