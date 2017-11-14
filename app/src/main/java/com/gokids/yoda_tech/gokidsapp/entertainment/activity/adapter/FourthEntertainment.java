package com.gokids.yoda_tech.gokidsapp.entertainment.activity.adapter;


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
import com.gokids.yoda_tech.gokidsapp.eat.model.Contact;
import com.gokids.yoda_tech.gokidsapp.eat.model.CuisinesBean;
import com.gokids.yoda_tech.gokidsapp.eat.model.MainBean;
import com.gokids.yoda_tech.gokidsapp.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokidsapp.utils.Constants;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;

/**
 * Created by benepik on 23/6/17.
 */

public class FourthEntertainment extends Fragment implements FoodAdapter.ItemClickCallback,SwipeRefreshLayout.OnRefreshListener {
    RecyclerView food_rv_list;
    TextView numFoods;
    EntertainlistAdapter adapter;
    ArrayList<MainBean> list;
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
    private static String mtabcategory;
    private static String mtabTitles;
    private int countlimit = 50;
    private Location latlon;
    private HorizontalCalendar horizontalCalendar;
    private String PATH;
    private String dte;
    private LinearLayout calendarLL;
    private boolean isLoaded =false,isVisibleToUser;
    private String flagfirst="";


    public static FourthEntertainment newInstance(String tabcategory, String tabTitle){
        FourthEntertainment itemOnFragment = new FourthEntertainment();
        mtabcategory=tabcategory;
        mtabTitles=tabTitle;
        Log.e("adapter","tab title"+tabcategory);
        Log.e("fragment","tab title"+mtabcategory);

        return itemOnFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_entertainment, container, false);
        prefrence = getActivity().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,Context.MODE_PRIVATE);
        food_rv_list = (RecyclerView) view.findViewById(R.id.entertainment_rv_list);
        swipe_food = (SwipeRefreshLayout) view.findViewById(R.id.swipe_enterainment);
        numFoods = (TextView)view. findViewById(R.id.enterment_num);
        calendarLL = (LinearLayout)view. findViewById(R.id.calendarLL);
        ctx= getActivity();
        setHasOptionsMenu(true);
       total= getTotalRestaurants(category);
        list = new ArrayList<>();
        swipe_food.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        food_rv_list.setLayoutManager(layoutManager);
        adapter = new EntertainlistAdapter(getActivity(),list, flagfirst);
        food_rv_list.setAdapter(adapter);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        latlon= Utils.getLatLong(getActivity());


        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

         horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView).build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                dte=Utils.getselecteddate(date);
                Log.e(TAG," date"+ dte);
                final int startlimit= 0;
                final int count=50;
                swipe_food.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipe_food.setRefreshing(true);
                                        list.clear();
                                        food_rv_list.removeAllViewsInLayout();
                                        getRestaurants(dte,prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",startlimit,count);

                                    }
                                }
                );
               // getRestaurants(dte,prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",startlimit,count);
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Date date, int position) {
                return true;
            }
        });



       /// Utils.getCurrentdate();


            swipe_food.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipe_food.setRefreshing(true);
                                    list.clear();
                                    food_rv_list.removeAllViewsInLayout();
                                    getRestaurants(Utils.getCurrentdate(), prefrence.getString("emailId", ""), latlon.getLatitude(), latlon.getLongitude(), "Distance", mCount, countlimit);
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
                .load("http://52.77.82.210/api/categoryTotalCount/category/CLS3/subCategory/"+mtabcategory)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {

                            if (result.getAsJsonObject().get("status").getAsString().equals("200")) {
                                total = result.getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject().get("TOTAL_COUNT").getAsInt();
                            }
                            String category_actual = "Restaurants";

                            numFoods.setText(total + " " + mtabTitles);
                        }
                    }
                });
        return  total;
    }



    public ArrayList<MainBean> getRestaurants(final String date, final String name, final double lat, final double longi, final String sortBy, final int start, final int count){
        mCount =  start;
        String url = BASE_URL + "api/viewAllRestaurants/";
        url += "/latitude/" + lat;
        url += "/longitude/" + longi;
        url += "/email/" + name;

        url +="/limitStart/"+mCount+"/count/" + count;

        if(name != null){
            url += "/searchBy/" + category ;
        }


            String dte= date;
            PATH= BASE_URL + "api/viewAllEntertainments/latitude/"+latlon.getLatitude()+"/longitude/"+latlon.getLongitude()+"/category/"+mtabcategory+"/startDate/"+dte+"/endDate/"+dte+"/limitStart/"+ mCount+"/count/"+count+"/sortBy/Distance";
            Log.e(TAG,"path in else"+PATH);


        // System.out.println(url);
        Log.e(TAG,"tab category   "+mtabcategory);
        //String PATH= BASE_URL + "api/viewAllEntertainments/latitude/"+latlon.getLatitude()+"/longitude/"+latlon.getLongitude()+"/category/"+mtabcategory+"/startDate/2017-01-09/endDate/2017-30-09"+"/limitStart/"+ mCount+"/count/"+count+"/sortBy/Distance";
        Log.e(TAG,"path"+PATH);

        Ion.with(getActivity())
                .load(PATH)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.e("Entertainment", "result" + result);
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
                                if(result.has("result")) {

                                    if (res.size() > 0) {
                                    for (int i = 0; i < res.size(); i++) {
                                        MainBean m = new MainBean();
                                        JsonElement obj = res.get(i);
                                        m.setEntertainmentID(obj.getAsJsonObject().get("EntertainmentID").getAsString());
                                        m.setEntertainmentTitle(obj.getAsJsonObject().get("EntertainmentTitle").getAsString());
                                        m.setEntertainmentdetail(obj.getAsJsonObject().get("EntertainmentDetail").getAsString());
                                        m.setWebsite(obj.getAsJsonObject().get("Website").getAsString());
                                        m.setEmail(obj.getAsJsonObject().get("Email").getAsString());
                                        m.setAddress(obj.getAsJsonObject().get("Address").getAsString());
                                        m.setPostal(obj.getAsJsonObject().get("Postal").getAsString());
                                        m.setLatlong(obj.getAsJsonObject().get("LatLong").getAsString());
                                        m.setStartDate(obj.getAsJsonObject().get("StartDate").toString());
                                        m.setEndDate(obj.getAsJsonObject().get("EndDate").toString());
                                        m.setEventDate(obj.getAsJsonObject().get("EventDate").toString());
                                        m.setKidsfinityScore(obj.getAsJsonObject().get("KidsfinityScore").getAsInt());
                                        m.setDistance(obj.getAsJsonObject().get("Distance").getAsString());
                                        //   m.setSpecialty(obj.getAsJsonObject().get("Specialty").getAsString());
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
                                        list.add(m);
                                        mCount++;
                                    }

                                    adapter.notifyDataSetChanged();
                                    swipe_food.setRefreshing(false);
                                }
                            }


                                if (total > count) {
                                    Log.d(TAG, "total_posts is greater ");

                                    food_rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                                        int lmCount = mCount + 50;
                                                        int lastcount = count + 50;
                                                        getRestaurants(date, name, latlon.getLatitude(), latlon.getLongitude(), sortBy, lmCount, count + 100);

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
                    }
                });

        return list;
    }


    @Override
    public void onRefresh() {
        list.clear();
        food_rv_list.removeAllViewsInLayout();
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
            //getActivity().startActivity(new Intent(getActivity(), GoKidsHome.class));
            Intent intent= new Intent(getActivity(), GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
            getActivity().finish();

        } else if (item.getItemId() == R.id.filter_search) {
            PopupMenu popup = new PopupMenu(getActivity(), getActivity().findViewById(R.id.filter_search));
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.filter_only_distance, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.filter_distance_only) {

                        Log.e("log", "i m in sort distance here");
                        Collections.sort(list, new Comparator<MainBean>() {
                            @Override
                            public int compare(MainBean lhs, MainBean rhs) {

                                return lhs.getDistance().compareTo(rhs.getDistance());


                            }
                        });
                        adapter.notifyDataSetChanged();

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
            Button searchbtn=(Button)dialogView.findViewById(R.id.searchText);
            searchbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String query = queryTv.getText().toString().toLowerCase();

                    final ArrayList<MainBean> filteredList = new ArrayList<>();

                    for (int i = 0; i < list.size(); i++) {
                        final String   restaurantName= list.get(i).getEntertainmentTitle().toLowerCase();

                        final String location   = list.get(i).getAddress().toLowerCase();
                        if (restaurantName.contains(query)) {
                            Log.e(TAG," resaurant name" + query);
                            filteredList.add(list.get(i));
                        }
                        else if(location.contains(query))
                        {
                            Log.e(TAG," location name" + query);

                            filteredList.add(list.get(i));
                        }
                    }

                    food_rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                    EntertainlistAdapter mAdapter = new EntertainlistAdapter(getActivity(),filteredList, flagfirst);
                    food_rv_list.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();




                }
            });
            /*SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) item.getActionView();
            //searchViewItem.expandActionView();
            searchView.setQueryHint("Search by Name");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setIconifiedByDefault(false);// Do not iconify the widget; expand it by defaul

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    // This is your adapter that will be filtered
                    //projectsAdapter.getFilter().filter(newText);
                    String query = newText.toLowerCase();

                    final ArrayList<MainBean> filteredList = new ArrayList<>();

                    for (int i = 0; i < list.size(); i++) {
                        final String  restaurantName= list.get(i).getEntertainmentTitle().toLowerCase();

                        final String location = list.get(i).getAddress().toLowerCase();
                        if (restaurantName.contains(query)) {

                            filteredList.add(list.get(i));
                        }
                        else if(location.contains(query))
                        {
                            filteredList.add(list.get(i));
                        }
                    }

                    food_rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                    EntertainlistAdapter mAdapter = new EntertainlistAdapter(getActivity(),filteredList);
                    food_rv_list.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();  // data set changed

                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    // **Here you can get the value "query" which is entered in the search box.**


                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);*/
        }
        return super.onOptionsItemSelected(item);

    }

}
