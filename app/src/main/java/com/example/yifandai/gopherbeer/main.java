package com.example.yifandai.gopherbeer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class main extends AppCompatActivity implements LocationListener, SensorEventListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    double lat;
    double lng;

    double location1lat = 43.4710767;
    double location1lng = -80.5136690;
    Location location1;
    Location userLocation;

    Sensor sensor;

    double distance;
    double degree;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.mainscreen);
        SensorManager mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, magnetometer,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location1 = new Location("location 1");
        userLocation = new Location("user location");

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        getLatLng();


        location1.setLatitude(location1lat);
        location1.setLongitude(location1lng);
        imageView = (ImageView)findViewById(R.id.arrow);
    }


    public void getLatLng(){
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        lat = lastKnownLocation.getLatitude();
        lng = lastKnownLocation.getLongitude();

        TextView tv = (TextView)findViewById(R.id.text);
        tv.setText(lat+","+lng);
        userLocation.setLatitude(lat);
        userLocation.setLongitude(lng);
    }

    public void function(View v){
        getLatLng();
    }

    public void beerClicked(View v){
        distance = userLocation.distanceTo(location1);
        TextView distancetv = (TextView)findViewById(R.id.distance);
        distancetv.setText(""+distance);
        degree = userLocation.bearingTo(location1);



    }

    public void apiCall(View v){
        ImageView iv = (ImageView)findViewById(R.id.arrow);
        iv.setRotation(180);
    }



    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getBaseContext(), "eeee", Toast.LENGTH_LONG).show();

        Log.d("hello", "hello");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    float[] mGravity;
    float[] mGeomagnetic;
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimut = orientation[0];
                int degrees = (int)Math.round(Math.toDegrees(azimut));

                int xConstant = (int)degree;
                if(xConstant < 0){
                    xConstant = xConstant + 360;
                }
                int yVariable = degrees;
                if(yVariable < 0){
                    yVariable = yVariable + 360;
                }

                int zDifference = xConstant - yVariable;
                if(zDifference < 0){
                    zDifference = zDifference + 360;
                }

                if (imageView != null){
                    imageView.setRotation((float)(zDifference));
                    Log.d(""+xConstant,""+yVariable+ "   "+zDifference);

                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
