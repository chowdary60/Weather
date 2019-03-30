package com.detroitlab.weather.controller.WeatherFragements;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.detroitlab.weather.utility.GPSTracker;
import com.detroitlab.weather.utility.Constants;
import com.detroitlab.weather.R;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by madhu on 3/29/19.
 */


public class CurrentWeatherFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView current_temperature;
    String base_url = "http://api.openweathermap.org/data/2.5/weather?";
    String api_key  = "b5bfca0171f7e6688cd80f80d8d6de44";

    public CurrentWeatherFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_current_weather, container, false);

        current_temperature = rootView.findViewById(R.id.current_temperature);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get  lat,lang of the current location from GPSTracker

        GPSTracker gps = new GPSTracker(getContext());
        Log.i("Madhu","lat,lang"+Double.toString(gps.getLatitude())+" "+Double.toString(gps.getLongitude()));
        if (gps.canGetLocation()) {
            getTemperature(Double.toString(gps.getLatitude()),Double.toString(gps.getLongitude()));
        }else{
             Toast.makeText(getActivity(),"Please enable GPS location in your device",Toast.LENGTH_LONG).show();

         }

    }


    public void getTemperature(String latitude,String longitude){


        String url = Constants.apiBaseUrl + "weather?" + "lat=" + latitude + "&lon=" + longitude + "&APPID=" + Constants.apiKey;

        // getting the weather data from Open weather API using Volley

        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSON parsing to ge the current temperature
                    JSONObject jsonObj = new JSONObject(response.toString());
                    Log.i("madhu", "madhu " + response.toString());
                    JSONObject main = jsonObj.getJSONObject("main");
                    String temperature = main.getString("temp");
                    // Float temperature_kelvin = Float.parseFloat(temperature);
                    //long temperature_farenheit = Math.round(((temperature_kelvin-273.15)*1.8)+32);
                    // set current temeperature to textview
                    current_temperature.setText(Float.toString(Math.round(((Float.parseFloat(temperature) - 273.15) * 1.8) + 32)) + "\u2109");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsObjRequest);
    }


}