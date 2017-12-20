package com.gokids.yoda_tech.gokids.shop.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.astuetz.PagerSlidingTabStrip;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.shop.activity.adapter.ShopFragmentPagerAdapter;

public class Shopping extends AppCompatActivity {
    final int PAGE_COUNT = 6;
    private String tabTitles[] = new String[] { "Apparel & Accessories", "Education", "Kids Wear","Maternity","Shoes","Toys & Gifts"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        getSupportActionBar().setTitle(tabTitles[0]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ShopFragmentPagerAdapter(getSupportFragmentManager(),
                Shopping.this));

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.shopping_toolbar);
        tabsStrip.setViewPager(viewPager);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                getSupportActionBar().setTitle(tabTitles[position]);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_act_menu, menu);
        return true;
    }
}
