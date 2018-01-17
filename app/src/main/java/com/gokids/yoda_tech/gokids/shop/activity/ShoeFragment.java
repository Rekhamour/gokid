package com.gokids.yoda_tech.gokids.shop.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodAdapter;
import com.gokids.yoda_tech.gokids.eat.model.Contact;
import com.gokids.yoda_tech.gokids.eat.model.CuisinesBean;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.shop.activity.adapter.ShoplistAdapter;
import com.gokids.yoda_tech.gokids.shop.activity.adapter.ShoppingListAdapter;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by benepik on 23/6/17.
 */

public class ShoeFragment extends Fragment  {
    RecyclerView rvListview;
    TextView numList;
    ShoppingListAdapter adapter;
    ArrayList<MainBean> datalist;
    public int  mCount =  0;
    Context ctx;
    private boolean loading = true;

    LinearLayoutManager layoutManager;
    private static final String BASE_URL = "http://52.77.82.210/";
    private String TAG = getClass().getName();
    private int total;
    private SwipeRefreshLayout swipeView;
    private SharedPreferences prefrence;
    private int countlimit = 50;
    private Location latlon;
    private LinearLayout listmap_LL;
    String  category= "Eat";

    public static ShoeFragment newInstance(){
        ShoeFragment itemOnFragment = new ShoeFragment();
        return itemOnFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_food, container, false);
        prefrence = getActivity().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,Context.MODE_PRIVATE);
        rvListview = (RecyclerView) view.findViewById(R.id.food_rv_list);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_food);
        numList = (TextView)view. findViewById(R.id.food_num);
        listmap_LL = (LinearLayout)view. findViewById(R.id.listmap_LL);
        listmap_LL.setVisibility(View.GONE);
        ctx= getActivity();
        setHasOptionsMenu(true);
       total= getTotalRestaurants(category);
        datalist = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        rvListview.setLayoutManager(layoutManager);
        adapter = new ShoppingListAdapter(getActivity(),datalist);
        latlon= Utils.getLatLong(getActivity());
        adapter.setLoadMoreListener(new ShoppingListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                rvListview.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = datalist.size();
                        Log.e(TAG,"i m in loadmore scroll");
                        loadMore(index);
                    }
                });
            }
        });
        rvListview.setAdapter(adapter);
        load(0);

        return view;

    }
    private void load(int index) {

        getRestaurants(category,prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",index,total);

    }

    private void loadMore(int index) {
        MainBean bean=new MainBean();
        bean.setType("load");
        datalist.add(bean);
        adapter.notifyItemInserted(datalist.size()+1);
        getRestaurants(category,prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",index,total);


    }
    public int getTotalRestaurants(final String category) {
        Ion.with(getActivity())
                .load("http://52.77.82.210/api/categoryTotalCount/category/CLS2/subCategory/CAT3")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {

                            if (result.getAsJsonObject().get("status").getAsString().equals("200")) {
                                total = result.getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject().get("TOTAL_COUNT").getAsInt();
                            }
                            String category_actual = "Shoes";

                            numList.setText(total + " " + category_actual);
                        }
                    }
                });
        return  total;
    }



    public ArrayList<MainBean> getRestaurants(final String category, final String name, final double lat, final double longi, final String sortBy, final int start, final int count){

        String PATH= BASE_URL + "api/viewAllShops/latitude/"+latlon.getLatitude()+"/longitude/"+latlon.getLongitude()+"/category/CAT3/limitStart/"+ start+"/count/"+(start+50)+"/sortBy/Distance/searchBy/-";
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

                                Log.e(TAG, " i m if status" + status);

                                Log.e("Foodfragment", "status" + status);
                                String message = String.valueOf(result.get("message"));
                                Log.e("Foodfragment", "message" + message);
                                JsonArray res = result.getAsJsonArray("result");
                                if(result.has("result")) {
                                    if(res.size()>0) {

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
                                            m.setType("data");

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


                                        adapter.notifyDataChanged();
                                    }
                                }
                            }


                        }
                    }
                });

        return datalist;
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
            Utils.getSearchDialog(getActivity(),datalist,rvListview);

        }
        return super.onOptionsItemSelected(item);

    }

}
