package com.gokids.yoda_tech.gokids.signup.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.home.service.GPSService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {

    public static final int FINE_LOCATION_REQUEST_CODE = 3424;

    public static final int COARSE_LOCATION_REQUEST_CODE = 3452;

    public static final String RECEIVE_BROADCAST = "com.gokids.yoda_tech.gokidsapp.signup";

    String city;

    SharedPreferences prefs;

    LocalBroadcastManager manager;
    BroadcastReceiver receiver;
    TextView homePageClick;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    Integer tabSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        gettingkeyhash();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        requestPermission();
        startService();
        setupBroadcast();
        setupTabLayout();
        setupHomePageClick();




    }
    private void gettingkeyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.facebook.samples.loginhowto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    public void setupBroadcast(){

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(RECEIVE_BROADCAST)) {
                    city = prefs.getString("city", "c");
                    Log.v("city", city);
                    displayToast("Your city is "+ city);
                }
            }
        };

        manager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(RECEIVE_BROADCAST);
        manager.registerReceiver(receiver,filter);

    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(SignUpActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                FINE_LOCATION_REQUEST_CODE
        );
    }

    public void setupHomePageClick(){

        homePageClick = (TextView) findViewById(R.id.homeClick);
        homePageClick.setText(R.string.browse);
        homePageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,GoKidsHome.class);
                intent.putExtra("flag","2");
                startActivity(intent);
            }
        });

    }

    public void setupTabLayout(){


        mTabLayout = (TabLayout) findViewById(R.id.sign_tab);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_signup);
        mTabLayout.setupWithViewPager(mViewPager);

        TabLayout.Tab signup = mTabLayout.newTab().setText("Sign Up");
        TabLayout.Tab signIn = mTabLayout.newTab().setText("Sign In");

        mTabLayout.addTab(signIn);
        mTabLayout.addTab(signup);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelected = tab.getPosition();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("level",tab.getPosition());
                editor.apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        final SignInFragment signInFragment = new SignInFragment();
        final SignUpFragment signUpFragment = new SignUpFragment();

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return signInFragment;
                    case 1:
                        return signUpFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "Sign In";
                    case 1:
                        return "Sign Up";
                    default:
                        return null;
                }
            }
        };

        mViewPager.setAdapter(adapter);


    }

    public void startService(){
        Intent intent = new Intent(this, GPSService.class);
        startService(intent);
    }

    public void displayToast(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
