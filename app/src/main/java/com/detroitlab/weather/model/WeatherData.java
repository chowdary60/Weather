package com.detroitlab.weather.model;

import android.graphics.Bitmap;

/**
 * Created by madhu on 3/29/19.
 */

// pojo calss for weather data

public class WeatherData {

    //weather elements  to present in the recyclerview view

    public String date_time;
    public String weather_icon;
    public String temperature;
    public Bitmap bitmap;

    public WeatherData(){

    }
    // constructors to set the items received from the weather API
    public WeatherData(String date_time,String temperature,String weather_icon) {
        this.date_time = date_time;
        this.temperature = temperature;
        this.weather_icon = weather_icon;

    }

    public WeatherData(String date_time,String temperature,Bitmap bitmap) {
        this.date_time = date_time;
        this.temperature = temperature;
        this.bitmap = bitmap;

    }
    //setter and getters
    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getWeather_icon() {
        return weather_icon;
    }

    public void setWeather_icon(String weather_icon) {
        this.weather_icon = weather_icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }




}
