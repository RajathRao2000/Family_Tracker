package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;



import java.util.List;

public class Home extends AppCompatActivity  {


    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSION_FINE_LOCATION = 99;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_sensor, tv_updates, tv_address,tv_wayPointCounts;
    Button btn_newWayPoint,btn_showWayPointList,OptionButton;
    Switch sw_locationupdates, sw_gps;

    //variable to remember if we are tracking location or not.
    boolean updateOn = false;

    //current location
    Location currentLocation;
    //list of saved location
    List<Location> savedLocations;


    //location request is a config file for all settings related to fusedLocationProviderClient
    LocationRequest locationRequest;

    LocationCallback locationCallBack;

    //google's api for location services. The majority of the apps functions using this class.
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // give each UI variable a value

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
//        tv_altitude = findViewById(R.id.tv_altitude);
//        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
//        btn_newWayPoint = findViewById(R.id.btn_newWayPoint);
//        btn_showWayPointList = findViewById(R.id.btn_showWayPointList);
        //tv_wayPointCounts = findViewById(R.id.tv_countOfCrumbs);
        OptionButton = findViewById(R.id.OptionsBtn);


        //set all properties of location request
        locationRequest = new LocationRequest();

        //how often does default location occur?
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);

        //how often does location check occur when set most frequent update?
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);


        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //event that is triggered whenever the update interval is met
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //save the location
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };

        OptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOptionsActivity();
            }
        });

//        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                //get the GPS location
//
//                //add the new location to the global list
//                MyApp myApplication = (MyApp) getApplicationContext();
//                savedLocations = MyApp.getInstance().getMyLocations();//in video : savedLocations = MyApplication.getMyLocations();
//                savedLocations.add(currentLocation);
//            }
//        });



//        btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(Home.this,ShowSavedLocationsList.class);
//                startActivity(i);
//            }
//        });

        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_gps.isChecked()) {
                    locationRequest.setPriority((LocationRequest.PRIORITY_HIGH_ACCURACY));
                    tv_sensor.setText("Using GPS sensors");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using tower+WIFI");
                }
            }
        });


        sw_locationupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_locationupdates.isChecked()) {
                    //turn on tracking
                    startLocationUpdates();
                } else {
                    //turn off tracking
                    stopLocationUpdates();

                }
            }
        });

        updateGps();
    }//end of onCreate method

    private void goToOptionsActivity() {
        Intent intent = new Intent(Home.this,OptionsActivity.class);
        startActivity(intent);
    }

    private void stopLocationUpdates() {

        tv_updates.setText("Location is NOT being tracked");

        tv_lat.setText("Not Tracking Location");
        tv_lon.setText("Not Tracking Location");
//        tv_accuracy.setText("Not Tracking Location");
        tv_address.setText("Not Tracking Location");
//        tv_altitude.setText("Not Tracking Location");
        tv_sensor.setText("Not Tracking Location");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);

    }

    private void startLocationUpdates() {

        tv_updates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    updateGps();
                }else{
                    Toast.makeText(this, "This app requires permission to be granted to work properly", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void updateGps(){
        //get permission from user to track GPS
        //get the current location from fused client
        //update the UI:-i.e. set all properties in their associated text view items

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(Home.this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //User provided permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //we got permissions.Put the values of location. XXX into the UI components.
                    updateUIValues(location);
                    currentLocation = location;

                }
            });
        }else{
            //permission not granted yet
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
            }

        }

    }


    private void updateUIValues(Location location) {
        //update all of textView objects with new location
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
//        tv_accuracy.setText(String.valueOf(location.getAccuracy()));
//
//        if(location.hasAccuracy()){
//            tv_altitude.setText(String.valueOf(location.getAccuracy()));
//        }else{
//            tv_altitude.setText("Not Available");
//        }



        Geocoder geocoder = new Geocoder(Home.this);

        try{
            List<Address> addresses= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            tv_address.setText(addresses.get(0).getAddressLine(0));

        }catch (Exception e){

            tv_address.setText("Unable to get street address");
        }
//        MyApp myApplication = (MyApp) getApplicationContext();
//        savedLocations = MyApp.getInstance().getMyLocations();//in video : savedLocations = MyApplication.getMyLocations();

        //show the number of waypoints saved
//        tv_wayPointCounts.setText(Integer.toString(savedLocations.size()));

    }



}