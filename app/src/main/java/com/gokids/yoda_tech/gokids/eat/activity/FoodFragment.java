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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodListAdapter;
import com.gokids.yoda_tech.gokids.eat.adapter.HintAdapter;
import com.gokids.yoda_tech.gokids.eat.model.Contact;
import com.gokids.yoda_tech.gokids.eat.model.CuisinesBean;
import com.gokids.yoda_tech.gokids.eat.model.HintBean;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.RecyclerItemClickListener;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
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
    private static Context context;
    static RecyclerView food_rv_list;
    TextView numFoods;
    static FoodListAdapter mfoodAdapter;
    static ArrayList<MainBean> Foodlist;
    String  category= "Eat";
    public static int  mCount =  0;
    Context ctx;
    private boolean loading = true;
    LinearLayoutManager layoutManager;
    private static String TAG = "Foodfragment";
    private int total;
    private SwipeRefreshLayout swipe_food;
    private SharedPreferences prefrence;
    private int countlimit = 50;
    private Location latlon;
    private static ArrayList<CuisinesBean> con;
    private ArrayList<HintBean> hintlist= new ArrayList<HintBean>();
    private ImageView btnLocation;
    private ImageView btnList;
    private FrameLayout frameMap;
    private FoodMapFragment fm;
    private HintAdapter adapter;
    private LinearLayoutManager lm;
    private RecyclerView hintlistview;
    private static DecimalFormat df = new DecimalFormat(".##");
    private static ProgressDialog dialog;
    private Handler handler;
    private TextWatcher mTextwatcher;
    private TextView no_result;
    public static String place_id;


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
        food_rv_list = view.findViewById(R.id.food_rv_list);
        swipe_food = view.findViewById(R.id.swipe_food);
        numFoods = view. findViewById(R.id.food_num);
        no_result = view. findViewById(R.id.no_result);
        frameMap = view. findViewById(R.id.frame_in_food_fragment);
        btnList = view. findViewById(R.id.btn_list);
        btnLocation = view. findViewById(R.id.btn_location);
        fm= new FoodMapFragment();
        context= getActivity();
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
        getRestaurants("-",prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",0,index+50);

    }

    private void loadMore(int index) {
        MainBean bean=new MainBean();
        bean.setType("load");
        Foodlist.add(bean);
        mfoodAdapter.notifyItemInserted(Foodlist.size()+1);
        getRestaurants("-",prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",index,index+50);

    }


    public int getTotalRestaurants(final String category) {
        Ion.with(getActivity())
                .load(Urls.BASE_URL+"api/categoryTotalCount/category/CLS1/subCategory/-"+"/city/"+ MySharedPrefrence.getPrefrence(context).getString("current_city",""))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {

                            if (result.get("status").getAsString().equals("200")) {
                                total = result.get("result").getAsJsonArray().get(0).getAsJsonObject().get("TOTAL_COUNT").getAsInt();
                            }
                            String category_actual = "Restaurants";

                            numFoods.setText(total + " " + category_actual);
                        }
                    }
                });
        return  total;
    }



    public static ArrayList<MainBean>  getRestaurants(final String category, final String name, final double lat, final double longi, final String sortBy, final int start, final int count){
         dialog.show();
        String PATH= Urls.BASE_URL + "api/viewAllRestaurants/latitude/"+lat+"/longitude/"+longi+"/email/"+name+"/limitStart/"+start+"/count/"+count+"/sortBy/"+sortBy+"/searchBy/"+category+"/city/"+ MySharedPrefrence.getPrefrence(context).getString("current_city","");
        Log.e(TAG,"path"+PATH);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PATH, null, new Response.Listener<JSONObject>() {
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
                                    m.setRestaurantID(obj.getString("RestaurantID"));
                                    m.setRestaurantName(obj.getString("RestaurantName"));
                                    m.setRestaurantSubName(obj.getString("RestaurantSubName"));
                                    m.setSpecialty(obj.getString("Specialty"));
                                    m.setWebsite(obj.getString("Website"));
                                    m.setEmail(obj.getString("Email"));
                                    m.setAddress(obj.getString("Address"));
                                    m.setPostal(obj.getString("Postal"));
                                    m.setLatlong(obj.getString("LatLong"));
                                    m.setPrice(obj.getString("Price"));
                                    m.setKidsfinityScore(obj.getDouble("KidsfinityScore"));
                                    m.setDistance(obj.getString("Distance"));
                                    m.setWorkingHour(obj.getString("WorkingHour"));
                                    m.setType("data");
                                    // ArrayList<CuisinesBean> spe = new ArrayList<>();
                                    // if(obj.getStringhas("Specialization") && obj.getString("Specialization").isJsonArray()) {
                                    // JsonArray spec = obj.getString("Cuisines").getAsJsonArray();
                                    if (obj.get("Cuisines") instanceof JSONArray) {

                                        if (obj.getJSONArray("Cuisines").length() > 0) {
                                            con = new ArrayList<>();

                                            JSONArray cont = obj.getJSONArray("Cuisines");
                                            for (int j = 0; j < cont.length(); j++) {
                                                CuisinesBean c = new CuisinesBean();

                                                c.setCuisine(cont.getJSONObject(j).getString("Cuisine"));
                                                c.getCuisine();
                                                Log.e(TAG, "cousine " + c.getCuisine().toString());

                                                con.add(c);
                                            }
                                            Log.e(TAG, "con array size" + con.toString());

                                            m.setCuisines(con);
                                            Log.e(TAG, "cousine " + m.getCuisines().toString());

                                        }
                                    }
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
                                    if(obj.has("Images"))
                                    {
                                        if (obj.get("Images") instanceof JSONArray) {

                                            ArrayList<String> images = new ArrayList<String>();
                                            if (obj.getJSONArray("Images").length() > 0) {
                                                for (int j = 0; j < obj.getJSONArray("Images").length(); j++) {
                                                    //System.out.println(obj.getString("Images").getAsJsonArray().get(j).getString("ImageURL"));
                                                    images.add(obj.getJSONArray("Images").getJSONObject(j).getString("ImageURL"));
                                                }
                                                Log.e(TAG, "images array size" + images.size());
                                                m.setImages(images);
                                            }


                                        }
                                        else
                                        {
                                            m.setImages(new ArrayList<String>(0));
                                        }

                                    }
                                    Foodlist.add(m);
                                    mCount++;
                                }
                                mfoodAdapter.notifyDataChanged();

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


/*        Ion.with(context)
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
                                            m.setRestaurantID(obj.getString("RestaurantID").getAsString());
                                            m.setRestaurantName(obj.getString("RestaurantName").getAsString());
                                            m.setRestaurantSubName(obj.getString("RestaurantSubName").getAsString());
                                            m.setSpecialty(obj.getString("Specialty").getAsString());
                                            m.setWebsite(obj.getString("Website").getAsString());
                                            m.setEmail(obj.getString("Email").getAsString());
                                            m.setAddress(obj.getString("Address").getAsString());
                                            m.setPostal(obj.getString("Postal").getAsString());
                                            m.setLatlong(obj.getString("LatLong").getAsString());
                                            m.setPrice(obj.getString("Price").getAsString());
                                            m.setKidsfinityScore(obj.getString("KidsfinityScore").getAsDouble());
                                            m.setDistance(obj.getString("Distance").getAsString());
                                            m.setWorkingHour(obj.getString("WorkingHour").getAsString());
                                            m.setType("data");
                                            // ArrayList<CuisinesBean> spe = new ArrayList<>();
                                            // if(obj.getStringhas("Specialization") && obj.getString("Specialization").isJsonArray()) {
                                            // JsonArray spec = obj.getString("Cuisines").getAsJsonArray();

                                            if (obj.getString("Cuisines").isJsonArray()) {
                                                con = new ArrayList<>();

                                                JsonArray cont = obj.getString("Cuisines").getAsJsonArray();
                                                for (int j = 0; j < cont.size(); j++) {
                                                    CuisinesBean c = new CuisinesBean();

                                                    c.setCuisine(cont.get(j).getString("Cuisine").getAsString());
                                                    c.getCuisine();
                                                    Log.e(TAG, "cousine " + c.getCuisine().toString());

                                                    con.add(c);
                                                }
                                                Log.e(TAG, "con array size" + con.toString());

                                                m.setCuisines(con);
                                                Log.e(TAG, "cousine " + m.getCuisines().toString());

                                            }

                                            if (obj.getString("Contacts").isJsonArray()) {
                                                ArrayList<Contact> hr = new ArrayList<>();
                                                JsonArray cont = obj.getString("Contacts").getAsJsonArray();
                                                for (int j = 0; j < cont.size(); j++) {
                                                    Contact c = new Contact();
                                                    c.setContactId(cont.get(j).getString("ContactID").getAsLong());
                                                    c.setOwnerId(cont.get(j).getString("OwnerID").getAsString());
                                                    c.setPhoneNo(cont.get(j).getString("PhoneNo").getAsString());
                                                    hr.add(c);
                                                }
                                                m.setContacts(hr);
                                            }

                                            ArrayList<String> images = new ArrayList<String>();
                                            if (obj.getString("Images").isJsonArray()) {
                                                for (int j = 0; j < obj.getString("Images").getAsJsonArray().size(); j++) {
                                                    //System.out.println(obj.getString("Images").getAsJsonArray().get(j).getString("ImageURL").getAsString());
                                                    images.add(obj.getString("Images").getAsJsonArray().get(j).getString("ImageURL").getAsString());
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
                            Log.e(TAG,"API EXception"+e.getMessage());


                            Toast.makeText(context, "oops something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

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
                        dialog.show();
                        Foodlist.clear();

                        getRestaurants("-",prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"AscPrice",0,50);
                    } else if (item.getItemId() == R.id.high_to_low) {

                        Log.e("log", "i m in high to low sort");
                        dialog.show();
                        Foodlist.clear();
                        getRestaurants("-",prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"DescPrice",0,50);


                    } else if (item.getItemId() == R.id.kidfinity_Score) {

                        Log.e("log", "i m in sort score");
                        dialog.show();
                        Foodlist.clear();

                        getRestaurants("-",prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Score",0,50);
                    } else if (item.getItemId() == R.id.distance) {

                        Log.e("log", "i m in sort distance here");
                        dialog.show();
                        Foodlist.clear();
                        getRestaurants("-",prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",0,50);


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
            final EditText queryTv= dialogView.findViewById(R.id.queryText);
             hintlistview= dialogView.findViewById(R.id.search_hint_list);
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
                                queryTv.setText(hintlist.get(position).getmDescription());

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
            Button searchbtn= dialogView.findViewById(R.id.searchText);
            searchbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String query = queryTv.getText().toString().toLowerCase();

                    final ArrayList<MainBean> filteredList = new ArrayList<>();
                    filteredList.clear();

                    for (int i = 0; i < Foodlist.size(); i++) {
                        final String restaurantName = Foodlist.get(i).getRestaurantName().toLowerCase();

                        final String location = Foodlist.get(i).getAddress().toLowerCase();
                        if (restaurantName.contains(query)) {
                            Log.e(TAG, " resaurant name" + query);
                            filteredList.add(Foodlist.get(i));
                        } else if (location.contains(query)) {
                            Log.e(TAG, " location name" + query);

                            filteredList.add(Foodlist.get(i));
                        }
                    }
                    if (filteredList.size() == 0) {
                        Foodlist.clear();
                        food_rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mfoodAdapter = new FoodListAdapter(getActivity(),Foodlist);
                        food_rv_list.setAdapter(mfoodAdapter);
                            if(Utils.getLatLongUsingGoogle(getActivity(),place_id, "FoodFragment", food_rv_list, query)) {
                            }
                            alertDialog.dismiss();

                    } else {
                        food_rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                        FoodListAdapter mAdapter = new FoodListAdapter(getActivity(), filteredList);
                        food_rv_list.setAdapter(mAdapter);
                        mAdapter.notifyDataChanged();
                        alertDialog.dismiss();
                   }




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
                                            HintBean bean= new HintBean();
                                            JSONObject obj = predictions.getJSONObject(i);
                                            String description = obj.getString("description");
                                            bean.setmDescription(description);
                                            if(obj.has("place_id")) {
                                                place_id = obj.getString("place_id");
                                                bean.setmPlaceId(place_id);
                                            }
                                            hintlist.add(bean);

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
