package com.gokids.yoda_tech.gokids.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.bookmark.activity.AllBookmarksActivity;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceClassGridAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceGridAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceSLiderAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.model.EcommercProductBean;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.home.util.RoundedImageView;
import com.gokids.yoda_tech.gokids.referfriend.activity.ReferFriend;
import com.gokids.yoda_tech.gokids.settings.activity.CityActivity;
import com.gokids.yoda_tech.gokids.settings.activity.SettingsActivity;
import com.gokids.yoda_tech.gokids.settings.model.Allergy;
import com.gokids.yoda_tech.gokids.signup.activity.SignUpActivity;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class EcommerceClassActivity extends AppCompatActivity {

    private RecyclerView listView;
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private ArrayList<Integer> imagesList;
    private GridLayoutManager lm;
    private EcommerceClassGridAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RoundedImageView image;
    private NavigationView navigation;
    private TextView name;
    private ArrayList<EcommercProductBean> BeanArrayList= new ArrayList<>();
    private String TAG="Eclasslist";
    private int currentpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecommerce_class_activity);
        initInstances();
        initView();

    }

    private void initView() {
        viewPager= findViewById(R.id.ecommerce__pager_class);
        listView= findViewById(R.id.ecommerce_list_class);
        setImages();
        setImagesforGrid();
        viewPager.setAdapter(new EcommerceSLiderAdapter(EcommerceClassActivity.this,imagesList));
        indicator = findViewById(R.id.image_indicator_class);
        indicator.setViewPager(viewPager);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                if (currentpage == imagesList.size()) {
                    currentpage = 0;
                }
                viewPager.setCurrentItem(currentpage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
        lm=  new GridLayoutManager(EcommerceClassActivity.this,2);
        adapter = new EcommerceClassGridAdapter(EcommerceClassActivity.this,BeanArrayList);
        listView.setLayoutManager(lm);
        listView.setAdapter(adapter);
    }

    private void setImagesforGrid() {
         String url= Urls.BASE_URL+"api/viewAllProductClass";
        Ion.with(EcommerceClassActivity.this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(e==null)
                        {
                            try {
                                Object json = new JSONTokener(result.toString()).nextValue();
                                if (json instanceof JSONObject)
                                {

                                    Log.e(TAG,"obj out if"+json.toString());

                                    JSONArray arrey=((JSONObject) json).getJSONArray("result");
                                    if(arrey.length()>0) {
                                        for (int i = 0; i < arrey.length(); i++) {
                                            JSONObject obj = arrey.getJSONObject(i);
                                            String ProductClassID = obj.getString("ProductClassID");
                                            String ProductClass = obj.getString("ProductClass");
                                            //String ImageURL = obj.getString("ImageURL");
                                            EcommercProductBean bean= new EcommercProductBean();
                                            bean.setProductClass(ProductClass);
                                            bean.setProductClassID(ProductClassID);
                                           // bean.setImageURL(ImageURL);
                                            BeanArrayList.add(bean);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }

                                }







                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void setImages() {
        imagesList= new ArrayList<>();
        imagesList.add(R.drawable.ad1);
        imagesList.add(R.drawable.ad2);
        imagesList.add(R.drawable.ad3);
        imagesList.add(R.drawable.ad4);
        imagesList.add(R.drawable.ad5);
        imagesList.add(R.drawable.ad6);
        imagesList.add(R.drawable.entad);


    }
    private void initInstances() {
        Toolbar toolbar = findViewById(R.id.ecommerce_mainclass_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
        getSupportActionBar().setTitle("E-Commerce");


        drawerLayout = findViewById(R.id.drawerLayout_ecommerce_class);
        drawerToggle = new ActionBarDrawerToggle(EcommerceClassActivity.this, drawerLayout,toolbar, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerToggle.setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
        drawerToggle.setDrawerIndicatorEnabled(false);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        final SharedPreferences.Editor editor= prefs.edit();
        navigation = findViewById(R.id.navigation_ecommerce_class);
        final View header = navigation.getHeaderView(0);

        image = header.findViewById(R.id.nav_userImage);
        name = header.findViewById(R.id.nav_username);


        Menu menu = navigation.getMenu();
        MenuItem sign_out = menu.getItem(4);
        if(prefs.contains("emailId") && !prefs.getString("emailId","").isEmpty() ) {
            Log.e("Signin fragment","userId "+  prefs.getString("emailId",""));
            name.setText(prefs.getString("userName","Guest"));

            if(prefs.contains("ImageURL") && !prefs.getString("ImageURL","").isEmpty()) {
                Picasso.with(EcommerceClassActivity.this).load(prefs.getString("ImageURL", "")).into(image);
            }
            if(!MySharedPrefrence.getPrefrence(EcommerceClassActivity.this).getString("emailId","").trim().isEmpty()) {
                String path = "https://s3-ap-southeast-1.amazonaws.com/kisimages/User/" + MySharedPrefrence.getPrefrence(EcommerceClassActivity.this).getString("emailId", "") + ".jpg";
                Picasso.with(EcommerceClassActivity.this).load(path).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
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
                        Intent intent = new Intent(EcommerceClassActivity.this, SignUpActivity.class);
                        startActivity(intent);
                        // }
                        return true;

                    case R.id.nav_settings:
                        Intent settingsIntent = new Intent(EcommerceClassActivity.this, SettingsActivity.class);
                        settingsIntent.putExtra("flag","1");
                        startActivity(settingsIntent);
                        return true;
                    case R.id.nav_refer_friend:
                        Intent referfriendIntent = new Intent(EcommerceClassActivity.this, ReferFriend.class);
                        startActivity(referfriendIntent);
                        return true;
                    case R.id.nav_home_drawer:
                        Intent homeintent = new Intent(EcommerceClassActivity.this,GoKidsHome.class);
                        homeintent.putExtra("flag","3");
                        startActivity(homeintent);
                        return true;
                    case R.id.nav_selectcity:
                        Intent intents= new Intent(EcommerceClassActivity.this, CityActivity.class);
                        startActivity(intents);
                        return true;

                    case R.id.nav_bookmark:
                        Intent bookintent = new Intent(EcommerceClassActivity.this,AllBookmarksActivity.class);
                        startActivity(bookintent);
                        return true;

                    default:
                        return true;
                }
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ecommerce_main_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.house)
        {
            Intent intent=  new Intent(EcommerceClassActivity.this,GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
        }
        else if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }
}
