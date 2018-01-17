package com.gokids.yoda_tech.gokids.eat.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodAdapter;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodListAdapter;
import com.gokids.yoda_tech.gokids.eat.adapter.HintAdapter;
import com.gokids.yoda_tech.gokids.eat.model.Contact;
import com.gokids.yoda_tech.gokids.eat.model.CuisinesBean;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.RecyclerItemClickListener;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by benepik on 23/6/17.
 */

public class FoodFragment extends Fragment  {
    RecyclerView food_rv_list;
    TextView numFoods;
    FoodListAdapter mfoodAdapter;
    ArrayList<MainBean> Foodlist;
    String  category= "Eat";
    public int  mCount =  0;
    Context ctx;
    private boolean loading = true;
    LinearLayoutManager layoutManager;
    private static final String BASE_URL = "http://52.77.82.210/";
    private String TAG = getClass().getName();
    private int total;
    private SwipeRefreshLayout swipe_food;
    private SharedPreferences prefrence;
    private int countlimit = 50;
    private Location latlon;
    private ArrayList<CuisinesBean> con;
    private ArrayList<String> hintlist= new ArrayList<>();
    private ImageView btnLocation;
    private ImageView btnList;
    private FrameLayout frameMap;
    private FoodMapFragment fm;
    private HintAdapter adapter;
    private LinearLayoutManager lm;
    private RecyclerView hintlistview;
    private static DecimalFormat df = new DecimalFormat(".##");
    private ProgressDialog dialog;
    private Handler handler;
    private TextWatcher mTextwatcher;
    private TextView no_result;


    public static FoodFragment newInstance(){
        FoodFragment itemOnFragment = new FoodFragment();
        df.setRoundingMode(RoundingMode.UP);

        return itemOnFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_food, container, false);
        prefrence = getActivity().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,Context.MODE_PRIVATE);
        food_rv_list = (RecyclerView) view.findViewById(R.id.food_rv_list);
        swipe_food = (SwipeRefreshLayout) view.findViewById(R.id.swipe_food);
        numFoods = (TextView)view. findViewById(R.id.food_num);
        no_result = (TextView)view. findViewById(R.id.no_result);
        frameMap = (FrameLayout)view. findViewById(R.id.frame_in_food_fragment);
        btnList = (ImageView)view. findViewById(R.id.btn_list);
        btnLocation = (ImageView)view. findViewById(R.id.btn_location);
        fm= new FoodMapFragment();
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLocation.setImageDrawable(getResources().getDrawable(R.drawable.white_location));
                btnLocation.setBackgroundColor(getResources().getColor(R.color.black));
                btnList.setImageDrawable(getResources().getDrawable(R.drawable.list));
                btnList.setBackgroundColor(getResources().getColor(R.color.white));
                frameMap.setVisibility(View.VISIBLE);
                if(getActivity().getFragmentManager().getBackStackEntryCount()==0)
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_in_food_fragment,fm,"map fragment").addToBackStack(null).commit();
               // getActivity().getFragmentManager().beginTransaction().addToBackStack(null);
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnList.setImageDrawable(getResources().getDrawable(R.drawable.list_white));
                btnList.setBackgroundColor(getResources().getColor(R.color.black));
                btnLocation.setImageDrawable(getResources().getDrawable(R.drawable.black_location));
                btnLocation.setBackgroundColor(getResources().getColor(R.color.white));
                getActivity().getFragmentManager().popBackStack();
                //getActivity().getFragmentManager().beginTransaction().remove(new FoodMapFragment()).commit();

                frameMap.setVisibility(View.GONE);

            }
        });
        ctx= getActivity();
       setHasOptionsMenu(true);
        Utils.getCurrentdate();
       total= getTotalRestaurants(category);
        Foodlist = new ArrayList<>();
       // swipe_food.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        food_rv_list.setLayoutManager(layoutManager);
        mfoodAdapter = new FoodListAdapter(getActivity(),Foodlist);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please wait..");
        latlon= Utils.getLatLong(getActivity());
        handler= new Handler();

        mfoodAdapter.setLoadMoreListener(new FoodListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                food_rv_list.post(new Runnable() {
                    @Override
                    public void run() {
                       final int index = Foodlist.size();
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
        food_rv_list.setAdapter(mfoodAdapter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load(0);

            }
        },200);

        return view;

    }

    private void load(int index) {


       dialog.show();
        getRestaurants(category,prefrence.getString("emailId",""),latlon.getLongitude(),latlon.getLongitude(),"Distance",0,countlimit);

    }

    private void loadMore(int index) {
        MainBean bean=new MainBean();
        bean.setType("load");
        Foodlist.add(bean);
        mfoodAdapter.notifyItemInserted(Foodlist.size()+1);
        getRestaurants(category,prefrence.getString("emailId",""),latlon.getLongitude(),latlon.getLongitude(),"Distance",index,countlimit);

    }


    public int getTotalRestaurants(final String category) {
        Ion.with(getActivity())
                .load("http://52.77.82.210/api/categoryTotalCount/category/CLS1/subCategory/-")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {

                            if (result.getAsJsonObject().get("status").getAsString().equals("200")) {
                                total = result.getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject().get("TOTAL_COUNT").getAsInt();
                            }
                            String category_actual = "Restaurants";

                            numFoods.setText(total + " " + category_actual);
                        }
                    }
                });
        return  total;
    }



    public ArrayList<MainBean> getRestaurants(final String category, final String name, final double lat, final double longi, final String sortBy, final int start, final int count){

        String PATH= BASE_URL + "api/viewAllRestaurants/latitude/"+latlon.getLatitude()+"/longitude/"+latlon.getLongitude()+"/email/"+name+"/limitStart/"+start+"/count/"+(start+50)+"/sortBy/Distance/searchBy/-";
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
                            dialog.dismiss();
                            if (status.equalsIgnoreCase("200")) {
                                Log.e(TAG, " i m if status" + status);


                                Log.e("Foodfragment", "status" + status);
                                String message = String.valueOf(result.get("message"));
                                Log.e("Foodfragment", "message" + message);
                                JsonArray res = result.getAsJsonArray("result");
                                if (result.has("result")) {
                                    Log.e(TAG,"result size"+res.size());
                                    if (res.size() > 0) {
                                        for (int i = 0; i < res.size(); i++) {
                                            MainBean m = new MainBean();
                                            JsonElement obj = res.get(i);
                                            m.setRestaurantID(obj.getAsJsonObject().get("RestaurantID").getAsString());
                                            m.setRestaurantName(obj.getAsJsonObject().get("RestaurantName").getAsString());
                                            m.setRestaurantSubName(obj.getAsJsonObject().get("RestaurantSubName").getAsString());
                                            m.setSpecialty(obj.getAsJsonObject().get("Specialty").getAsString());
                                            m.setWebsite(obj.getAsJsonObject().get("Website").getAsString());
                                            m.setEmail(obj.getAsJsonObject().get("Email").getAsString());
                                            m.setAddress(obj.getAsJsonObject().get("Address").getAsString());
                                            m.setPostal(obj.getAsJsonObject().get("Postal").getAsString());
                                            m.setLatlong(obj.getAsJsonObject().get("LatLong").getAsString());
                                            m.setPrice(obj.getAsJsonObject().get("Price").getAsString());
                                            m.setKidsfinityScore(obj.getAsJsonObject().get("KidsfinityScore").getAsDouble());
                                            m.setDistance(obj.getAsJsonObject().get("Distance").getAsString());
                                            m.setWorkingHour(obj.getAsJsonObject().get("WorkingHour").getAsString());
                                            m.setType("data");
                                            // ArrayList<CuisinesBean> spe = new ArrayList<>();
                                            // if(obj.getAsJsonObject().has("Specialization") && obj.getAsJsonObject().get("Specialization").isJsonArray()) {
                                            // JsonArray spec = obj.getAsJsonObject().get("Cuisines").getAsJsonArray();

                                            if (obj.getAsJsonObject().get("Cuisines").isJsonArray()) {
                                                con = new ArrayList<>();

                                                JsonArray cont = obj.getAsJsonObject().get("Cuisines").getAsJsonArray();
                                                for (int j = 0; j < cont.size(); j++) {
                                                    CuisinesBean c = new CuisinesBean();

                                                    c.setCuisine(cont.get(j).getAsJsonObject().get("Cuisine").getAsString());
                                                    c.getCuisine();
                                                    Log.e(TAG, "cousine " + c.getCuisine().toString());

                                                    con.add(c);
                                                }
                                                Log.e(TAG, "con array size" + con.toString());

                                                m.setCuisines(con);
                                                Log.e(TAG, "cousine " + m.getCuisines().toString());

                                            }
                                            //m.setCuisines(con);


                                            //}
                                            //m.setCuisines(con);
                                            // m.setCuisines(spe);
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
                                            Foodlist.add(m);
                                            mCount++;
                                        }
                                        mfoodAdapter.notifyDataChanged();

                                    }
                                }
                            }



                        }
                        else{


                            Toast.makeText(getActivity(), "oops something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return Foodlist;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(FoodListActivity.drawerLayout.isDrawerOpen(Gravity.START)) {
                FoodListActivity.drawerLayout.closeDrawer(Gravity.START);
            }
            else {
                FoodListActivity.drawerLayout.openDrawer(Gravity.START);
            }
        }
        else if(item.getItemId()==R.id.house_search)
        {
          //  getActivity().startActivity(new Intent(getActivity(), GoKidsHome.class));
            Intent intent= new Intent(getActivity(), GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
            getActivity().finish();

        } else if (item.getItemId() == R.id.filter_search) {
            PopupMenu popup = new PopupMenu(getActivity(), getActivity().findViewById(R.id.filter_search));
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.low_to_high) {
                        Log.e("log", "i m in low to high ");
                        Collections.sort(Foodlist, new Comparator<MainBean>() {
                            @Override
                            public int compare(MainBean lhs, MainBean rhs) {

                                return lhs.getPrice().compareTo(rhs.getPrice());


                            }
                        });
                        mfoodAdapter.notifyDataSetChanged();
                    } else if (item.getItemId() == R.id.high_to_low) {

                        Log.e("log", "i m in high to low sort");
                        Collections.sort(Foodlist, new Comparator<MainBean>() {
                            @Override
                            public int compare(MainBean lhs, MainBean rhs) {

                                return lhs.getPrice().compareTo(rhs.getPrice());


                            }
                        });

                        //layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
                       // layoutManager.setStackFromEnd(true);
                       // layoutManager.setReverseLayout(true);

                       // food_rv_list.setLayoutManager(layoutManager);
                        Collections.reverse(Foodlist);
                        mfoodAdapter.notifyDataSetChanged();


                    } else if (item.getItemId() == R.id.kidfinity_Score) {

                        Log.e("log", "i m in sort score");
                        Collections.sort(Foodlist, new Comparator<MainBean>() {
                            @Override
                            public int compare(MainBean lhs, MainBean rhs) {

                                return String.valueOf(lhs.getKidsfinityScore()).compareTo(String.valueOf(rhs.getKidsfinityScore()));


                            }
                        });
                        Collections.reverse(Foodlist);

                        mfoodAdapter.notifyDataSetChanged();

                    } else if (item.getItemId() == R.id.distance) {

                        Log.e("log", "i m in sort distance here");
                        Collections.sort(Foodlist, new Comparator<MainBean>() {
                            @Override
                            public int compare(MainBean lhs, MainBean rhs) {

                                return lhs.getDistance().compareTo(rhs.getDistance());


                            }
                        });
                        mfoodAdapter.notifyDataSetChanged();


                    }
                    return true;
                }
            });

            popup.show();

        } else if (item.getItemId() == R.id.lens_search) {
            final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater(null);
            final View dialogView = inflater.inflate(R.layout.search_layout, null);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
           // builder.setView(R.layout.search_layout);
            final EditText queryTv=(EditText)dialogView.findViewById(R.id.queryText);
             hintlistview=(RecyclerView)dialogView.findViewById(R.id.search_hint_list);
            lm= new LinearLayoutManager(getActivity());
            hintlistview.setLayoutManager(lm);
            hintlist.clear();
            adapter=  new HintAdapter(getActivity(),hintlist);
            hintlistview.setAdapter(adapter);
            mTextwatcher= new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {



                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    hintlist.clear();
                    getHints( s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //getHints( s.toString());



                }
            };
            hintlistview.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            try {
                                queryTv.removeTextChangedListener(mTextwatcher);
                                queryTv.setText(hintlist.get(position));

                            }
                            catch (Exception e)
                            {
                                Log.e(TAG,"exception"+ e.getMessage());

                            }
                            // TODO Handle item click
                        }
                    })
            );

            queryTv.addTextChangedListener(mTextwatcher);
            Button searchbtn=(Button)dialogView.findViewById(R.id.searchText);
            searchbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String query = queryTv.getText().toString().toLowerCase();

                    final ArrayList<MainBean> filteredList = new ArrayList<>();
                    filteredList.clear();

                    for (int i = 0; i < Foodlist.size(); i++) {
                        final String   restaurantName= Foodlist.get(i).getRestaurantName().toLowerCase();

                        final String location   = Foodlist.get(i).getAddress().toLowerCase();
                        if (restaurantName.contains(query)) {
                            Log.e(TAG," resaurant name" + query);
                            filteredList.add(Foodlist.get(i));
                        }
                        else if(location.contains(query))
                        {
                            Log.e(TAG," location name" + query);

                            filteredList.add(Foodlist.get(i));
                        }
                    }
                    if(filteredList.size()==0)
                    {
                        no_result.setVisibility(View.VISIBLE);
                    }
                    food_rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                    FoodAdapter mAdapter = new FoodAdapter(getActivity(),filteredList);
                    food_rv_list.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();




                }
            });



        }
        return super.onOptionsItemSelected(item);

    }

    private void getHints(String searchhint) {
        String apipath = "https://maps.googleapis.com/maps/api/place/queryautocomplete/json?input="+searchhint+"&location=1.3521,103.8198&radius=1000&key=AIzaSyAVFxqmmNDjbLUEZ7mDqN-65VqHtc0xvTk";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apipath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("add result","add result"+response);
                        JSONObject result = null;
                        try {
                            result = new JSONObject(response);
                            Log.e(TAG,"result hint"+result.toString());

                            if (result != null) {
                                if (result.has("predictions")) {

                                    JSONArray predictions = result.getJSONArray("predictions");
                                    if (predictions.length() > 0) {
                                        for (int i = 0; i < predictions.length(); i++) {
                                            JSONObject obj = predictions.getJSONObject(i);
                                            String description = obj.getString("description");
                                            hintlist.add(description);

                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }


}
