package com.example.myapplication.Options;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.Home;
import com.example.myapplication.OptionsActivity;
import com.example.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapView;

import java.util.HashMap;
import java.util.Map;

public class MyCurrentLoc extends AppCompatActivity {

    MapView mMapView;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geopoint latlng;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String current_uid;
    AlertDialog.Builder builder;
    private MapElementLayer mPinLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_current_loc);

        mMapView = new MapView(this, MapRenderMode.VECTOR);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout) findViewById(R.id.map_view)).addView(mMapView);
        mMapView.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        current_uid = firebaseUser.getUid();

        mPinLayer = new MapElementLayer();
        mMapView.getLayers().add(mPinLayer);

        //callpermissionlistener();
        update_location();

    }

    private void update_location() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MyCurrentLoc.this);
            locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(3000) //update interval
                    .setFastestInterval(5000); //fastest interval
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        mPinLayer.getElements().clear();

                        final double lat = locationResult.getLastLocation().getLatitude();
                        final double log = locationResult.getLastLocation().getLongitude();
                        latlng = new Geopoint(lat, log);
                        /*
                        MapIcon pushpin = new MapIcon();
pushpin.setLocation(location);
pushpin.setTitle(title);
pushpin.setImage(new MapImage(pinBitmap));

mPinLayer.getElements().add(pushpin);
                         */
                        MapIcon pushpin = new MapIcon();
                        pushpin.setLocation(latlng);
                        pushpin.setTitle("Your Current Location");
                        mPinLayer.getElements().add(pushpin);
                        mMapView.setScene(
                                MapScene.createFromLocationAndZoomLevel(latlng, 15),
                                MapAnimationKind.NONE);


//                        mMap.addMarker(new MarkerOptions().position(latLng).title("your current location"));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F));

                        //update latitude and longitude
                        Map<String, Object> update = new HashMap<>();
                        update.put("latitude", lat);
                        update.put("longitude", log);
                        databaseReference.child(current_uid).updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MyCurrentLoc.this, "updated", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        Toast.makeText(MyCurrentLoc.this, "location not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }, getMainLooper());
        } else {
            //callpermissionlistener();
        }

    }

//    private void callpermissionlistener() {
//    }

    /////
    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
//        mMapView.setScene(
//                MapScene.createFromLocationAndZoomLevel(, 10),
//                MapAnimationKind.NONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onBackPressed(){

        builder = new AlertDialog.Builder(MyCurrentLoc.this);
        builder.setMessage("Go Back?").setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMapView.removeAllViewsInLayout();
                        finish();
                        startActivity(new Intent(MyCurrentLoc.this, OptionsActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Exit");
        alert.show();

    }
}


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_current_loc);
//    }
