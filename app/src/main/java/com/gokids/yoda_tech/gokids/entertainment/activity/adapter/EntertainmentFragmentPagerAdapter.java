package com.gokids.yoda_tech.gokids.entertainment.activity.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.gokids.yoda_tech.gokids.entertainment.activity.DaytripsFragment;
import com.gokids.yoda_tech.gokids.entertainment.activity.EntertainFragment;
import com.gokids.yoda_tech.gokids.utils.Utils;

public class EntertainmentFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 7;
   String month= Utils.getMonth();
    private String tabTitles[] = new String[] { month + " Events", "Day Trips and Sightseeing", "Events and Shows","Indoor Entertainment","Outdoor Play","Summer Camp","Tours and City Walk"};
    private String tabcategories[] = new String[] { "CATSPE", "CAT8", "CAT11","CAT13","CAT18","CAT19","CAT9"};
    private Context context;

    public EntertainmentFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("adapter","tab title"+tabcategories[position]);
        Fragment selectedFragment = null;



      switch (position) {
            case 0:
                selectedFragment = EntertainFragment.newInstance(tabcategories[0],tabTitles[0]);
                break;
            case 1:
                selectedFragment = DaytripsFragment.newInstance(tabcategories[1],tabTitles[1]);
                break;
            case 2:
                selectedFragment = EventsShows.newInstance(tabcategories[2],tabTitles[2]);
                break;
            case 3:
                selectedFragment = FourthEntertainment.newInstance(tabcategories[3],tabTitles[3]);
                break;
            case 4:
                selectedFragment = OuterEntertainment.newInstance(tabcategories[4],tabTitles[4]);
                break;
            case 5:
                selectedFragment = SummerEntertainment.newInstance(tabcategories[5],tabTitles[5]);

                break;
          case 6:
              selectedFragment = ToursEntertainment.newInstance(tabcategories[6],tabTitles[6]);

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
