package com.detroitlab.weather.view.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.detroitlab.weather.R;
import com.detroitlab.weather.model.WeatherData;


import java.util.List;

/**
 * Created by madhu on 3/29/19.
 */

public class WeatherDataAdapter extends  RecyclerView.Adapter<WeatherDataAdapter.CustomViewHolder> {

    private List<WeatherData> data;
    private Context context;


    // Constructor to se teh the data to List of type Weartherdata
    public WeatherDataAdapter(Context context,List<WeatherData> data){
       if(data != null) {
           this.context = context;
           this.data = data;
           notifyDataSetChanged();
       }else{
           Toast.makeText(context,"Did not receive data from Weather API",Toast.LENGTH_LONG).show();
       }
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(parent!= null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.custom_row, parent, false); // custom view for the elemetns inside the recyclerview
            return new CustomViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
       if(holder!= null) {
           // setting the data to the elements in the witht the help of holder
           holder.date_time.setText(data.get(position).getDate_time());
           holder.tempearature.setText(Float.toString(Math.round((Float.parseFloat(data.get(position).getTemperature()) - 273.15) * 9 / 5 + 32)) + "\u2109");
           holder.weather_icon.setImageBitmap(data.get(position).getBitmap());

       }

    }

    @Override
    public int getItemCount() {
        if(data.size()>0) {
            return data.size();
        }
        return  0;
    }

   // defininf the custom view elements
    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView date_time;
        TextView tempearature;
        ImageView weather_icon;

       CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            date_time = mView.findViewById(R.id.data_time);
            tempearature= mView.findViewById(R.id.temperature);
            weather_icon = mView.findViewById(R.id.weather_icon);

        }
    }



}
