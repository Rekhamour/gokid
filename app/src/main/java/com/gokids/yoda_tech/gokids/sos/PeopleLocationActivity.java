package com.gokids.yoda_tech.gokids.sos;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import java.util.List;
import java.util.Locale;

import static com.gokids.yoda_tech.gokids.utils.Utils.latlon;

public class PeopleLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<SosLocationsBean> locationslist=new ArrayList<>();
    private ClusterManager mClusterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_location);
        sendSenderLocation();
       // addPersonItems();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mClusterManager = new ClusterManager<>(this, googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        //addPersonItems();
        mClusterManager.cluster();


    }
    private void sendSenderLocation() {
        Log.e("i m in func","i m in func");
        Location loc= Utils.getLatLong(PeopleLocationActivity.this);
        String lati = String.valueOf(loc.getLatitude());
        String longitude = String.valueOf(loc.getLongitude());
        String urlSendLocation= Urls.BASE_URL+"api/sendSOSToContactsByAPN/email/"+ MySharedPrefrence.getPrefrence(PeopleLocationActivity.this).getString("emailId","")+"/location/"+lati+","+longitude;
        //String urlSendLocation= Urls.BASE_URL+"api/viewSOSLocationByContact/email/"+ MySharedPrefrence.getPrefrence(PeopleLocationActivity.this).getString("emailId","")+"/location/"+lati+","+longitude+"/sos/:sos:";
        //String urlSendLocation= Urls.BASE_URL+"api/viewSOSLocation/email/"+ MySharedPrefrence.getPrefrence(PeopleLocationActivity.this).getString("emailId","")+"/location/"+lati+","+longitude;

        Log.e("i m in func","i m in func"+urlSendLocation);

        Ion.with(PeopleLocationActivity.this)
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
                                        String SosEmail = obj.get("SosEmail").getAsString();
                                        String SosLocation = obj.get("SosLocation").getAsString();
                                        String SosPhoneNo = obj.get("SosPhoneNo").getAsString();
                                        String DeviceToken = obj.get("DeviceToken").getAsString();
                                        //String CurrentLocation = obj.get("CurrentLocation").getAsString();
                                        SosLocationsBean bean = new SosLocationsBean();

                                        bean.setCounter(Counter);
                                        bean.setDeviceToken(DeviceToken);
                                        bean.setSosEmail(SosEmail);
                                        bean.setSosPhoneNo(SosPhoneNo);
                                      //  bean.setSosLocation(CurrentLocation);
                                        Location loc=new Location(SosLocation);
                                        mClusterManager.addItem(new MyItem(loc.getLatitude(), loc.getLongitude(), "PJ", "https://twitter.com/pjapplez"));
                                        locationslist.add(bean);
                                    }
                                    /*Intent intent = new Intent(PeopleLocationActivity.this,PeopleLocationActivity.class);
                                    intent.putExtra("locationlist",locationslist);
                                    startActivity(intent);*/

                                } else {


                                }


                            }
                        }
                    }
                });


    }

    public void videocall(View view) {
        Intent intent= new Intent(PeopleLocationActivity.this,VideoActivity.class);
        startActivity(intent);
    }
}
