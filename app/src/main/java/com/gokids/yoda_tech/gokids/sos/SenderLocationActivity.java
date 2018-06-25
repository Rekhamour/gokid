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
import java.util.List;
import java.util.Locale;

import static com.gokids.yoda_tech.gokids.utils.Utils.latlon;

public class SenderLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Location latlon;

    private ArrayList<SenderLocationsBean> locationslist=new ArrayList<>();
    private ArrayList<LatLng> Ltlonglist=new ArrayList<>();
    private MarkerOptions options = new MarkerOptions();
    private String latslong;
    private LatLng loc;
    private LatLng Latlong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_location);
        sendSenderLocation();


        //  SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

       // mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
      /*  String latlong = MySharedPrefrence.getPrefrence(SenderLocationActivity.this).getString("SOSlocation","");
            Log.e("latlong string value","latlong string valy"+latlong);
           String[] latandlong = latlong.split("(?!^,)");
            if (latlong != null) {
               loc = new LatLng(Double.parseDouble(latandlong[0]), Double.parseDouble(latandlong[1]));
                Log.e("latlong", "latlong" + loc.latitude + " long" + loc.longitude);
            } else {
                loc = new LatLng(1.23, 103.23);
            }*/

       /*latlon = //Utils.getLatLong(SenderLocationActivity.this);
        LatLng loc = null;
        String nameOfPlace = " ";
        if (latlon != null) {
            loc = new LatLng (latlon.getLatitude(), latlon.getLongitude());
        } else {
            loc = new LatLng(1.23, 103.23);
        }*/
        Geocoder geocoder = new Geocoder(SenderLocationActivity.this, Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(Latlong.latitude, Latlong.longitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                ///nameOfPlace = listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        googleMap.addMarker(new MarkerOptions().position(Latlong)
                .title(""));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Latlong));
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
                                        if(i==0)
                                        {
                                           latslong= SOSLocation;
                                            Log.e("i m in func","i m in func latsandlong"+latslong);
                                            MySharedPrefrence.getPrefrence(SenderLocationActivity.this).edit().putString("SOSlocation",SOSLocation).commit();
                                            String latlong = MySharedPrefrence.getPrefrence(SenderLocationActivity.this).getString("SOSlocation","");
                                            Log.e("i m in func","i m in func latlong"+latlong);

                                        }
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

                                    }


                                } else {


                                }


                            }
                        }
                    }
                });
        String latlong = MySharedPrefrence.getPrefrence(SenderLocationActivity.this).getString("SOSlocation","");
        Log.e("latlong string value","latlong string valy"+latlong);
        ArrayList aList= new ArrayList(Arrays.asList(latlong.split(",")));
        //String[] latandlong = latlong.split("(?!^,)");
        if (latlong != null) {
            Latlong = new LatLng(Double.parseDouble(aList.get(0).toString()), Double.parseDouble(aList.get(1).toString()));
            Log.e("latlong", "latlong" + Latlong.latitude + " long" + Latlong.longitude);
        } else {
            Latlong = new LatLng(1.23, 103.23);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

         mapFragment.getMapAsync(this);


    }

/*
    private void setUpClusterer() {
        // Position the map.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        mClusterManager = new ClusterManager<MyItem>(this, getMap());

        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }
*/
    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng);
           // mClusterManager.addItem(offsetItem);
        }
     /*   for (LatLng point : Ltlonglist) {
            options.position(point);
            options.title("");
            options.snippet("someDesc");
            googleMap.addMarker(options);
        }*/
    }

    public void videocall(View view)
    {
        Intent callIntent = new Intent("com.android.phone.videocall");
        callIntent.putExtra("videocall", true);
        callIntent.setData(Uri.parse("tel:" + "9996633927"));
        startActivity(callIntent);
    }
}

