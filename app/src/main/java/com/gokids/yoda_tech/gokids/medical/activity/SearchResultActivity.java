package com.gokids.yoda_tech.gokids.medical.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodListAdapter;
import com.gokids.yoda_tech.gokids.eat.model.Contact;
import com.gokids.yoda_tech.gokids.eat.model.CuisinesBean;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.eat.model.Specialization;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.medical.adapter.MedicalAdapter;
import com.gokids.yoda_tech.gokids.medical.adapter.MedicalListAdapter;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity implements MedicalAdapter.ItemClickCallback{

    RecyclerView medical_list;
    public static MedicalListAdapter medicalAdapter;
    static ArrayList<MainBean> list;
    TextView numMedicals;
    Intent intent;
    public static String category,specializ;
    private SharedPreferences prefrence;
    private Location latlon;
    public static String TAG= "Searchresultactivity";
    private LinearLayoutManager layoutManager;
    private int total;
    public static int  mCount =  0;

    private boolean loading=false;
    private SwipeRefreshLayout swipe;
    private static ProgressDialog dialog;
    private Handler handler;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        list = new ArrayList<>();

        setContentView(R.layout.activity_search_result);
        medical_list = (RecyclerView) findViewById(R.id.medical_list);
        prefrence= getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        numMedicals = (TextView) findViewById(R.id.num_medicals);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_medical);
        medicalAdapter = new MedicalListAdapter(SearchResultActivity.this,list);
        medicalAdapter.setItemCallback(SearchResultActivity.this);
        medical_list.setAdapter(medicalAdapter);
        context= getApplicationContext();
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


         layoutManager = new LinearLayoutManager(getApplicationContext());
        medical_list.setLayoutManager(layoutManager);
        latlon= Utils.getLatLong(SearchResultActivity.this);
        dialog= new ProgressDialog(SearchResultActivity.this);
        dialog.setMessage("Please wait..");
        handler= new Handler();

        medicalAdapter.setLoadMoreListener(new MedicalListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                medical_list.post(new Runnable() {
                    @Override
                    public void run() {
                        final int index = list.size();
                        Log.e(TAG,"i m in loadmore scroll");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadMore(index);

                            }
                        },200);
                    }
                });
            }
        });
        medical_list.setAdapter(medicalAdapter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load(0);

            }
        },200);


    }

    private void load(int index) {


        dialog.show();
        getMedicals(category,"-",latlon.getLatitude(),latlon.getLongitude(),"Distance",specializ,0,50);


    }

    private void loadMore(int index) {
        MainBean bean=new MainBean();
        bean.setType("load");
        list.add(bean);
        medicalAdapter.notifyItemInserted(list.size()+1);
        getMedicals(category,"-",latlon.getLatitude(),latlon.getLongitude(),"Distance",specializ,index,50);

    }


    public void getTotalMedicals(final String category){
        String url = Urls.BASE_URL+"/api/categoryTotalCount/category/CLS4/subCategory/" + category+"/city/"+ MySharedPrefrence.getPrefrence(SearchResultActivity.this).getString("current_city","");;
        Ion.with(getApplicationContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            total = 0;
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

    public static ArrayList<MainBean> getMedicals(final String category, final String name, double lat, double longi, final String sortBy, String specialization, final int start, final int count){
        String url = Urls.BASE_URL + "api/viewAllMedical";
        mCount =  start;

        url += "/latitude/" + lat;
        url += "/longitude/" + longi;
        url += "/category/" + category;
        if(specialization != null){
            url += "/specialization/" + specialization;
        }
        else{
            url += "/specialization/-";
        }
        url +="/limitStart/"+mCount+"/count/" + (mCount+50);
        if(sortBy != null){
            url += "/sortBy/" + sortBy;
        }
        if(name != null){
            url += "/searchBy/" + name+"/city/"+ MySharedPrefrence.getPrefrence(context).getString("current_city","");;
        }
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);
                String status = null;
                try {
                    status = String.valueOf(response.get("status")).replace("\"", "");
                    dialog.dismiss();
                    if (status.equalsIgnoreCase("200")) {
                        Log.e(TAG, " i m if status" + status);


                        Log.e("Foodfragment", "status" + status);
                        String message = String.valueOf(response.get("message"));
                        Log.e("Foodfragment", "message" + message);
                        JSONArray res = response.getJSONArray("result");
                        if (response.has("result")) {
                            Log.e(TAG,"result size"+res.length());
                            if (res.length() > 0) {
                                for (int i = 0; i < res.length(); i++) {
                                    MainBean m = new MainBean();
                                    JSONObject obj = res.getJSONObject(i);
                                    m.setMedicalID(obj.getString("MedicalID"));
                                    m.setCategory(obj.getString("Category"));
                                    m.setName(obj.getString("Name"));
                                    m.setWebsite(obj.getString("Website"));
                                    m.setEmail(obj.getString("Email"));
                                    m.setAddress(obj.getString("Address"));
                                    m.setPostal(obj.getString("Postal"));
                                    m.setLatlong(obj.getString("LatLong"));
                                    m.setPrice(obj.getString("Schedule"));
                                    m.setKidsfinityScore(obj.getDouble("KidsfinityScore"));
                                    m.setDistance(obj.getString("Distance"));
                                    m.setType("data");
                                    // ArrayList<CuisinesBean> spe = new ArrayList<>();
                                    // if(obj.getStringhas("Specialization") && obj.getString("Specialization").isJsonArray()) {
                                    // JsonArray spec = obj.getString("Cuisines").getAsJsonArray();

                                    if (obj.get("Contacts") instanceof JSONArray) {

                                        if (obj.getJSONArray("Contacts").length()>0) {
                                            ArrayList<Contact> hr = new ArrayList<>();
                                            JSONArray cont = obj.getJSONArray("Contacts");
                                            for (int j = 0; j < cont.length(); j++) {
                                                Contact c = new Contact();
                                                c.setContactId(cont.getJSONObject(j).getLong("ContactID"));
                                                c.setOwnerId(cont.getJSONObject(j).getString("OwnerID"));
                                                c.setPhoneNo(cont.getJSONObject(j).getString("PhoneNo"));
                                                hr.add(c);
                                            }
                                            m.setContacts(hr);
                                        }
                                    }
                                    if (obj.get("Specialization") instanceof JSONArray) {

                                        ArrayList<Specialization> spe = new ArrayList<>();
                                        if (obj.getJSONArray("Specialization").length()>0) {
                                            JSONArray spec = obj.getJSONArray("Specialization");
                                            for (int j = 0; j < spec.length(); j++) {
                                                Specialization s = new Specialization();
                                                s.setSpecializationHC(spec.getJSONObject(j).getString("SpecializationHC"));
                                                s.setSpecializationId(spec.getJSONObject(j).getString("SpecializationHCID"));
                                                spe.add(s);
                                            }
                                        }
                                    }
                                    if (obj.get("Images") instanceof JSONArray) {

                                        ArrayList<String> images = new ArrayList<String>();
                                        if (obj.getJSONArray("Images").length() > 0) {
                                            for (int j = 0; j < obj.getJSONArray("Images").length(); j++) {
                                                //System.out.println(obj.getString("Images").getAsJsonArray().get(j).getString("ImageURL"));
                                                images.add(obj.getJSONArray("Images").getJSONObject(j).getString("ImageURL"));
                                            }
                                        }

                                        Log.e(TAG, "images array size" + images.size());
                                        m.setImages(images);
                                    }
                                    list.add(m);
                                    mCount++;
                                }
                                medicalAdapter.notifyDataChanged();

                            }
                            else
                            {
                                Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(context).add(jsonObjectRequest);




        /*Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {


                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {

                            System.out.println(result);
                            if(result.has("result")) {
                                dialog.dismiss();
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
                                    m.setType("data");
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
                                    mCount++;


                                }
                                Log.e(" list size","list size"+list.size());
                                medicalAdapter.notifyDataChanged();

                            }

                        }
                        else
                        {
                            Toast.makeText(context, context.getResources().getString(R.string.oops), Toast.LENGTH_SHORT).show();
                        }
                    }

                });*/
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
