package com.gokids.yoda_tech.gokidsapp.eat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.gokids.yoda_tech.gokidsapp.eat.activity.EntertainmentFragment;
import com.gokids.yoda_tech.gokidsapp.eat.activity.FoodFragment;
import com.gokids.yoda_tech.gokidsapp.eat.activity.FoodListActivity;
import com.gokids.yoda_tech.gokidsapp.eat.activity.ShopFragment;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.EntertainFragment;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.Entertainment;
import com.gokids.yoda_tech.gokidsapp.shop.activity.ApperealsFragment;
import com.gokids.yoda_tech.gokidsapp.shop.activity.EducationFragment;
import com.gokids.yoda_tech.gokidsapp.shop.activity.KidsWearFragment;
import com.gokids.yoda_tech.gokidsapp.shop.activity.MaternityFragment;
import com.gokids.yoda_tech.gokidsapp.shop.activity.ShoeFragment;
import com.gokids.yoda_tech.gokidsapp.shop.activity.Shopping;
import com.gokids.yoda_tech.gokidsapp.shop.activity.ToyFragment;

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
