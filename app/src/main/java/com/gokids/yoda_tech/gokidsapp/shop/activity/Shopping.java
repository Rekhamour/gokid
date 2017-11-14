package com.gokids.yoda_tech.gokidsapp.shop.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokidsapp.shop.activity.adapter.ShopFragmentPagerAdapter;

public class Shopping extends AppCompatActivity {
    final int PAGE_COUNT = 6;
    private String tabTitles[] = new String[] { "Apparel & Aceessories", "Education", "Kids Wear","Maternity","Shoes","Toys & Gifts"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        getSupportActionBar().setTitle("Apparels");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ShopFragmentPagerAdapter(getSupportFragmentManager(),
                Shopping.this));

        // Give the TabLayout the ViewPager
      //  TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.shopping_toolbar);
        tabsStrip.setViewPager(viewPager);
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
     /*  setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Appereals");*/


        /*BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.shopping_navigation);*/
/*

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = ApperealsFragment.newInstance();
                        switch (item.getItemId()) {
                            case R.id.appereals:
                                selectedFragment = ApperealsFragment.newInstance();
                                break;
                            case R.id.education:
                                // selectedFragment = ShopFragment.newInstance();
                                break;
                            case R.id.kidswear:
                                break;
                            case R.id.maternity:
                                break;
                            case R.id.toy:
                                break;

                        }
                        FragmentTransaction transaction = getSupportFragmentManager().
                                beginTransaction();
                        transaction.replace(R.id.shppoingframe_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
*/

        //Manually displaying the first fragment - one time only
        //FragmentTransaction transaction = getSupportFragmentManager().
               // beginTransaction();
        //transaction.replace(R.id.shppoingframe_layout, ApperealsFragment.newInstance(position + 1));
        //transaction.commit();
    }


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            Intent intent= new Intent(Shopping.this, GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
        }
        return true;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_act_menu, menu);
        return true;
    }
}
