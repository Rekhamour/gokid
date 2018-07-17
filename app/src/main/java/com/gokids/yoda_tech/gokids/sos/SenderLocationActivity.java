package com.gokids.yoda_tech.gokids.sos;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.clustering.ClusterManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static com.gokids.yoda_tech.gokids.utils.Utils.latlon;

public class SenderLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Location latlon;

    private ArrayList<SenderLocationsBean> locationslist=new ArrayList<>();
    private ArrayList<LatLng> Ltlonglist=new ArrayList<>();
    private ArrayList<Location> latlongList=new ArrayList<Location>();
    private MarkerOptions options = new MarkerOptions();
    private String latslong;
    private LatLng loc;
    private LatLng Latlong;
    private ClusterManager mClusterManager;
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_location);
        sendSenderLocation();
        list= (ListView)findViewById(R.id.listofcontacts);
        //list.setAdapter(new MyvideoContactsAdapter(SenderLocationActivity.this,R.layout.videocontacts_single_row,locationslist));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mClusterManager = new ClusterManager<>(this, googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.cluster();


    }





    private void sendSenderLocation() {
        Log.e("i m in func","i m in func");
        Location loc= Utils.getLatLong(SenderLocationActivity.this);
        String lati = String.valueOf(loc.getLatitude());
        String longitude = String.valueOf(loc.getLongitude());
        String urlSendLocation= Urls.BASE_URL+"api/viewSOSLocation/email/"+ MySharedPrefrence.getPrefrence(SenderLocationActivity.this).getString("emailId","")+"/location/"+lati+","+longitude;
        //String urlSendLocation= Urls.BASE_URL+"api/viewSOSLocationByContact/email/"+ MySharedPrefrence.getPrefrence(PeopleLocationActivity.this).getString("emailId","")+"/location/"+lati+","+longitude+"/sos/:sos:";
        Log.e("i m in func","i m in func"+urlSendLocation);

        Ion.with(SenderLocationActivity.this)
                .load(urlSendLocation)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            Log.e("rsult", "result" + result.toString());
                            String status = result.get("status").getAsString();
                            String message = result.get("message").getAsString();
                            if (result.has("result")) {

                                JsonArray jsonArray = result.get("result").getAsJsonArray();
                                if (jsonArray.size() > 0) {
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        JsonObject obj = jsonArray.get(i).getAsJsonObject();
                                        String Counter = obj.get("Counter").getAsString();
                                        String SOSLocation = obj.get("SOSLocation").getAsString();
                                    // String SosEmail = obj.get("SOSEmail").getAsString();
                                        String ContactEmail = "";//obj.get("ContactEmail").getAsString();
                                        String ContactPhone = obj.get("ContactPhone").getAsString();
                                        String ContactDeviceToken = "";//obj.get("ContactDeviceToken").getAsString();
                                        String ContactName = obj.get("ContactName").getAsString();
                                        String ContactLocation = obj.get("ContactLocation").getAsString();
                                        SenderLocationsBean bean = new SenderLocationsBean();
                                        bean.setCounter(Counter);
                                        bean.setContactDeviceToken(ContactDeviceToken);
                                        bean.setSosEmail("");
                                        bean.setContactPhone(ContactPhone);
                                        bean.setContactEmail(ContactEmail);
                                        bean.setContactName(ContactName);
                                        bean.setContactLocation(ContactLocation);
                                        bean.setSOSLocation(SOSLocation);
                                        locationslist.add(bean);
                                        Location loc=new Location(SOSLocation);
                                        mClusterManager.addItem(new MyItem(loc.getLatitude(), loc.getLongitude(), "PJ", "https://twitter.com/pjapplez"));


                                    }
                                    Log.e("Senderlocation","listsize"+locationslist.size());

                                    list.setAdapter(new MyvideoContactsAdapter(SenderLocationActivity.this,R.layout.videocontacts_single_row,locationslist));


                                } else {


                                }


                            }
                        }
                    }
                });



    }




}

