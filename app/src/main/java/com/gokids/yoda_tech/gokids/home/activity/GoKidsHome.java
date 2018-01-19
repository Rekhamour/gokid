package com.gokids.yoda_tech.gokids.home.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.bookmark.activity.AllBookmarksActivity;
import com.gokids.yoda_tech.gokids.home.adapter.HomeMenuAdapter;
import com.gokids.yoda_tech.gokids.home.service.GPSService;
import com.gokids.yoda_tech.gokids.home.util.RoundedImageView;
import com.gokids.yoda_tech.gokids.referfriend.activity.ReferFriend;
import com.gokids.yoda_tech.gokids.settings.activity.SettingsActivity;
import com.gokids.yoda_tech.gokids.signup.activity.SignUpActivity;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;


public class GoKidsHome extends AppCompatActivity {

    private HomeMenuAdapter homeMenuAdapter;
    private GridView mGridView;
    public static int height1;
    public static int statusBarHeight;
    public static int actionBarHeight;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView name;
    private RoundedImageView image;
    private String flag ="0";
    private String TAG= getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_kids_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        //setSupportActionBar(toolbar);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        Log.e(TAG," test"+"   testingg");

        setGridViewAdapterForMenu();
        getSizeOfEachRow();
        setICDrawer();
        setUpDrawer();
        setupNavigationClickListener();

        startService();
    }
    public void startService(){
        Intent intent = new Intent(this, GPSService.class);
        startService(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if(mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
                else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
        }

        return true;
    }

    public void setGridViewAdapterForMenu() {

        ArrayList<Integer> intIDS = new ArrayList<>();
        intIDS.add(R.drawable.sample);
        intIDS.add(R.drawable.sample);
        intIDS.add(R.drawable.sample);
        intIDS.add(R.drawable.sample);
        intIDS.add(R.drawable.sample);
        intIDS.add(R.drawable.sample);
        intIDS.add(R.drawable.sample);
        intIDS.add(R.drawable.sample);
        intIDS.add(R.drawable.sample);
        String[] menuNames = {"Eat","Shopping","Entertain","Medical","SOS","Buy","Learn","Advertise","Advice"};

        homeMenuAdapter = new HomeMenuAdapter(GoKidsHome.this, intIDS,menuNames);

        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setAdapter(homeMenuAdapter);


    }


    public void getSizeOfEachRow() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height1 = metrics.heightPixels;

        int resource = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resource > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resource);
        }

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        height1 = (height1 - statusBarHeight - actionBarHeight) / 3;
    }


    public void setICDrawer() {

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_black_24dp);

        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
        drawable.setColorFilter(Color.parseColor("#ffffff"),mMode);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

    }

    //Set up Drawer and Listener
    public void setUpDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.home);


        mDrawerToggle = new ActionBarDrawerToggle(GoKidsHome.this, mDrawerLayout, 0, 0) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
        mDrawerToggle.setDrawerIndicatorEnabled(false);




    }

    public void setupNavigationClickListener() {

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        final SharedPreferences.Editor editor= prefs.edit();

        Menu menu = mNavigationView.getMenu();
        MenuItem sign_out = menu.getItem(4);
        final View header = mNavigationView.getHeaderView(0);

         name = (TextView) header.findViewById(R.id.nav_username);
         image = (RoundedImageView) header.findViewById(R.id.nav_userImage);
        checkintentvalues();
        //setProfile();


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_sign_out:
                        if (flag.equalsIgnoreCase("1"))
                        {
                            Utils.setSharedPreferenceEmpty(editor);
                            LoginManager.getInstance().logOut();

                            disconnectFromFacebook();

                        }
                        else {
                            Utils.setSharedPreferenceEmpty(editor);
                           // LoginManager.getInstance().logOut();

                            Intent intent = new Intent(GoKidsHome.this, SignUpActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.nav_settings:
                        if(!MySharedPrefrence.getPrefrence(GoKidsHome.this).getString("emailId","").trim().isEmpty()) {
                            Intent settingsIntent = new Intent(GoKidsHome.this, SettingsActivity.class);
                            settingsIntent.putExtra("flag", "1");
                            startActivity(settingsIntent);
                        }
                        else
                       {
                           Utils.getLoginContinue(GoKidsHome.this);
                       // Toast.makeText(GoKidsHome.this, "Please login first", Toast.LENGTH_SHORT).show();
                       }
                        mDrawerLayout.closeDrawer(Gravity.LEFT);

                        return true;
                    case R.id.nav_refer_friend:
                        Intent referfriendIntent = new Intent(GoKidsHome.this, ReferFriend.class);
                        startActivity(referfriendIntent);
                        return true;
                    case R.id.nav_home_drawer:
                        Intent homeintent = new Intent(GoKidsHome.this,GoKidsHome.class);
                        homeintent.putExtra("flag","3");
                        startActivity(homeintent);
                        return true;

                    case R.id.nav_bookmark:
                        Intent bookintent = new Intent(GoKidsHome.this,AllBookmarksActivity.class);
                        startActivity(bookintent);
                        return true;

                    default:
                        return true;
                }
            }
        });



        if(prefs.contains("emailId") && !prefs.getString("emailId","").isEmpty() ) {
            Log.e("Signin fragment","emailId "+  prefs.getString("emailId",""));
            name.setText(prefs.getString("userName","Guest"));
            if(prefs.contains("ImageURL") && !prefs.getString("ImageURL","").isEmpty()) {
                Log.e(TAG," image url"+ prefs.getString("ImageURL",""));
                Picasso.with(GoKidsHome.this).load(prefs.getString("ImageURL", "")).into(image);
            }

        } else {
            sign_out.setTitle("Sign In");
        }


    }

    private void checkintentvalues() {
        flag=getIntent().getStringExtra("flag");
        Log.e("Gokidshome","flag value"+flag);
        if(flag.equalsIgnoreCase("1"))
        {
            setProfile();
        }
    }

    private void setProfile() {
        String jsondata = getIntent().getStringExtra("userProfile");
        Log.w("Jsondata", jsondata);
        try {
          JSONObject  response = new JSONObject(jsondata);
            name.setText(response.get("name").toString());
           JSONObject profile_pic_data = new JSONObject(response.get("picture").toString());
             JSONObject profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            Picasso.with(this).load(profile_pic_url.getString("url"))
                    .into(image);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        name.setText(prefs.getString("userName","Guest"));
        super.onResume();
    }

    public void disconnectFromFacebook() {
        final Boolean[] logout = {false};

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                logout[0] =  true;
                if(logout[0]) {
                    Intent intent = new Intent(GoKidsHome.this, SignUpActivity.class);
                    startActivity(intent);
                }

            }
        }).executeAsync();
    }
}
