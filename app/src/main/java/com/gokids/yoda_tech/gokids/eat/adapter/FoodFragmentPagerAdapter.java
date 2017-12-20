package com.gokids.yoda_tech.gokids.eat.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gokids.yoda_tech.gokids.eat.activity.EntertainmentFragment;
import com.gokids.yoda_tech.gokids.eat.activity.FoodFragment;
import com.gokids.yoda_tech.gokids.eat.activity.ShopFragment;

public class FoodFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Food","Shop","Entertainment"};
    private Context context;

    public FoodFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment selectedFragment = null;// FoodFragment.newInstance();


        switch (position) {
            case 0:
             selectedFragment = FoodFragment.newInstance();
                break;
            case 1:
                selectedFragment = ShopFragment.newInstance();

                break;
            case 2:
                selectedFragment = EntertainmentFragment.newInstance();


                break;


        }
        return selectedFragment;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
