package com.gokids.yoda_tech.gokids.settings.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.settings.activity.AddKidsActivity;
import com.gokids.yoda_tech.gokids.settings.adapter.CuisineAdapter;
import com.gokids.yoda_tech.gokids.settings.model.Cuisine;
import com.gokids.yoda_tech.gokids.settings.model.SpecialNeedBean;
import com.gokids.yoda_tech.gokids.utils.GridItemView;
import com.gokids.yoda_tech.gokids.utils.MyGridView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CuisineFragment extends Fragment {

    MyGridView mGridView;
    CuisineAdapter cuisineAdapter;
    Button cuisine_continue;
    ArrayList<Cuisine> cuisines;
    private static ArrayList<String> selectedStrings= new ArrayList<>();
    private String TAG =  getClass().getName();
    public ArrayList<String> list;
    private AddKidsActivity activity;
    private SpecialNeedBean bean;
    private TextView cuisineSkip;
    private boolean _hasLoadedOnce= false;
    private View rootView;


    public CuisineFragment() {
        // Required empty public constructor
    }

    public static CuisineFragment newInstance(ArrayList<String> list) {
        selectedStrings=list;
        Log.e("in cusine","in cusine lise size"+selectedStrings);

        Bundle args = new Bundle();

        CuisineFragment fragment = new CuisineFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         rootView = inflater.inflate(R.layout.fragment_cuisine, container, false);
        cuisine_continue = rootView.findViewById(R.id.cuisine_continue);
        cuisineSkip = rootView.findViewById(R.id.skip_cuisine);
       // AddKidsActivity.setSeekBarProgress(80);
        activity=  new AddKidsActivity();
        bean =  new SpecialNeedBean();


        //setupData();
        //setAlreadyselecteddata();
        //setupGridView(rootView);
        //setListView();
        //continueButton();



        return rootView;
    }

    private void setAlreadyselecteddata() {
        Log.e(TAG,"i m out of if");

        if(selectedStrings.size()>0) {
            Log.e(TAG," i m in if list size"+selectedStrings.toString());

            for (int i = 0; i < selectedStrings.size(); i++) {
                for (int j = 0; j < cuisines.size(); j++) {
                    if (cuisines.get(j).getSpecialNeedID().equalsIgnoreCase(selectedStrings.get(i))) {
                        cuisines.get(j).setSelected(true);
                        Log.e(TAG," in status"+cuisines.get(j).isSelected());

                    }
                }
            }
        }
    }

    public void setupGridView(View rootView) {

        mGridView = rootView.findViewById(R.id.allergy_gridview);





        //setupData();
        cuisineAdapter = new CuisineAdapter(getContext(),cuisines);
        mGridView.setAdapter(cuisineAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(cuisines.get(position).isSelected())
                {
                    cuisines.get(position).setSelected(false);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove(cuisines.get(position).getSpecialNeedID());
                }
                else
                {
                    cuisines.get(position).setSelected(true);
                    ((GridItemView) v).display(true);
                    selectedStrings.add(cuisines.get(position).getSpecialNeedID());
                }
                AddKidsActivity.setCuisines(selectedStrings);
                for(int j = 0; j< AddKidsActivity.SelectedNeeds.size(); j++)
                {
                    Log.e(TAG," selected strings" + AddKidsActivity.SelectedNeeds.get(j));
                }



            }

        });


    }


    public void setupData() {

        cuisines = new ArrayList<>();
        cuisines.add(new Cuisine(R.drawable.cuisine_american,false,"American","SN1"));
        cuisines.add(new Cuisine(R.drawable.cuisine_asian,false,"Asian","SN3"));
        cuisines.add(new Cuisine(R.drawable.cuisine_chinese,false, "Chinese","SN7"));
        cuisines.add(new Cuisine(R.drawable.cuisine_cuban,false, "Cuban","SN10"));
        cuisines.add(new Cuisine(R.drawable.cuisine_english,false,"English","SN8"));
        cuisines.add(new Cuisine(R.drawable.cuisine_french,false,"French","SN11"));
        cuisines.add(new Cuisine(R.drawable.cuisine_greek,false,"Greek","SN12"));
        cuisines.add(new Cuisine(R.drawable.cuisine_indian,false,"Indian","SN13"));
        cuisines.add(new Cuisine(R.drawable.cuisine_irish,false,"Irish","SN16"));
        cuisines.add(new Cuisine(R.drawable.cuisine_italian,false,"Italian","SN14"));
        cuisines.add(new Cuisine(R.drawable.cuisine_japanese,false,"Japanese","SN17"));
        cuisines.add(new Cuisine(R.drawable.mediterranean,false,"Mediterranean ","SN18"));
        cuisines.add(new Cuisine(R.drawable.cuisine_mexican,false,"Mexican","SN9"));
        cuisines.add(new Cuisine(R.drawable.cuisine_thai,false,"Thai","SN2"));
        cuisines.add(new Cuisine(R.drawable.turkish,false,"Turkish","SN34"));

    }


    public void setListView() {

        ViewGroup vg = mGridView;
        int totalHeight = 0;
        for (int i = 0; i < cuisineAdapter.getCount(); i++) {
            View listItem = cuisineAdapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = mGridView.getLayoutParams();
        par.height = totalHeight/3;
        mGridView.setLayoutParams(par);
        mGridView.requestLayout();

    }


    public void continueButton() {

        cuisine_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"at cuisine"+AddKidsActivity.mViewPager.getCurrentItem());
                if(AddKidsActivity.mViewPager.getCurrentItem()<4) {
                    AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem()+1);
                }
            }
        });
        cuisineSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AddKidsActivity.mViewPager.getCurrentItem()<4) {
                    AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem()+1);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                setupData();
                setAlreadyselecteddata();
                setupGridView(rootView);
                setListView();
                continueButton();
                _hasLoadedOnce = true;
            }
        }
    }

}
