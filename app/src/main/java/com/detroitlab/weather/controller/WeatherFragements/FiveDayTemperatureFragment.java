package com.detroitlab.weather.controller.WeatherFragements;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.detroitlab.weather.utility.GPSTracker;
import com.detroitlab.weather.view.Adapter.WeatherDataAdapter;
import com.detroitlab.weather.utility.Constants;
import com.detroitlab.weather.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.detroitlab.weather.model.WeatherData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by madhu on 3/29/19.
 */

public class FiveDayTemperatureFragment  extends Fragment{

    List<WeatherData> weatherDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private WeatherDataAdapter weatherDataAdapter;
    WeatherData weatherData = new WeatherData();

    public FiveDayTemperatureFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_five_day_temperature, container, false);
        recyclerView = rootView.findViewById(R.id.customRecyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // get lattitude and longitude
        GPSTracker gps = new GPSTracker(getContext());
        if(gps.canGetLocation()&& (!Double.toString(gps.getLatitude()).equals("0.0"))&& (!Double.toString(gps.getLongitude()).equals("0.0"))){
           getFiveDayForecast(Double.toString(gps.getLatitude()),Double.toString(gps.getLongitude()));
        }else{
            Toast.makeText(getActivity(),"Please enable GPS location in your device",Toast.LENGTH_LONG).show();
        }
    }

    private void populateWeatehrDate(List<WeatherData> weatherDataList) {
        // attaching the data to WeatherData adapter
        weatherDataAdapter = new WeatherDataAdapter(getActivity(),weatherDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(weatherDataAdapter);

    }

    public void getFiveDayForecast(String lattitude,String longitude){
        String url = Constants.apiBaseUrl+"forecast?"+"lat="+lattitude+"&lon="+longitude+"&APPID="+Constants.apiKey; // weather api URL to download the forcast data

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        // using Volley Library to get weaher data from Open Weather API.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject  jsonObj = new JSONObject(response.toString());
                    JSONArray jsonarray = jsonObj.getJSONArray("list");
                    for(int i=0;i<jsonarray.length();i++){

                        JSONObject jsonarrayJSONObject = jsonarray.getJSONObject(i);
                        JSONObject jsonObject1 = jsonarrayJSONObject.getJSONObject("main");
                        String temperature = jsonObject1.getString("temp");
                        JSONArray weather = jsonarrayJSONObject.getJSONArray("weather");
                        JSONObject weatherJSONObject = weather.getJSONObject(0);
                        String  icon = weatherJSONObject.getString("icon");
                        String  date = jsonarrayJSONObject.getString("dt_txt");

                        weatherData = new WeatherData(date,temperature,icon);
                        ImageDownload task = new ImageDownload();  // downlaod the Weather Icon from Open Wether API using icon code
                        task.execute(weatherData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsObjRequest);
    }


    //Asynctask to get the weather icon using weather icon code.
    private class ImageDownload extends AsyncTask<WeatherData,Void,WeatherData> {

        @Override
        protected WeatherData doInBackground(WeatherData... weatherData1) {

            try {

                String src = Constants.image_url+weatherData1[0].getWeather_icon()+".png";
                java.net.URL url = new java.net.URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();   // connect to the Open weather API and fetech the corresponding weather Ico
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input); // convert inputstream to bitmap.


                // upscale bitmap
                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                int width = myBitmap.getWidth();
                int height = myBitmap.getHeight();
                float scaleWidth = metrics.scaledDensity;
                float scaleHeight = metrics.scaledDensity;

                // create a matrix for the manipulation
                Matrix matrix = new Matrix();
                // resize the bit map
                matrix.postScale(scaleWidth, scaleHeight);
                // recreate the new Bitmap
                Bitmap   resizedBitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, matrix, true);

                return   new WeatherData(weatherData1[0].getDate_time(),weatherData1[0].getTemperature(),resizedBitmap);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeatherData weatherData) {
            super.onPostExecute(weatherData);

            weatherDataList.add(weatherData);
            populateWeatehrDate(weatherDataList);   // updating data to Recyclerview UI element.
        }
    }
}