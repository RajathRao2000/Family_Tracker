package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapView;

public class CircleMembersOnMap extends AppCompatActivity {

    MapView mMapView;
    Geopoint latlng;
    DatabaseReference databaseReference;
    String uid;
    MapElementLayer mPinLayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_members_on_map);

        mMapView = new MapView(this, MapRenderMode.VECTOR);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout)findViewById(R.id.map_view)).addView(mMapView);
        mMapView.onCreate(savedInstanceState);

        mPinLayer1 = new MapElementLayer();
        mMapView.getLayers().add(mPinLayer1);


        //fatch data from clicked card
        Intent intent = getIntent();
        if (intent != null) {
            uid = intent.getStringExtra("joined_uid");

        }

        openMaps();
    }

    private void openMaps() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {

            databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/").child(uid);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        double lat = dataSnapshot.child("latitude").getValue(Double.class);
                        double log = dataSnapshot.child("longitude").getValue(Double.class);
                        String mtitle = dataSnapshot.child("firstname").getValue(String.class);

                        latlng = new Geopoint(lat, log);

                        //add marker in current position

                        MapIcon pushpin = new MapIcon();
                        pushpin.setLocation(latlng);
                        pushpin.setTitle(mtitle);
                        mPinLayer1.getElements().add(pushpin);
                        mMapView.setScene(
                                MapScene.createFromLocationAndZoomLevel(latlng, 15),
                                MapAnimationKind.NONE);


//                        mMap.addMarker(new MarkerOptions().position(latLng).title(mtitle));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            Toast.makeText(CircleMembersOnMap.this, "check permission", Toast.LENGTH_SHORT).show();
        }

    }

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

}

/*

    private GoogleMap mMap;
    LatLng latLng;
    DatabaseReference databaseReference;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_members_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //fatch data from clicked card
        Intent intent = getIntent();
        if (intent != null) {
            uid = intent.getStringExtra("joined_uid");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {

            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        double lat = dataSnapshot.child("latitude").getValue(Double.class);
                        double log = dataSnapshot.child("longitude").getValue(Double.class);
                        String mtitle = dataSnapshot.child("firstname").getValue(String.class);

                        latLng = new LatLng(lat, log);

                        //add marker in current possition
                        mMap.addMarker(new MarkerOptions().position(latLng).title(mtitle));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            Toast.makeText(circle_members_map.this, "check permission", Toast.LENGTH_SHORT).show();
        }
    }
 */