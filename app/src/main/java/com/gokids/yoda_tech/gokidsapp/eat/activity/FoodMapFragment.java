package com.gokids.yoda_tech.gokidsapp.eat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.utils.Constants;
import com.gokids.yoda_tech.gokidsapp.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Lenovo on 10/21/2017.
 */

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.gokids.yoda_tech.gokidsapp.R.string.latitude;
import static com.gokids.yoda_tech.gokidsapp.R.string.longitude;

/**
 * Created by Lenovo on 10/21/2017.
 */
public class FoodMapFragment extends Fragment implements OnMapReadyCallback {
    private Location latlon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment_layout, container, false);
        //String location = MySharedPrefrence.getPrefrence(getActivity()).getString("city", "delhi");
        //Log.v("current loc----------",location + "|-------------");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    /*  mapFragment.getMapAsync(new OnMapReadyCallback() {
          @Override
          public void onMapReady(GoogleMap googleMap) {
              String location= MySharedPrefrence.getPrefrence(getActivity()).getString("city","");
              latlon= Utils.getLatLong(getActivity(), MySharedPrefrence.getPrefrence(getActivity()).getString("city",""));
              LatLng loc = null;
              if(location != null && location.contains(",")) {

                  loc = new LatLng(latlon[0],latlon[1]);
              }
              else{
                  loc = new LatLng(1.23,103.23);
              }
              googleMap.addMarker(new MarkerOptions().position(loc)
                      .title("delhi"));
              googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
          }
      });*/
        return view;
    }

    String getCurrentLocation(){
        String currentLoc = "";

        return currentLoc;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //String location = MySharedPrefrence.getPrefrence(getActivity()).getString("city", "");
        latlon = Utils.getLatLong(getActivity());
        LatLng loc = null;
        //Log.v("loc-----------",location);
        String nameOfPlace = " ";
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }
}