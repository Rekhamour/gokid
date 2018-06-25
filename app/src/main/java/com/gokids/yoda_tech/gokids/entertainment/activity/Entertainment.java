package com.gokids.yoda_tech.gokids.entertainment.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.entertainment.activity.adapter.EntertainmentFragmentPagerAdapter;
import com.gokids.yoda_tech.gokids.utils.Utils;

import java.util.Calendar;

public class Entertainment extends AppCompatActivity implements SearchView.OnQueryTextListener {
    String month= Utils.getMonth();
    private String tabTitles[] = new String[] { month + " Events", "Day Trips and Sightseeing", "Events and Shows","Indoor Entertainment","Outdoor Play","Summer Camp","Tours and City Walk"};
    private Intent intent;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment);
        intent=getIntent();
        pos=intent.getIntExtra("position",0);
        getSupportActionBar().setTitle(tabTitles[pos]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);
        ViewPager viewPager = findViewById(R.id.entertainment_viewpager);
        viewPager.setAdapter(new EntertainmentFragmentPagerAdapter(getSupportFragmentManager(),
                Entertainment.this));

        // Give the TabLayout the ViewPager
      //  TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        PagerSlidingTabStrip tabsStrip = findViewById(R.id.entertainment_tabs);

        tabsStrip.setViewPager(viewPager);
        viewPager.setCurrentItem(pos);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

/** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
              //  if(position==0)
                //{
                    getSupportActionBar().setTitle(tabTitles[position]);
               // }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_act_menu, menu);
        MenuItem filtermenu= menu.findItem(R.id.filter_search);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.lens_search).getActionView();
//        searchView.setIconified(true);
        // searchView.setOnQueryTextListener(this);

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
}
