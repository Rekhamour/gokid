package com.gokids.yoda_tech.gokids.eat.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.Utils;
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

/**
 * Created by Lenovo on 10/21/2017.
 */
public class FoodMapFragment extends Fragment implements OnMapReadyCallback {
    private Location latlon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment_layout, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return view;
    }

    String getCurrentLocation(){
        String currentLoc = "";

        return currentLoc;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        latlon = Utils.getLatLong(getActivity());
        LatLng loc = null;
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