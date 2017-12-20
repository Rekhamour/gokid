package com.gokids.yoda_tech.gokids.shop.activity.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gokids.yoda_tech.gokids.shop.activity.ApperealsFragment;
import com.gokids.yoda_tech.gokids.shop.activity.EducationFragment;
import com.gokids.yoda_tech.gokids.shop.activity.KidsWearFragment;
import com.gokids.yoda_tech.gokids.shop.activity.MaternityFragment;
import com.gokids.yoda_tech.gokids.shop.activity.ShoeFragment;
import com.gokids.yoda_tech.gokids.shop.activity.ToyFragment;

public class ShopFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 6;
    private String tabTitles[] = new String[] { "Apparel & Accessories", "Education", "Kids Wear","Maternity","Shoes","Toys & Gifts"};
    private Context context;

    public ShopFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment selectedFragment = null;


        switch (position) {
            case 0:
                selectedFragment = ApperealsFragment.newInstance();
                break;
            case 1:
                selectedFragment = EducationFragment.newInstance();
                break;
            case 2:
                selectedFragment = KidsWearFragment.newInstance();
                break;
            case 3:
                selectedFragment = MaternityFragment.newInstance();
                break;
            case 4:
                selectedFragment = ShoeFragment.newInstance();
                break;
            case 5:
                selectedFragment = ToyFragment.newInstance();

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
