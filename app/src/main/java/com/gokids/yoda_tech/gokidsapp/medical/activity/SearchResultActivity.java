package com.gokids.yoda_tech.gokidsapp.medical.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.eat.adapter.FoodAdapter;
import com.gokids.yoda_tech.gokidsapp.eat.model.Contact;
import com.gokids.yoda_tech.gokidsapp.eat.model.MainBean;
import com.gokids.yoda_tech.gokidsapp.eat.model.Specialization;
import com.gokids.yoda_tech.gokidsapp.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokidsapp.medical.adapter.MedicalAdapter;
import com.gokids.yoda_tech.gokidsapp.utils.Constants;
import com.gokids.yoda_tech.gokidsapp.utils.Urls;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchResultActivity extends AppCompatActivity implements MedicalAdapter.ItemClickCallback {

    private static final String BASE_URL = "http://52.77.82.210/";
    RecyclerView medical_list;
    MedicalAdapter medicalAdapter;
    ArrayList<MainBean> list;
    TextView numMedicals;
    Intent intent;
    String category,specializ;
    private SharedPreferences prefrence;
    private Location latlon;
    private String TAG= getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        medical_list = (RecyclerView) findViewById(R.id.medical_list);
        prefrence= getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        numMedicals = (TextView) findViewById(R.id.num_medicals);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medical Assistance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        intent = getIntent();
        category = intent.getStringExtra("medical_slecet");
        specializ = intent.getStringExtra("spec_select");

        getTotalMedicals(category);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        list = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        medical_list.setLayoutManager(layoutManager);
        latlon= Utils.getLatLong(SearchResultActivity.this);
        getMedicals(category,null,latlon.getLatitude(),latlon.getLongitude(),"Distance",specializ,0,10);
    }

    public void getTotalMedicals(final String category){
        String url = Urls.BASE_URL+"/api/categoryTotalCount/category/CLS4/subCategory/" + category;
        Ion.with(getApplicationContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            int total = 0;
                            if (result.getAsJsonObject().get("status").getAsString().equals("200")) {
                                total = result.getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject().get("TOTAL_COUNT").getAsInt();
                            }
                           String category_actual = "Clinic";
                           if (category.equals("CAT15")) {
                                category_actual = "Clinic";
                            } else if (category.equals("CAT14")) {
                                category_actual = "Hospital";
                            }
                            if (category.equals("CAT16")) {
                                category_actual = "Pharmacy";
                            }
                            if (category.equals("CAT17")) {
                                category_actual = "Child Care";
                            }
                            numMedicals.setText(total + " " + category_actual);
                        }
                        else
                        {
                            Toast.makeText(SearchResultActivity.this, getResources().getString(R.string.oops), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public ArrayList<MainBean> getMedicals(String category, String name, double lat, double longi, String sortBy, String specialization, int start, int count){
        String url = BASE_URL + "api/viewAllMedical";
        url += "/latitude/" + lat;
        url += "/longitude/" + longi;
        url += "/category/" + category;
        if(specialization != null){
            url += "/specialization/" + specialization;
        }
        else{
            url += "/specialization/-";
        }
        url +="/limitStart/"+start+"/count/" + count;
        if(sortBy != null){
            url += "/sortBy/" + sortBy;
        }
        if(name != null){
            url += "/searchBy/" + name;
        }
        System.out.println(url);

        Ion.with(getApplicationContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            System.out.println(result);
                            JsonArray med = result.getAsJsonObject().get("result").getAsJsonArray();
                            for (int i = 0; i < med.size(); i++) {
                                MainBean m = new MainBean();
                                JsonElement obj = med.get(i);
                                m.setMedicalID(obj.getAsJsonObject().get("MedicalID").getAsString());
                                m.setCategory(obj.getAsJsonObject().get("Category").getAsString());
                                m.setName(obj.getAsJsonObject().get("Name").getAsString());
                                m.setWebsite(obj.getAsJsonObject().get("Website").getAsString());
                                m.setEmail(obj.getAsJsonObject().get("Email").getAsString());
                                m.setAddress(obj.getAsJsonObject().get("Address").getAsString());
                                m.setPostal(obj.getAsJsonObject().get("Postal").getAsString());
                                m.setLatLong(obj.getAsJsonObject().get("LatLong").getAsString());
                                m.setSchedule(obj.getAsJsonObject().get("Schedule").getAsString());
                                m.setKidsfinityScore(Integer.parseInt(obj.getAsJsonObject().get("KidsfinityScore").toString()));
                                m.setDistance(obj.getAsJsonObject().get("Distance").getAsString());
                                ArrayList<Specialization> spe = new ArrayList<>();
                                if (obj.getAsJsonObject().has("Specialization") && obj.getAsJsonObject().get("Specialization").isJsonArray()) {
                                    JsonArray spec = obj.getAsJsonObject().get("Specialization").getAsJsonArray();
                                    for (int j = 0; j < spec.size(); j++) {
                                        Specialization s = new Specialization();
                                        s.setSpecializationHC(spec.get(j).getAsJsonObject().get("SpecializationHC").getAsString());
                                        s.setSpecializationId(spec.get(j).getAsJsonObject().get("SpecializationHCID").getAsString());
                                        spe.add(s);
                                    }
                                }
                                m.setSpecializations(spe);
                                if (obj.getAsJsonObject().get("Contacts").isJsonArray()) {
                                    ArrayList<Contact> con = new ArrayList<>();
                                    JsonArray cont = obj.getAsJsonObject().get("Contacts").getAsJsonArray();
                                    for (int j = 0; j < cont.size(); j++) {
                                        Contact c = new Contact();
                                        c.setContactId(cont.get(j).getAsJsonObject().get("ContactID").getAsLong());
                                        c.setOwnerId(cont.get(j).getAsJsonObject().get("OwnerID").getAsString());
                                        c.setPhoneNo(cont.get(j).getAsJsonObject().get("PhoneNo").getAsString());
                                        con.add(c);
                                    }
                                    m.setContacts(con);
                                }

                                ArrayList<String> images = new ArrayList<String>();
                                if (obj.getAsJsonObject().get("Images").isJsonArray()) {
                                    for (int j = 0; j < obj.getAsJsonObject().get("Images").getAsJsonArray().size(); j++) {
                                        //System.out.println(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageURL").getAsString());
                                        images.add(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageURL").getAsString());
                                    }
                                }
                                m.setImages(images);
                                list.add(m);
                            }
                            //System.out.println(medicals.size()+"-------------");
                            medicalAdapter = new MedicalAdapter(list);
                            medicalAdapter.setItemCallback(SearchResultActivity.this);
                            medical_list.setAdapter(medicalAdapter);
                        }
                        else
                        {
                            Toast.makeText(SearchResultActivity.this, getResources().getString(R.string.oops), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        return list;
    }

    @Override
    public void onItemClick(int p) {
        Intent intent = new Intent(SearchResultActivity.this, MedicalDetail.class).putExtra("medical_data", (Serializable) list.get(p));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        else if(item.getItemId() == R.id.house_search )
        {
          startActivity(new Intent(SearchResultActivity.this, GoKidsHome.class));
            finish();
        }

        else if(item.getItemId()==R.id.filter_search)
        {
            PopupMenu popup = new PopupMenu(SearchResultActivity.this, findViewById(R.id.filter_search));
            Utils.getfilterDistanceMedical(SearchResultActivity.this,list,popup,medicalAdapter);

        }
        else if (item.getItemId() == R.id.lens_search) {
            Utils.getSearchMedical(SearchResultActivity.this,list,medical_list);

        }
        return super.onOptionsItemSelected(item);
    }
}
