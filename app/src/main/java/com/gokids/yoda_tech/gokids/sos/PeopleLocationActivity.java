package com.gokids.yoda_tech.gokids.sos;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.gokids.yoda_tech.gokids.utils.Utils.latlon;

public class PeopleLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<SosLocationsBean> locationslist=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_location);
        sendSenderLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        for(int i=0;i<locationslist.size();i++)
        {
            LatLng loc= null;
            String nameOfPlace="";
            SosLocationsBean bean=locationslist.get(i);
            String latlong = bean.getSosLocation();
            String[] latandlong=latlong.split("(?!^,)");
            Log.e("latlong","lalong"+latlong);
            if (latlon != null) {
                loc = new LatLng(Double.parseDouble(latandlong[0]),Double.parseDouble( latandlong[1]));
            } else {
                loc = new LatLng(1.23, 103.23);
            }
            Geocoder geocoder = new Geocoder(PeopleLocationActivity.this, Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(Double.parseDouble(latandlong[0]),Double.parseDouble( latandlong[1]), 1);
                if(null!=listAddresses&&listAddresses.size()>0){
                    nameOfPlace = listAddresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            googleMap.addMarker(new MarkerOptions().position(loc)
                    .title(nameOfPlace));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        }
      //  latlon = Utils.getLatLong(getActivity());
       // LatLng loc = null;
      /*  String nameOfPlace = " ";
        if (latlon != null) {
            loc = new LatLng (latlon.getLatitude(), latlon.getLongitude());
        } else {
            loc = new LatLng(1.23, 103.23);
        }
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(latlon.getLatitude(), latlon.getLongitude(), 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                nameOfPlace = listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        googleMap.addMarker(new MarkerOptions().position(loc)
                .title(nameOfPlace));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));*/
    }
    private void sendSenderLocation() {
        Log.e("i m in func","i m in func");
        Location loc= Utils.getLatLong(PeopleLocationActivity.this);
        String lati = String.valueOf(loc.getLatitude());
        String longitude = String.valueOf(loc.getLongitude());
        String urlSendLocation= Urls.BASE_URL+"api/sendSOSToContactsByAPN/email/"+ MySharedPrefrence.getPrefrence(PeopleLocationActivity.this).getString("emailId","")+"/location/"+lati+","+longitude;
        //String urlSendLocation= Urls.BASE_URL+"api/viewSOSLocationByContact/email/"+ MySharedPrefrence.getPrefrence(PeopleLocationActivity.this).getString("emailId","")+"/location/"+lati+","+longitude+"/sos/:sos:";
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
                                        String CurrentLocation = obj.get("CurrentLocation").getAsString();
                                        SosLocationsBean bean = new SosLocationsBean();

                                        bean.setCounter(Counter);
                                        bean.setDeviceToken(DeviceToken);
                                        bean.setSosEmail(SosEmail);
                                        bean.setSosPhoneNo(SosPhoneNo);
                                        bean.setSosLocation(CurrentLocation);

                                        locationslist.add(bean);
                                    }
                                    Intent intent = new Intent(PeopleLocationActivity.this,PeopleLocationActivity.class);
                                    intent.putExtra("locationlist",locationslist);
                                    startActivity(intent);

                                } else {


                                }


                            }
                        }
                    }
                });


    }

}
