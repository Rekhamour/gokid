package com.gokids.yoda_tech.gokids.bookmark.activity;

import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.bookmark.adapter.BookmarksAdapter;
import com.gokids.yoda_tech.gokids.eat.model.Contact;
import com.gokids.yoda_tech.gokids.eat.model.CuisinesBean;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.eat.model.Specialization;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllBookmarksActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private SharedPreferences prefrence;
    private String TAG= getClass().getName();
    public ArrayList<MainBean> list;
    private LinearLayoutManager layoutmanager;
    private BookmarksAdapter adapter;
    private Spinner spinner;
    private ArrayList<MainBean> filtershoppinglist = new ArrayList<>();
    private ArrayList<MainBean> filterFoodlist= new ArrayList<>();
    private ArrayList<MainBean> filterentertainment= new ArrayList<>();
    private ArrayList<MainBean> filtersmedicals= new ArrayList<>();
    private Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bookmarks);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefrence = getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
       loc= Utils.getLatLong(AllBookmarksActivity.this);
         list = new ArrayList<>();

        setUpUi();

    }

    private void setUpUi() {
        spinner = (Spinner) findViewById(R.id.bookmark_filter);
        List<String> lists = new ArrayList<String>();
        lists.add("All");
        lists.add("Food");
        lists.add("Shopping");
        lists.add("Medical");
        lists.add("Entertainment");
       ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
               android.R.layout.simple_spinner_item, lists);
       dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner.setAdapter(dataAdapter);
        /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_single_row,R.array.bookmark_arrays);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);*/

        recyclerview= (RecyclerView)findViewById(R.id.rv_bookmark);
        layoutmanager= new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutmanager);
        adapter= new BookmarksAdapter(AllBookmarksActivity.this,list);
        recyclerview.setAdapter(adapter);

       // apiCall();
    }

    private void apiCall() {
        list.clear();
        if (!prefrence.getString("emailId", "").trim().isEmpty()) {
            String url = "api/viewAllBookmarks/email/" + prefrence.getString("emailId", "") + "/class/-"+"/latitude/"+loc.getLatitude()+"/longitude/"+loc.getLongitude();

            String getAllBookmark = Urls.BASE_URL + url;
            Log.e(TAG,"path"+getAllBookmark);
            Ion.with(AllBookmarksActivity.this)
                    .load(getAllBookmark)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                Log.e(TAG, "result" + result.toString());
                                String status = result.get("status").toString();
                                String message = result.get("message").toString();
                            /*if(status.equalsIgnoreCase("200"))
                            {*/
                                Log.e(TAG, "i m in status");

                                JsonArray resultarrey = result.get("result").getAsJsonArray();
                                try {
                                    JSONArray arrey = new JSONArray(resultarrey.toString());
                                    if (arrey != null) {
                                        for (int i = 0; i < arrey.length(); i++) {
                                            JSONObject obj = arrey.getJSONObject(i);
                                            String Class = obj.getString("Class");

                                            if (Class.equalsIgnoreCase("Food")) {
                                                MainBean m = new MainBean();

                                                m.setRestaurantID(obj.get("RestaurantID").toString());
                                                m.setClassname(Class);
                                                m.setRestaurantName(obj.get("RestaurantName").toString());
                                                m.setRestaurantSubName(obj.get("RestaurantSubName").toString());
                                                m.setSpecialty(obj.get("Specialty").toString());
                                                m.setWebsite(obj.get("Website").toString());
                                                m.setEmail(obj.get("Email").toString());
                                                m.setAddress(obj.get("Address").toString());
                                                m.setPostal(obj.get("Postal").toString());
                                                m.setLatlong(obj.get("LatLong").toString());
                                                m.setPrice(obj.get("Price").toString());
                                                m.setKidsfinityScore((int) Double.parseDouble(obj.get("KidsfinityScore").toString()));
                                                m.setDistance(obj.get("Distance").toString());
                                                m.setWorkingHour(obj.get("WorkingHour").toString());
                                                ArrayList<CuisinesBean> spe = new ArrayList<>();
                                                // if(obj.getAsJsonObject().has("Specialization") && obj.getAsJsonObject().get("Specialization").isJsonArray()) {
                                                // JsonArray spec = obj.getAsJsonObject().get("Cuisines").getAsJsonArray();
                                                if (obj.has("Cuisines")) {
                                                    if(obj.get("Cuisines") instanceof  JSONArray) {

                                                            ArrayList<CuisinesBean> con = new ArrayList<>();
                                                            JSONArray cont = obj.getJSONArray("Cuisines");
                                                            for (int j = 0; j < cont.length(); j++) {
                                                                CuisinesBean c = new CuisinesBean();

                                                                c.setCuisine(cont.getJSONObject(j).getString("Cuisine"));
                                                                con.add(c);
                                                            }
                                                            Log.e(TAG, "con array size" + con.size());

                                                            m.setCuisines(con);
                                                        }

                                            }

                                                //}
                                                m.setCuisines(spe);
                                                if (obj.has("Contacts")) {
                                                    if(obj.get("Contacts") instanceof  JSONArray) {
                                                        ArrayList<Contact> con = new ArrayList<>();
                                                        JSONArray cont = obj.getJSONArray("Contacts");
                                                        for (int j = 0; j < cont.length(); j++) {
                                                            Contact c = new Contact();
                                                            // c.setContactId(cont.getJSONObject(j).getString("ContactID"));
                                                            c.setOwnerId(cont.getJSONObject(j).getString("OwnerID"));
                                                            c.setPhoneNo(cont.getJSONObject(j).getString("PhoneNo"));
                                                            con.add(c);
                                                        }
                                                        m.setContacts(con);
                                                    }
                                                }

                                                if (obj.has("Images")) {
                                                    if (obj.get("Images") instanceof JSONArray) {
                                                        ArrayList<String> images = new ArrayList<String>();
                                                        if (obj.getJSONArray("Images").length() > 0) {
                                                            for (int j = 0; j < obj.getJSONArray("Images").length(); j++) {
                                                                images.add(obj.getJSONArray("Images").getJSONObject(j).getString("ImageURL"));
                                                            }
                                                        }


                                                        Log.e(TAG, "images array size" + images.size());
                                                        m.setImages(images);
                                                        m.setImage(images.get(0));
                                                    }
                                                }

                                                list.add(m);
                                                filterFoodlist.add(m);


                                            } else if (Class.equalsIgnoreCase("Medicals")) {
                                                MainBean m = new MainBean();

                                                m.setMedicalID(obj.get("MedicalID").toString());
                                                m.setClassname(Class);
                                                m.setName(obj.get("Name").toString());
                                                m.setSpecialty(obj.get("Specialty").toString());
                                                m.setWebsite(obj.get("Website").toString());
                                                m.setEmail(obj.get("Email").toString());
                                                m.setAddress(obj.get("Address").toString());
                                                m.setPostal(obj.get("Postal").toString());
                                                m.setLatlong(obj.get("LatLong").toString());
                                                m.setPrice(obj.get("Price").toString());
                                                m.setKidsfinityScore((int) Double.parseDouble(obj.get("KidsfinityScore").toString()));
                                                m.setDistance(obj.get("Distance").toString());
                                                m.setWorkingHour(obj.get("WorkingHour").toString());
                                                ArrayList<Specialization> spe = new ArrayList<>();
                                              /*  if (obj.has("Specialization") && obj.get("Specialization").isJsonArray()) {
                                                    JsonArray spec = obj.getAsJsonObject().get("Specialization").getAsJsonArray();
                                                    for (int j = 0; j < spec.size(); j++) {
                                                        Specialization s = new Specialization();
                                                        s.setSpecializationHC(spec.get(j).getAsJsonObject().get("SpecializationHC").getAsString());
                                                        s.setSpecializationId(spec.get(j).getAsJsonObject().get("SpecializationHCID").getAsString());
                                                        spe.add(s);
                                                    }
                                                }*/
                                                m.setSpecializations(spe);
                                                // if(obj.getAsJsonObject().has("Specialization") && obj.getAsJsonObject().get("Specialization").isJsonArray()) {
                                                // JsonArray spec = obj.getAsJsonObject().get("Cuisines").getAsJsonArray();


                                                if (obj.has("Cuisines")) {
                                                    if (obj.get("Cuisines") instanceof JSONArray) {
                                                        ArrayList<CuisinesBean> con = new ArrayList<>();
                                                        JSONArray cont = obj.getJSONArray("Cuisines");
                                                        for (int j = 0; j < cont.length(); j++) {
                                                            CuisinesBean c = new CuisinesBean();

                                                            c.setCuisine(cont.getJSONObject(j).getString("Cuisine"));
                                                            con.add(c);
                                                        }
                                                        Log.e(TAG, "con array size" + con.size());

                                                        m.setCuisines(con);
                                                    }
                                                }

                                                //}
                                                //m.setCuisines(spe);
                                                if (obj.has("Contacts")) {
                                                    if(obj.get("Contacts") instanceof  JSONArray) {
                                                        ArrayList<Contact> con = new ArrayList<>();
                                                        JSONArray cont = obj.getJSONArray("Contacts");
                                                        for (int j = 0; j < cont.length(); j++) {
                                                            Contact c = new Contact();
                                                            // c.setContactId(cont.getJSONObject(j).getString("ContactID"));
                                                            c.setOwnerId(cont.getJSONObject(j).getString("OwnerID"));
                                                            c.setPhoneNo(cont.getJSONObject(j).getString("PhoneNo"));
                                                            con.add(c);
                                                        }
                                                        m.setContacts(con);
                                                    }
                                                }

                                                if (obj.has("Images")) {
                                                    if (obj.get("Images") instanceof JSONArray) {
                                                        ArrayList<String> images = new ArrayList<String>();
                                                        if (obj.getJSONArray("Images").length() > 0) {
                                                            for (int j = 0; j < obj.getJSONArray("Images").length(); j++) {
                                                                images.add(obj.getJSONArray("Images").getJSONObject(j).getString("ImageURL"));
                                                            }
                                                        }


                                                        Log.e(TAG, "images array size" + images.size());
                                                        m.setImages(images);
                                                        m.setImage(images.get(0));
                                                    }
                                                }

                                                list.add(m);
                                                filterFoodlist.add(m);


                                            } else if (Class.equalsIgnoreCase("Shopping")) {
                                                MainBean m = new MainBean();
                                                m.setClassname(Class);
                                                m.setShopDetail(obj.get("ShopDetail").toString());
                                                m.setShopID(obj.get("ShopID").toString());
                                                m.setShopName(obj.get("ShopName").toString());
                                                m.setShopSubName(obj.get("ShopSubName").toString());
                                                m.setSpecialty(obj.get("Specialty").toString());
                                                m.setWebsite(obj.get("Website").toString());
                                                m.setEmail(obj.get("Email").toString());
                                                m.setAddress(obj.get("Address").toString());
                                                m.setPostal(obj.get("Postal").toString());
                                                m.setLatlong(obj.get("LatLong").toString());
                                                m.setKidsfinityScore(Integer.parseInt(obj.get("KidsfinityScore").toString()));
                                                m.setDistance(obj.get("Distance").toString());
                                                m.setWorkingHour(obj.get("WorkingHour").toString());
                                                // ArrayList<CuisinesBean> spe = new ArrayList<>();
                                                // if(obj.getAsJsonObject().has("Specialization") && obj.getAsJsonObject().get("Specialization").isJsonArray()) {
                                                // JsonArray spec = obj.getAsJsonObject().get("Cuisines").getAsJsonArray();
                                                //}
                                                //  m.setCuisines(spe);
                                                if (obj.has("Contacts")) {
                                                    if(obj.get("Contacts") instanceof  JSONArray) {
                                                        ArrayList<Contact> con = new ArrayList<>();
                                                        JSONArray cont = obj.getJSONArray("Contacts");
                                                        for (int j = 0; j < cont.length(); j++) {
                                                            Contact c = new Contact();
                                                            // c.setContactId(cont.getJSONObject(j).getString("ContactID"));
                                                            c.setOwnerId(cont.getJSONObject(j).getString("OwnerID"));
                                                            c.setPhoneNo(cont.getJSONObject(j).getString("PhoneNo"));
                                                            con.add(c);
                                                        }
                                                        m.setContacts(con);
                                                    }
                                                }
                                                if (obj.has("Images")) {
                                                    if (obj.get("Images") instanceof JSONArray) {
                                                        ArrayList<String> images = new ArrayList<String>();
                                                        if (obj.getJSONArray("Images").length() > 0) {
                                                            for (int j = 0; j < obj.getJSONArray("Images").length(); j++) {
                                                                images.add(obj.getJSONArray("Images").getJSONObject(j).getString("ImageURL"));
                                                            }
                                                        }


                                                        Log.e(TAG, "images array size" + images.size());
                                                        m.setImages(images);
                                                        m.setImage(images.get(0));
                                                    }
                                                }

                                                list.add(m);
                                                filtershoppinglist.add(m);

                                            } else if (Class.equalsIgnoreCase("Entertainment")) {
                                                MainBean m = new MainBean();
                                                m.setClassname(Class);
                                                m.setEntertainmentID(obj.get("EntertainmentID").toString());
                                                m.setEntertainmentTitle(obj.get("EntertainmentTitle").toString());
                                                m.setEntertainmentdetail(obj.get("EntertainmentDetail").toString());
                                                m.setWebsite(obj.get("Website").toString());
                                                m.setEmail(obj.get("Email").toString());
                                                m.setAddress(obj.get("Address").toString());
                                                m.setPostal(obj.get("Postal").toString());
                                                m.setLatlong(obj.get("LatLong").toString());
                                                m.setKidsfinityScore(Integer.parseInt(obj.get("KidsfinityScore").toString()));
                                                m.setDistance(obj.get("Distance").toString());
                                                m.setStartDate(obj.get("StartDate").toString());
                                                m.setEndDate(obj.get("EndDate").toString());
                                                m.setEventDate(obj.get("EventDate").toString());


                                                //}
                                                if (obj.has("Contacts")) {
                                                    if(obj.get("Contacts") instanceof  JSONArray) {
                                                        ArrayList<Contact> con = new ArrayList<>();
                                                        JSONArray cont = obj.getJSONArray("Contacts");
                                                        for (int j = 0; j < cont.length(); j++) {
                                                            Contact c = new Contact();
                                                            // c.setContactId(cont.getJSONObject(j).getString("ContactID"));
                                                            c.setOwnerId(cont.getJSONObject(j).getString("OwnerID"));
                                                            c.setPhoneNo(cont.getJSONObject(j).getString("PhoneNo"));
                                                            con.add(c);
                                                        }
                                                        m.setContacts(con);
                                                    }
                                                }

                                                if (obj.has("Images")) {
                                                    if (obj.get("Images") instanceof JSONArray) {
                                                        ArrayList<String> images = new ArrayList<String>();
                                                        if (obj.getJSONArray("Images").length() > 0) {
                                                            for (int j = 0; j < obj.getJSONArray("Images").length(); j++) {
                                                                images.add(obj.getJSONArray("Images").getJSONObject(j).getString("ImageURL"));
                                                            }
                                                        }


                                                        Log.e(TAG, "images array size" + images.size());
                                                        m.setImages(images);
                                                        m.setImage(images.get(0));
                                                    }
                                                }

                                                list.add(m);

                                                filterentertainment = new ArrayList<MainBean>();
                                                filterentertainment.add(m);

                                            }


                                        }

                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                if (position == 0) {
                                                    adapter = new BookmarksAdapter(AllBookmarksActivity.this, list);
                                                    recyclerview.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();

                                                }
                                                if (position == 1) {
                                                    adapter = new BookmarksAdapter(AllBookmarksActivity.this, filterFoodlist);
                                                    recyclerview.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();

                                                }
                                                if (position == 2) {
                                                    adapter = new BookmarksAdapter(AllBookmarksActivity.this, filtershoppinglist);
                                                    recyclerview.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();

                                                }
                                                if (position == 3) {
                                                    adapter = new BookmarksAdapter(AllBookmarksActivity.this, filtersmedicals);
                                                    recyclerview.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();

                                                }
                                                if (position == 4) {
                                                    adapter = new BookmarksAdapter(AllBookmarksActivity.this, filterentertainment);
                                                    recyclerview.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();


                                                }

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });

                                        Log.e(TAG, "i m in notified change");

                                        adapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }


                            }


                        }
                    });
        }
        else
        {
            Utils.getLoginContinue(AllBookmarksActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiCall();
    }
}
