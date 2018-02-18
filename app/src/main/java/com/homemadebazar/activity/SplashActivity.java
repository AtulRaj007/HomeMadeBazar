package com.homemadebazar.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.homemadebazar.R;
import com.homemadebazar.model.UserLocation;
import com.homemadebazar.util.SharedPreference;

public class SplashActivity extends BaseActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private Handler handler = new Handler();
    private double latitude, longitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastLocation;
    private ProgressBar progressBar;
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLocation();
            tvMessage.setText("Getting Location ... Please Wait");
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            latitude = mLastLocation.getLatitude();
                            longitude = mLastLocation.getLongitude();
                            Log.e("Latitude", mLastLocation.getLatitude() + "");
                            Log.e("Longitude", mLastLocation.getLongitude() + "");
                            progressBar.setVisibility(View.INVISIBLE);
                            tvMessage.setText(latitude + "," + longitude);
                            UserLocation userLocation = new UserLocation();
                            userLocation.setLatitude(latitude);
                            userLocation.setLongitude(longitude);
                            SharedPreference.saveUserLocation(SplashActivity.this, userLocation);

                        } else {
                            Log.w("getLocation", "getLastLocation:exception", task.getException());
                            tvMessage.setText("Not able to fetch location.");

                        }
                        moveToLoginScreen();
                    }
                });
    }

    private void moveToLoginScreen() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }


    @Override
    protected void initUI() {
        progressBar = findViewById(R.id.progress_bar);
        tvMessage = findViewById(R.id.tv_message);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0) {
            getLocation();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
