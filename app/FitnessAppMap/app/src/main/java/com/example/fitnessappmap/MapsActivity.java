/**
 * This is the class for the activity that displays the map and tracks a
 * users runs.
 */
package com.example.fitnessappmap;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {


    private GoogleMap mMap;
    private static final int REQUEST_LOCATION = 1;
    TextView distanceView;
    Button startButton;
    protected LocationManager locationManager;
    double currentDistance = 0;
    double currentLat = 0;
    double currentLong = 0;
    boolean track = false;
    User user;

    /**
     * The onCreate() method initializes the screen of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        user = (User) getIntent().getSerializableExtra("userObject");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);
        distanceView = findViewById(R.id.distance_text);
        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, this);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * The onClick() method sets the functionality of the buttons on the screen
     */
    public void onClick(View v) {
        if (track) {
            BigDecimal bd =
                    new BigDecimal((currentDistance / Double.valueOf(1000)))
                            .setScale(2, RoundingMode.HALF_UP);
            Double distanceInKm = bd.doubleValue();
            if (distanceInKm > 0) {
                String jsonData = JsonParser.runToString(distanceInKm);
                if (jsonData != null) {
                    boolean status = API.addAttribute(jsonData,
                            user.getUserName(), "run");
                    if (status) {
                        Toast.makeText(getApplicationContext(),
                                NotificationMessages.RUN_SUCCESS,
                                Toast.LENGTH_LONG).show();
                        addMilestones();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                NotificationMessages.RUN_ERROR,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            NotificationMessages.RUN_ERROR,
                            Toast.LENGTH_LONG).show();
                }
            }
            currentDistance = 0;
            startButton.setText("Start");
            currentLat = 0;
            currentLong = 0;
            track = false;
            distanceView.setText("0");
            mMap.clear();
        } else {
            startButton.setText("Stop");
            track = true;
        }
    }

    /**
     * The addMilestones() method checks to see if a milestone has been
     * reached and adds it to the database if it has.
     */
    private void addMilestones() {
        String jsonDataRuns = API.getAttribute(user.getUserName(), "runs");
        if (jsonDataRuns != null) {
            Double[] runs = JsonParser.stringToRuns(jsonDataRuns);
            Double max = Collections.max(Arrays.asList(runs));
            if (runs.length == 1) {
                String milestone_award = "Completed First Run!";
                String jsonDataMilestone =
                        JsonParser.milestoneToString(milestone_award);
                API.addAttribute(jsonDataMilestone, user.getUserName(),
                        "milestone");
                Toast.makeText(getApplicationContext(), milestone_award,
                        Toast.LENGTH_LONG).show();
            }
            if (max == runs[runs.length - 1]) {
                String record_award = "New Record: " + max + " Kilometers!";
                String jsonDataMilestone =
                        JsonParser.milestoneToString(record_award);
                API.addAttribute(jsonDataMilestone, user.getUserName(),
                        "milestone");
                Toast.makeText(getApplicationContext(), record_award,
                        Toast.LENGTH_LONG).show();
            }
            if (runs.length % 5 == 0) {
                String milestone_award = "Completed " + runs.length + " Runs!";
                String jsonDataMilestone =
                        JsonParser.milestoneToString(milestone_award);
                API.addAttribute(jsonDataMilestone, user.getUserName(),
                        "milestone");
                Toast.makeText(getApplicationContext(), milestone_award,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * The onLocationsChanged() method tracks when the user has moved and
     * records the distance they moved
     */
    @Override
    public void onLocationChanged(Location location) {
        if (!track) {
            return;
        }
        double newLat = location.getLatitude();
        double newLong = location.getLongitude();
        if (currentDistance == 0 && currentLat == 0 && currentLong == 0) {
        } else {
            float ret[] = {0, 0};
            android.location.Location.distanceBetween(currentLat, currentLong
                    , newLat, newLong, ret);
            currentDistance += (double) ret[0];
        }
        currentLat = newLat;
        currentLong = newLong;
        int displayDistance = (int) currentDistance;
        distanceView.setText(Integer.toString(displayDistance));
        LatLng currentLatLng = new LatLng(newLat, newLong);
        mMap.addMarker(new MarkerOptions().position(currentLatLng).title(
                "Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
    }

    /**
     * The following methods are needed since the GPS is accessed.
     */
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}