package com.detroitlab.weather.controller;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.detroitlab.weather.view.Adapter.ViewPagerAdapter;
import com.detroitlab.weather.R;
import com.detroitlab.weather.controller.WeatherFragements.CurrentWeatherFragment;
import com.detroitlab.weather.controller.WeatherFragements.FiveDayTemperatureFragment;
import java.util.ArrayList;

/**
 * Created by madhu on 3/29/19.
 */

public class WeatherActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int sPermissionRequestCode = 0;
    private static final String[] sRequiredPermissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };   // Location runtime permission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> requests = new ArrayList<>();

        for ( String permission : sRequiredPermissions) {
            if ( ActivityCompat.checkSelfPermission( this, permission )
                    == PackageManager.PERMISSION_DENIED ) {
                requests.add( permission );
            }
        }

        // Request necessary permissions if not already granted, else start app
        if ( requests.size() > 0 ) {
            ActivityCompat.requestPermissions( this,
                    requests.toArray( new String[requests.size()] ), sPermissionRequestCode );
        } else {
            create();   // start the app
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ( requestCode == sPermissionRequestCode) {
            if ( grantResults.length > 0 ) {
                for ( int grantResult : grantResults ) {
                    if ( grantResult == PackageManager.PERMISSION_DENIED ) {
                        // Permission request was denied
                        Toast.makeText( this, "Permissions required",
                                Toast.LENGTH_LONG ).show();
                    }
                }
                // Permissions have been granted. Start app
                create();
            }
            else {
                // Permission request was denied
                Toast.makeText( this, "Permissions required", Toast.LENGTH_LONG ).show();
            }
        }
    }

    public void create(){
        // launch the application
        setContentView(R.layout.activity_weather);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void setupViewPager(ViewPager viewPager) {
        // set fragements to viewpager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrentWeatherFragment(),getResources().getString(R.string.CurrentWeather));
        adapter.addFragment(new FiveDayTemperatureFragment(), getResources().getString(R.string.Forecast));
        viewPager.setAdapter(adapter);
    }


}