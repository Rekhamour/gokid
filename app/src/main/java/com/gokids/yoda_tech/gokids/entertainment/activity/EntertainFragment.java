package com.gokids.yoda_tech.gokids.entertainment.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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
import com.gokids.yoda_tech.gokids.entertainment.activity.adapter.EntertainlistAdapter;
import com.gokids.yoda_tech.gokids.entertainment.activity.adapter.EntertainmentListAdapter;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;

/**
 * Created by benepik on 23/6/17.
 */

public class EntertainFragment extends Fragment {
    RecyclerView food_rv_list;
    TextView numFoods;
    EntertainmentListAdapter adapter;
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
    private String flagfirst="y";
    private LinearLayout calendarLL;

    private boolean isLoaded =false,isVisibleToUser;
    private Handler handler;
    private ProgressDialog dialog;


    public static EntertainFragment newInstance(String tabcategory, String tabTitle){
        EntertainFragment itemOnFragment = new EntertainFragment();
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
        total= getTotalRestaurants(Utils.getCurrentdate());
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        food_rv_list.setLayoutManager(layoutManager);
        adapter = new EntertainmentListAdapter(getActivity(),list, "y");
        food_rv_list.setAdapter(adapter);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        latlon= Utils.getLatLong(getActivity());
        Log.e(TAG,"Latlon lat" + latlon.getLatitude() + " long " + latlon.getLongitude());
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        calendarLL.setVisibility(View.GONE);

        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView).build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                dte=Utils.getselecteddate(date);
                Log.e(TAG," date"+ dte);

                total= getTotalRestaurants(dte);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        load(0);

                    }
                },200);
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

        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please wait..");
        latlon= Utils.getLatLong(getActivity());
        handler= new Handler();

        adapter.setLoadMoreListener(new EntertainmentListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                food_rv_list.post(new Runnable() {
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
        food_rv_list.setAdapter(adapter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load(0);

            }
        },200);

        return view;

    }

    private void load(int index) {


       // dialog.show();
        getRestaurants(dte,prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",index,0);

    }

    private void loadMore(int index) {
        MainBean bean=new MainBean();
        bean.setType("load");
        list.add(bean);
        adapter.notifyItemInserted(list.size()+1);
        getRestaurants(dte,prefrence.getString("emailId",""),latlon.getLatitude(),latlon.getLongitude(),"Distance",index,0);

    }
    public int getTotalRestaurants(final String date) {
        String getTotals= Urls.BASE_URL+"/api/categoryTotalCount/category/CLS3/subCategory/" +"CATSPE"+ "/startDate/"+date+"/endDate/"+Utils.getLastofMonth();
        Log.e(TAG," total items"+ getTotals);
        Ion.with(getActivity())
                .load(getTotals)
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


        String dte= date;
        PATH= BASE_URL + "api/viewAllEntertainments/latitude/"+latlon.getLatitude()+"/longitude/"+latlon.getLongitude()+"/category/"+"CATSPE"+"/startDate/"+dte+"/endDate/"+dte+"/limitStart/"+ start+"/count/"+(start+50)+"/sortBy/Distance";
        Log.e(TAG,"path in else"+PATH);

        Log.e(TAG,"tab category   "+mtabcategory);
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
                                            m.setType("data");
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

                                        adapter.notifyDataChanged();
                                    }
                                }



                            }

                        }
                    }
                });

        return list;
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
            Utils.getfilterDistanceEntertainment(getActivity(),list,popup,adapter);
        } else if (item.getItemId() == R.id.lens_search) {

            Utils.getSearchDialogEntertainment(getActivity(),list,food_rv_list,flagfirst);

        }
        return super.onOptionsItemSelected(item);

    }

}
