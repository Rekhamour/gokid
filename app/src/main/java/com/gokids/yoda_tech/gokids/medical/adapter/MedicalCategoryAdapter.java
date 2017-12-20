package com.gokids.yoda_tech.gokids.medical.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gokids.yoda_tech.gokids.medical.activity.Myfragment;


public class MedicalCategoryAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 7;
    private String tabTitles[] = new String[] { "Doctor", "Clinic", "Hospital", "Pharmacy", "Child Care"};
    private Context context;

    public MedicalCategoryAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment= new Myfragment();
        return fragment;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
