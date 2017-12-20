package com.gokids.yoda_tech.gokids.learn.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.learn.Util.SubTopic;
import com.gokids.yoda_tech.gokids.learn.fragment.AcademicFragment;
import com.gokids.yoda_tech.gokids.learn.fragment.ArtsFragment;
import com.gokids.yoda_tech.gokids.learn.fragment.SportsFragment;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {

    public final String LOG_TAG = this.getClass().getSimpleName();
    private int phoneHeight;
    private int phoneWidth;
    public static ArrayList<SubTopic> academics;
    public static ArrayList<SubTopic> arts;
    public static ArrayList<SubTopic> sports;
    Integer tabSelected = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);


        WindowManager.LayoutParams params = getWindow().getAttributes();
        getPhoneDimens();

        params.height = phoneHeight - 40;
        params.width = phoneWidth*3/4;

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);

        TabLayout.Tab tab1 = tabLayout.newTab().setText(getResources().getString(R.string.academics));
        TabLayout.Tab tab2 = tabLayout.newTab().setText(getResources().getString(R.string.arts));
        TabLayout.Tab tab3 = tabLayout.newTab().setText(getResources().getString(R.string.sports));

        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);

        final AcademicFragment academicFragment = new AcademicFragment();
        final ArtsFragment artsFragment = new ArtsFragment();
        final SportsFragment sportsFragment = new SportsFragment();


        FragmentPagerAdapter fragmentPagerAdapter =  new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return academicFragment;
                    case 1:
                        return artsFragment;
                    case 2:
                        return sportsFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return (getResources().getString(R.string.academics));
                    case 1:
                        return getResources().getString(R.string.arts);
                    case 2:
                        return getResources().getString(R.string.sports);
                    default:
                        return null;
                }
            }
        };

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelected = tab.getPosition();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("type",tab.getPosition());
                editor.apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setAdapter(fragmentPagerAdapter);
        CheckIfButtonClicked();
    }


    public void getPhoneDimens(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        phoneHeight = size.x;
        phoneWidth = size.y;

    }


    public void CheckIfButtonClicked(){

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selected = "";
                ArrayList<String> selected1;

                switch (tabSelected){
                    case 0:
                        if(AcademicFragment.topicsAdapter.academic.size()!=0){
                            selected ="";
                            selected1 = new ArrayList<String>();
                            for (int i=0;i<AcademicFragment.topicsAdapter.academic.size();i++){
                                if(AcademicFragment.topicsAdapter.academic.get(i).isSelected()){
                                    selected1.add(AcademicFragment.topicsAdapter.academic.get(i).getTopicName());
                                }

                            }
                            if(selected1.size()!=0){
                                for (int j=0;j<selected1.size();j++){
                                    selected+="\""+selected1.get(j) + "\"";
                                    if(j!=selected1.size()-1){
                                        selected+=",";
                                    }
                                }
                            }
                        }
                        break;
                    case 1:
                        if(ArtsFragment.topicsAdapter.academic.size()!=0){
                            selected ="";
                            selected1 = new ArrayList<String>();
                            for (int i=0;i<ArtsFragment.topicsAdapter.academic.size();i++){
                                if(ArtsFragment.topicsAdapter.academic.get(i).isSelected()){
                                    selected1.add(ArtsFragment.topicsAdapter.academic.get(i).getTopicName());
                                }

                            }
                            if(selected1.size()!=0){
                                for (int j=0;j<selected1.size();j++){
                                    selected+="\""+selected1.get(j) + "\"";
                                    if(j!=selected1.size()-1){
                                        selected+=",";
                                    }
                                }
                            }
                        }
                        break;
                    case 2:
                        if(SportsFragment.topicsAdapter.academic.size()!=0){
                            selected ="";
                            selected1 = new ArrayList<String>();
                            for (int i=0;i<SportsFragment.topicsAdapter.academic.size();i++){
                                if(SportsFragment.topicsAdapter.academic.get(i).isSelected()){
                                    selected1.add(SportsFragment.topicsAdapter.academic.get(i).getTopicName());
                                }

                            }
                            if(selected1.size()!=0){
                                for (int j=0;j<selected1.size();j++){
                                    selected+="\""+selected1.get(j) + "\"";
                                    if(j!=selected1.size()-1){
                                        selected+=",";
                                    }
                                }
                            }
                        }
                        break;

                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("selected",selected);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(),DisplayClassActivity.class);
                startActivity(intent);

            }
        });
    }

    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }
}
