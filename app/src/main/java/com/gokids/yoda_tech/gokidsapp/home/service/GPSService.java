package com.gokids.yoda_tech.gokidsapp.home.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gokids.yoda_tech.gokidsapp.signup.activity.SignUpActivity;
import com.gokids.yoda_tech.gokidsapp.utils.Constants;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.koushikdutta.async.Util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by manoj2prabhakar on 15/05/17.
 */

public class GPSService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    private Context mContext;
    private LocationManager mLocationManager;

    boolean isNetworkEnabled = false;
    boolean isGPSEnabled = false;
    boolean accessLocation = false;

    Location location;
    double latitude;
    double longitude;
    String city;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static final int FINE_LOCATION_REQUEST_CODE = 3424;

    public static final int COARSE_LOCATION_REQUEST_CODE = 3452;

    public static final int MIN_DISTANCE_CHANGE = 10;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute



    @Override
    public void onCreate() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        mGoogleApiClient.connect();
    }



    public boolean checkRunTimePermission(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(checkRunTimePermission()){
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if(location != null) {

                Log.v("location",location.getLongitude()+"");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getLocationCity();
            }
        }
        else {


        }

        this.stopSelf();
    }

    @Override
    public void onConnectionSuspended(int i) {
        this.stopSelf();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.stopSelf();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void getLocationCity(){

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            if(addresses.size() > 0) {
                city = addresses.get(0).getLocality();
                setSharedPreference(city);
                setCurrentlocationinprofile(city);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentlocationinprofile(String city) {

      /*  SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
             SharedPreferences.Editor editor = prefs.edit();
        editor.putString("newCurrentcity",city);*/

    }

    public void setSharedPreference(String city) {
        Utils.currentloc=city;
        Log.e("service"," city in service"+ city);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("city",city);
        editor.apply();
        //Utils.setCurrentloc(getApplicationContext(),city);


        sendIntent();

    }

    public void sendIntent(){
        Intent sendIntent = new Intent(SignUpActivity.RECEIVE_BROADCAST);
        sendIntent.putExtra("city",city);
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendIntent);
    }


}
