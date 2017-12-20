package com.gokids.yoda_tech.gokids.eat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;


import com.astuetz.PagerSlidingTabStrip;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.bookmark.activity.AllBookmarksActivity;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodAdapter;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodFragmentPagerAdapter;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.medical.model.Medical;
import com.gokids.yoda_tech.gokids.referfriend.activity.ReferFriend;
import com.gokids.yoda_tech.gokids.settings.activity.SettingsActivity;
import com.gokids.yoda_tech.gokids.shop.activity.Shopping;
import com.gokids.yoda_tech.gokids.signup.activity.SignUpActivity;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.Utils;

import java.util.ArrayList;

public class FoodListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView food_rv_list;
    FoodAdapter mfoodAdapter;
    ArrayList<Medical> medicals;
    TextView numFoods;
    Intent intent;
    String category, specializ;
    public static DrawerLayout drawerLayout;
    public NavigationView navigation;
    public ActionBarDrawerToggle drawerToggle;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

            initInstances();
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.food_tabs);
         viewPager = (ViewPager) findViewById(R.id.food_viewpager);
        viewPager.setAdapter(new FoodFragmentPagerAdapter(getSupportFragmentManager(),
                FoodListActivity.this));

        tabsStrip.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Intent intententer = new Intent(FoodListActivity.this, Shopping.class);
                    startActivity(intententer);
                } else if (position == 2) {
                    getSupportActionBar().setTitle("Entertainment");

                    //Intent intententer = new Intent(FoodListActivity.this, Entertainment.class);
                    //startActivity(intententer);
                }
                else if (position == 0) {
                    getSupportActionBar().setTitle("Food");

                    //Intent intententer = new Intent(FoodListActivity.this, Entertainment.class);
                    //startActivity(intententer);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void doMySearch(String query) {
        Log.e("calling","calling" + query);
    }


    private void initInstances() {
       Toolbar toolbar = (Toolbar) findViewById(R.id.food_list_toolbar);
       // toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        // setSupportActionBar(toolbar);
       //getSupportActionBar().setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
       getSupportActionBar().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
        getSupportActionBar().setTitle("Food");




        //toolbar.setNavigationIcon(R.drawable.img_hamburger_menu);
      //  toolbar.indi(R.drawable.img_hamburger_menu);

       // getSupportActionBar().

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_food);
        drawerToggle = new ActionBarDrawerToggle(FoodListActivity.this, drawerLayout,toolbar, R.string.hello_world, R.string.hello_world);
         drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerToggle.setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
        drawerToggle.setDrawerIndicatorEnabled(false);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        final SharedPreferences.Editor editor= prefs.edit();
        navigation = (NavigationView) findViewById(R.id.navigation_food);

        Menu menu = navigation.getMenu();
        MenuItem sign_out = menu.getItem(4);
        if(prefs.contains("userId") && prefs.getInt("userId",0)!=0) {
            Log.e("Signin fragment","userId "+  prefs.getInt("userId",0));


        } else {
            sign_out.setTitle("Sign In");
        }


        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_sign_out:
                       /* if (flag.equalsIgnoreCase("1"))
                        {
                            Utils.setSharedPreferenceEmpty(editor);
                            disconnectFromFacebook();
                        }
                        else {*/
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);

                            SharedPreferences.Editor editor= prefs.edit();
                            Utils.setSharedPreferenceEmpty(editor);
                            Intent intent = new Intent(FoodListActivity.this, SignUpActivity.class);
                            startActivity(intent);
                       // }
                        return true;
                    case R.id.nav_settings:
                        Intent settingsIntent = new Intent(FoodListActivity.this, SettingsActivity.class);
                        settingsIntent.putExtra("flag","1");
                        startActivity(settingsIntent);
                        return true;
                    case R.id.nav_refer_friend:
                        Intent referfriendIntent = new Intent(FoodListActivity.this, ReferFriend.class);
                        startActivity(referfriendIntent);
                        return true;
                    case R.id.nav_home_drawer:
                        Intent homeintent = new Intent(FoodListActivity.this,GoKidsHome.class);
                        homeintent.putExtra("flag","3");
                        startActivity(homeintent);
                        return true;

                    case R.id.nav_bookmark:
                        Intent bookintent = new Intent(FoodListActivity.this,AllBookmarksActivity.class);
                        startActivity(bookintent);
                        return true;

                    default:
                        return true;
                }
            }
        });



    }
    public void toggle() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            drawerLayout.openDrawer(GravityCompat.END);
            Log.e("base","clickd");
        }
    }
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        *//*switch (item.getItemId()) {
            case android.R.id.home:

        }*//*
       *//* if(item.getItemId()==R.id.house_search)
        {
            Toast.makeText(FoodListActivity.this, "clicked home", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.filter_search)
        {
            Toast.makeText(FoodListActivity.this, "clicked filter", Toast.LENGTH_SHORT).show();


        }
*//*
        return true;
    }*/


/*
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = FoodFragment.newInstance();;
                        switch (item.getItemId()) {
                            case R.id.food:
                                selectedFragment = FoodFragment.newInstance();
                                break;
                            case R.id.shop:

                               // selectedFragment = ShopFragment.newInstance();
                                break;
                            case R.id.entertainment:
                                Intent intententer = new Intent(FoodListActivity.this, Entertainment.class);
                                startActivity(intententer);
                               // selectedFragment = EntertainmentFragment.newInstance();
                                break;

                        }
                        FragmentTransaction transaction = getSupportFragmentManager().
                                beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });*/

        //Manually displaying the first fragment - one time only
        /*FragmentTransaction transaction = getSupportFragmentManager().
                beginTransaction();
        transaction.replace(R.id.frame_layout, FoodFragment.newInstance());
        transaction.commit();*/



       /* intent = getIntent();
        category = intent.getStringExtra("medical_slecet");
        specializ = intent.getStringExtra("spec_select");

        getTotalRestaurants(category);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.food_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        medicals = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        food_rv_list.setLayoutManager(layoutManager);
        getRestaurants(category,null,1.23,103.23,"Distance",specializ,0,10);
    }

    public void getTotalRestaurants(final String category){
        Ion.with(getApplicationContext())
                .load("http://52.77.82.210/api/categoryTotalCount/category/CLS4/subCategory/" + category)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        int total = 0;
                        if(result.getAsJsonObject().get("status").getAsString().equals("200")){
                            total = result.getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject().get("TOTAL_COUNT").getAsInt();
                        }
                        String category_actual = "Clinic";
                        if(category.equals("CAT15")) {
                            category_actual = "Clinic";
                        }
                        else if(category.equals("CAT14")) {
                            category_actual = "Hospital";
                        }
                        if(category.equals("CAT16")) {
                            category_actual = "Pharmacy";
                        }
                        if(category.equals("CAT17")) {
                            category_actual = "Child Care";
                        }
                        numFoods.setText(total + " " + category_actual);
                    }
                });
    }

    public ArrayList<Medical> getRestaurants(String category, String name, double lat, double longi, String sortBy, String specialization,int start,int count){
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
                        System.out.println(result);
                        JsonArray med = result.getAsJsonObject().get("result").getAsJsonArray();
                        for(int i=0;i<med.size();i++){
                            Medical m = new Medical();
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
                            m.setKidsfinityScore(obj.getAsJsonObject().get("KidsfinityScore").getAsInt());
                            m.setDistance(obj.getAsJsonObject().get("Distance").getAsString());
                            ArrayList<Specialization> spe = new ArrayList<>();
                            if(obj.getAsJsonObject().has("Specialization") && obj.getAsJsonObject().get("Specialization").isJsonArray()) {
                                JsonArray spec = obj.getAsJsonObject().get("Specialization").getAsJsonArray();
                                for (int j = 0; j < spec.size(); j++) {
                                    Specialization s = new Specialization();
                                    s.setSpecializationHC(spec.get(j).getAsJsonObject().get("SpecializationHC").getAsString());
                                    s.setSpecializationId(spec.get(j).getAsJsonObject().get("SpecializationHCID").getAsString());
                                    spe.add(s);
                                }
                            }
                            m.setSpecializations(spe);
                            if(obj.getAsJsonObject().get("Contacts").isJsonArray()){
                                ArrayList<Contact> con = new ArrayList<>();
                                JsonArray cont = obj.getAsJsonObject().get("Contacts").getAsJsonArray();
                                for (int j=0;j<cont.size();j++){
                                    Contact c = new Contact();
                                    c.setContactId(cont.get(j).getAsJsonObject().get("ContactID").getAsLong());
                                    c.setOwnerId(cont.get(j).getAsJsonObject().get("OwnerID").getAsString());
                                    c.setPhoneNo(cont.get(j).getAsJsonObject().get("PhoneNo").getAsString());
                                    con.add(c);
                                }
                                m.setContacts(con);
                            }

                            ArrayList<String> images = new ArrayList<String>();
                            if(obj.getAsJsonObject().get("Images").isJsonArray()){
                                for(int j=0;j<obj.getAsJsonObject().get("Images").getAsJsonArray().size();j++){
                                    //System.out.println(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageURL").getAsString());
                                    images.add(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageURL").getAsString());
                                }
                            }
                            m.setImages(images);
                            medicals.add(m);
                        }
                        //System.out.println(medicals.size()+"-------------");
                        mfoodAdapter = new FoodAdapter(medicals);
                        mfoodAdapter.setItemCallback(FoodListActivity.this);
                        food_rv_list.setAdapter(mfoodAdapter);
                    }
                });
        return medicals;
    }

    @Override
    public void onItemClick(int p) {
        Intent intent = new Intent(FoodListActivity.this, FoodDetailActivity.class).putExtra("medical_data", (Serializable) medicals.get(p));
        startActivity(intent);
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(0);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_act_menu, menu);
        //MenuItem filtermenu= menu.findItem(R.id.filter_search);
       //SearchManager searchManager =
            //    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       // SearchView searchView =
         //  (SearchView) menu.findItem(R.id.lens_search).getActionView();
//        searchView.setIconified(true);
       //searchView.setOnQueryTextListener(this);

      //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String query) {

                //loadHistory(query);

                return true;

            }

        });*/


        return true;
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
