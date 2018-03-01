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
import android.view.View;
import android.widget.TextView;


import com.astuetz.PagerSlidingTabStrip;
import com.facebook.login.LoginManager;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.bookmark.activity.AllBookmarksActivity;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodAdapter;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodFragmentPagerAdapter;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.home.util.RoundedImageView;
import com.gokids.yoda_tech.gokids.medical.model.Medical;
import com.gokids.yoda_tech.gokids.referfriend.activity.ReferFriend;
import com.gokids.yoda_tech.gokids.settings.activity.CityActivity;
import com.gokids.yoda_tech.gokids.settings.activity.SettingsActivity;
import com.gokids.yoda_tech.gokids.shop.activity.Shopping;
import com.gokids.yoda_tech.gokids.signup.activity.SignUpActivity;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static DrawerLayout drawerLayout;
    public NavigationView navigation;
    public ActionBarDrawerToggle drawerToggle;
    private ViewPager viewPager;
    private RoundedImageView image;
    private TextView name;

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

                }
                else if (position == 0) {
                    getSupportActionBar().setTitle("Food");


                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }




    private void initInstances() {
       Toolbar toolbar = (Toolbar) findViewById(R.id.food_list_toolbar);

       getSupportActionBar().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
        getSupportActionBar().setTitle("Food");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_food);
        drawerToggle = new ActionBarDrawerToggle(FoodListActivity.this, drawerLayout,toolbar, R.string.hello_world, R.string.hello_world);
         drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerToggle.setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
        drawerToggle.setDrawerIndicatorEnabled(false);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        final SharedPreferences.Editor editor= prefs.edit();
        navigation = (NavigationView) findViewById(R.id.navigation_food);
        final View header = navigation.getHeaderView(0);

        image = (RoundedImageView) header.findViewById(R.id.nav_userImage);
        name = (TextView) header.findViewById(R.id.nav_username);


        Menu menu = navigation.getMenu();
        MenuItem sign_out = menu.getItem(4);
        if(prefs.contains("emailId") && !prefs.getString("emailId","").isEmpty() ) {
            Log.e("Signin fragment","userId "+  prefs.getString("emailId",""));
            name.setText(prefs.getString("userName","Guest"));

            if(prefs.contains("ImageURL") && !prefs.getString("ImageURL","").isEmpty()) {
                Picasso.with(FoodListActivity.this).load(prefs.getString("ImageURL", "")).into(image);
            }
            if(!MySharedPrefrence.getPrefrence(FoodListActivity.this).getString("emailId","").trim().isEmpty()) {
                String path = "https://s3-ap-southeast-1.amazonaws.com/kisimages/User/" + MySharedPrefrence.getPrefrence(FoodListActivity.this).getString("emailId", "") + ".jpg";
                Picasso.with(FoodListActivity.this).load(path).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
            }

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
                        LoginManager.getInstance().logOut();
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
                    case R.id.nav_selectcity:
                        Intent intents= new Intent(FoodListActivity.this, CityActivity.class);
                        startActivity(intents);
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


    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(0);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_act_menu, menu);


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
