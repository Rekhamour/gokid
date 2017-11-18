package com.gokids.yoda_tech.gokidsapp.shop.activity;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.eat.adapter.FoodAdapter;
import com.gokids.yoda_tech.gokidsapp.eat.adapter.HintAdapter;
import com.gokids.yoda_tech.gokidsapp.eat.model.Contact;
import com.gokids.yoda_tech.gokidsapp.eat.model.CuisinesBean;
import com.gokids.yoda_tech.gokidsapp.eat.model.MainBean;
import com.gokids.yoda_tech.gokidsapp.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokidsapp.shop.activity.adapter.ShoplistAdapter;
import com.gokids.yoda_tech.gokidsapp.utils.Constants;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by benepik on 23/6/17.
 */

public class EducationFragment extends Fragment implements FoodAdapter.ItemClickCallback,SwipeRefreshLayout.OnRefreshListener {
    RecyclerView rvlistview;
    TextView numdata;
    ShoplistAdapter adapter;
    ArrayList<MainBean> datalist;
    String  category= "Eat";
    public int  mCount =  0;
    Context ctx;
    private boolean loading = true;

    LinearLayoutManager layoutManager;
    private static final String BASE_URL = "http://52.77.82.210/";
    private  final String getAllrestaurants = BASE_URL + "api/viewAllRestaurants/latitude/1.3224070/longitude/103.9443650/email/%20test1gokids@yahoo.com/limitStart/0/count/2/sortBy/Distance/searchBy/" + category;


    private static final String getTotalRestarants = BASE_URL+ "api/viewTotalRestaurants/latitude/1.3224070/longitude/103.9443650/email/%20test1gokids@yahoo.com/limitStart/0/count/2/sortBy/Distance/searchBy/";
    private String TAG = getClass().getName();
    private int total;
    private SwipeRefreshLayout swipe_food;
    private SharedPreferences prefrence;
    private int countlimit = 50;
    private Location latlon;
    private LinearLayout listmap_LL;
    private RecyclerView hintlistview;
    private HintAdapter hintadapter;
    private LinearLayoutManager lm;
    private ArrayList<String> hintlist= new ArrayList<>();

    public static EducationFragment newInstance(){
        EducationFragment itemOnFragment = new EducationFragment();
        return itemOnFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_food, container, false);
        prefrence = getActivity().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,Context.MODE_PRIVATE);
        rvlistview = (RecyclerView) view.findViewById(R.id.food_rv_list);
        swipe_food = (SwipeRefreshLayout) view.findViewById(R.id.swipe_food);
        numdata = (TextView)view. findViewById(R.id.food_num);
        listmap_LL = (LinearLayout)view. findViewById(R.id.listmap_LL);
        listmap_LL.setVisibility(View.GONE);
        ctx= getActivity();
        setHasOptionsMenu(true);
       total= getTotalRestaurants(category);
        datalist = new ArrayList<>();
        swipe_food.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        rvlistview.setLayoutManager(layoutManager);
        adapter = new ShoplistAdapter(getActivity(),datalist);
        rvlistview.setAdapter(adapter);
        latlon= Utils.getLatLong(getActivity());

        swipe_food.post(new Runnable() {
                            @Override
                            public void run() {
                                swipe_food.setRefreshing(true);
                                datalist.clear();
                                rvlistview.removeAllViewsInLayout();
                                getRestaurants(category,prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",mCount,countlimit);
                            }
                        }
        );


        return view;

    }

    @Override
    public void onItemClick(int p) {


    }
    public int getTotalRestaurants(final String category) {
        Ion.with(getActivity())
                .load("http://52.77.82.210/api/categoryTotalCount/category/CLS2/subCategory/CAT5")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {

                            if (result.getAsJsonObject().get("status").getAsString().equals("200")) {
                                total = result.getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject().get("TOTAL_COUNT").getAsInt();
                            }
                            String category_actual = "Educations";

                            numdata.setText(total + " " + category_actual);
                        }
                    }
                });
        return  total;
    }



    public ArrayList<MainBean> getRestaurants(final String category, final String name, final double lat, final double longi, final String sortBy, final int start, final int count){
        mCount =  start;
        String url = BASE_URL + "api/viewAllRestaurants/";
        url += "/latitude/" + lat;
        url += "/longitude/" + longi;
        url += "/email/" + name;

        url +="/limitStart/"+mCount+"/count/" + count;

        if(name != null){
            url += "/searchBy/" + category ;
        }
        String ak= "api/viewAllShops/latitude/1.301949/longitude/103.839829/category/CAT5/limitStart/0/count/5/sortBy/Distance";
        // System.out.println(url);
        String PATH= BASE_URL + "api/viewAllShops/latitude/"+latlon.getLatitude()+"/longitude/"+latlon.getLongitude()+"/category/CAT5/limitStart/"+ mCount+"/count/"+count+"/sortBy/Distance/searchBy/-";
        Log.e(TAG,"path"+PATH);

        Ion.with(getActivity())
                .load(PATH)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.e("Foodfragment", "result" + result);
                        if (e == null) {

                            System.out.println(result);
                            String status = String.valueOf(result.get("status")).replace("\"", "");
                            if (status.equalsIgnoreCase("200")) {
                                swipe_food.setRefreshing(false);
                                loading = true;
                                Log.e(TAG, " i m if status" + status);

                                Log.e("Foodfragment", "status" + status);
                                String message = String.valueOf(result.get("message"));
                                Log.e("Foodfragment", "message" + message);
                                JsonArray res = result.getAsJsonArray("result");
                                if (result.has("result")) {
                                    if (res.size() > 0) {
                                        for (int i = 0; i < res.size(); i++) {
                                            MainBean m = new MainBean();
                                            JsonElement obj = res.get(i);
                                            m.setShopID(obj.getAsJsonObject().get("ShopID").getAsString());
                                            m.setShopName(obj.getAsJsonObject().get("ShopName").getAsString());
                                            m.setShopSubName(obj.getAsJsonObject().get("ShopSubName").getAsString());
                                            m.setShopDetail(obj.getAsJsonObject().get("ShopDetail").getAsString());
                                            m.setSpecialty(obj.getAsJsonObject().get("Specialty").getAsString());
                                            m.setWebsite(obj.getAsJsonObject().get("Website").getAsString());
                                            m.setEmail(obj.getAsJsonObject().get("Email").getAsString());
                                            m.setAddress(obj.getAsJsonObject().get("Address").getAsString());
                                            m.setPostal(obj.getAsJsonObject().get("Postal").getAsString());
                                            m.setLatlong(obj.getAsJsonObject().get("LatLong").getAsString());
                                            m.setKidsfinityScore(obj.getAsJsonObject().get("KidsfinityScore").getAsInt());
                                            m.setDistance(obj.getAsJsonObject().get("Distance").getAsString());
                                            m.setWorkingHour(obj.getAsJsonObject().get("WorkingHour").getAsString());
                                            ArrayList<CuisinesBean> spe = new ArrayList<>();
                                            // if(obj.getAsJsonObject().has("Specialization") && obj.getAsJsonObject().get("Specialization").isJsonArray()) {
                                            // JsonArray spec = obj.getAsJsonObject().get("Cuisines").getAsJsonArray();

                                            if (obj.getAsJsonObject().get("Categories").isJsonArray()) {
                                                ArrayList<CuisinesBean> con = new ArrayList<>();

                                                JsonArray cont = obj.getAsJsonObject().get("Categories").getAsJsonArray();
                                                for (int j = 0; j < cont.size(); j++) {
                                                    CuisinesBean c = new CuisinesBean();

                                                    c.setCuisine(cont.get(j).getAsJsonObject().get("Category").getAsString());
                                                    con.add(c);
                                                }
                                                Log.e(TAG, "con array size" + con.size());

                                                m.setCuisines(con);
                                            }
                                            //m.setCuisines(con);


                                            //}
                                            m.setCuisines(spe);
                                            if (obj.getAsJsonObject().get("Contacts").isJsonArray()) {
                                                ArrayList<Contact> hr = new ArrayList<>();
                                                JsonArray cont = obj.getAsJsonObject().get("Contacts").getAsJsonArray();
                                                for (int j = 0; j < cont.size(); j++) {
                                                    Contact c = new Contact();
                                                    c.setContactId(cont.get(j).getAsJsonObject().get("ContactID").getAsLong());
                                                    c.setOwnerId(cont.get(j).getAsJsonObject().get("OwnerID").getAsString());
                                                    c.setPhoneNo(cont.get(j).getAsJsonObject().get("PhoneNo").getAsString());
                                                    hr.add(c);
                                                }
                                                m.setContacts(hr);
                                            }

                                            ArrayList<String> images = new ArrayList<String>();
                                            if (obj.getAsJsonObject().get("Images").isJsonArray()) {
                                                for (int j = 0; j < obj.getAsJsonObject().get("Images").getAsJsonArray().size(); j++) {
                                                    //System.out.println(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageURL").getAsString());
                                                    images.add(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageURL").getAsString());
                                                }
                                            }
                                            Log.e(TAG, "images array size" + images.size());
                                            m.setImages(images);
                                            datalist.add(m);
                                            mCount++;
                                        }

                                        adapter.notifyDataSetChanged();
                                        swipe_food.setRefreshing(false);
                                    }
                                }
                            }

                            if (total > count) {
                                Log.d(TAG, "total_posts is greater ");

                                rvlistview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                    }

                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        // mCount=count;
                                        // count=count+50;
                                        super.onScrolled(recyclerView, dx, dy);


                                        int pastVisiblesItems = count, visibleItemCount = mCount, totalItemCount = total;
                                        if (dy > 0) //check for scroll down
                                        {
                                            visibleItemCount = layoutManager.getChildCount();
                                            totalItemCount = layoutManager.getItemCount();
                                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                                            if (loading) {
                                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                                    Log.d(TAG, "total count " + totalItemCount + "visibleItemCount + pastVisiblesItems " + visibleItemCount + pastVisiblesItems);
                                                    loading = false;
                                                    swipe_food.setRefreshing(true);
                                                    Log.v(TAG, "Last Item Wow !");
                                                    // apiCall(mCount);
                                                    int lmCount= mCount+50;
                                                    int lastcount=count+50;
                                                    getRestaurants(category, name, latlon.getLatitude(), latlon.getLongitude(), sortBy, lmCount, count+100);

                                                    Log.d(TAG, "value mCount" + mCount);
                                                }
                                            }
                                        }
                                    }
                                });
                            }

                            //if()
                            //int lmCount= mCount+50;
                            // int lastcount=count+50;

                            //  getRestaurants(category,name,lat,longi,sortBy,lmCount,lastcount);
                            //  }
                            // });




                        }
                    }
                });

        return datalist;
    }


    @Override
    public void onRefresh() {
        datalist.clear();
        rvlistview.removeAllViewsInLayout();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        getRestaurants(category,prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",mCount,total);

    }

    @Override
    public void onResume() {
        super.onResume();
        mCount = 0;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        else if(item.getItemId()==R.id.house_search)
        {
         Utils.NavigatetoHome(getActivity());

        } else if (item.getItemId() == R.id.filter_search) {
            PopupMenu popup = new PopupMenu(getActivity(), getActivity().findViewById(R.id.filter_search));
            Utils.getfilterDistance(getActivity(),datalist,popup,adapter);


        } else if (item.getItemId() == R.id.lens_search) {
            Utils.getSearchDialog(getActivity(),datalist,rvlistview);

        }
        return super.onOptionsItemSelected(item);

    }

}
