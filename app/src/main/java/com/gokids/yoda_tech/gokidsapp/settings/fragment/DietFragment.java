package com.gokids.yoda_tech.gokidsapp.settings.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.settings.activity.AddKidsActivity;
import com.gokids.yoda_tech.gokidsapp.settings.adapter.DietAdapter;
import com.gokids.yoda_tech.gokidsapp.settings.model.Diet;
import com.gokids.yoda_tech.gokidsapp.settings.model.SpecialNeedBean;
import com.gokids.yoda_tech.gokidsapp.utils.GridItemView;
import com.gokids.yoda_tech.gokidsapp.utils.MyGridView;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DietFragment extends Fragment {

    ArrayList<Diet> diets;
    MyGridView mGridView;
    DietAdapter dietAdapter;
    Button diet_continue;
     static ArrayList<String> selectedStrings= new ArrayList<>();
    public String TAG=  getClass().getName();
    private AddKidsActivity activity;
    private ArrayList<String> list;
    private SpecialNeedBean bean;
    private TextView skipdiet;
    private View rootView;


    public DietFragment() {
        // Required empty public constructor
    }

    public static DietFragment newInstance(ArrayList<String> list) {
        selectedStrings=list;
        Log.e("diet fragment"," i m list size"+selectedStrings.toString());


        Bundle args = new Bundle();

        DietFragment fragment = new DietFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_diet, container, false);
        diet_continue = (Button) rootView.findViewById(R.id.diet_continue);
        skipdiet = (TextView) rootView.findViewById(R.id.skip_diet);
        //AddKidsActivity.setSeekBarProgress(60);
        //selectedStrings = ;
        activity= new AddKidsActivity();
        bean= new SpecialNeedBean();
        //setupData();
       // setAlreadyselecteddata();




        return rootView;
    }

    private void setAlreadyselecteddata() {
        Log.e(TAG,"i m out of if");

        if(selectedStrings.size()>0)
        {
            Log.e(TAG," i m in if list size"+selectedStrings.toString());

            for(int i = 0;i<selectedStrings.size();i++)
            {
                for(int j= 0;j<diets.size();j++) {
                    Log.e(TAG," selected not"+diets.get(j).isSelected());
                   Log.e(TAG," text"+diets.get(j).getDispText());
                   Log.e(TAG," selected text"+ selectedStrings.get(i));

                    if (diets.get(j).getSpecialNeedID().equalsIgnoreCase(selectedStrings.get(i))) {
                       // Log.e(TAG," selected notgrid"+diets.get(j).isSelected());

                        diets.get(j).setSelected(true);
                        Log.e(TAG," selected"+diets.get(j).isSelected());

                        //  Log.e(TAG," status"+diets.get(j).isSelected());
                    }
                }
            }
        }
    }

    public void setupGridView(View rootView) {

        mGridView = (MyGridView) rootView.findViewById(R.id.allergy_gridview);

        mGridView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });
        mGridView.setVerticalScrollBarEnabled(false);


        //setupData();
        dietAdapter = new DietAdapter(getContext(),diets);
        mGridView.setAdapter(dietAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(diets.get(position).isSelected())
                {
                    diets.get(position).setSelected(false);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove(diets.get(position).getSpecialNeedID());
                }
                else
                {
                    diets.get(position).setSelected(true);
                    ((GridItemView) v).display(true);
                    selectedStrings.add(diets.get(position).getSpecialNeedID());
                }
                AddKidsActivity.setDiets(selectedStrings);
                for(int j=0;j<activity.SelectedNeeds.size();j++)
                {
                    Log.e(TAG," selected strings" + activity.SelectedNeeds.get(j));
                }


            }

        });


    }


    public void setupData() {

        diets = new ArrayList<>();
        diets.add(new Diet(R.drawable.diet_lacto_veg,false,"Lacto Vegetarian","SN20"));
        diets.add(new Diet(R.drawable.diet_ovo_veg,false,"Ovo Vegetarian","SN22"));
        diets.add(new Diet(R.drawable.diet_paleo,false, "Paleo","SN23"));
        diets.add(new Diet(R.drawable.diet_pescetarian,false, "Pescetarian","SN24"));
        diets.add(new Diet(R.drawable.diet_vegan,false,"Vegan","SN25"));
        diets.add(new Diet(R.drawable.diet_veg,false,"Vegetarian","SN21"));

    }


    public void setListView() {

        ViewGroup vg = mGridView;
        int totalHeight = 0;
        for (int i = 0; i < dietAdapter.getCount(); i++) {
            View listItem = dietAdapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = mGridView.getLayoutParams();
        par.height = totalHeight/3;
        mGridView.setLayoutParams(par);
        mGridView.requestLayout();

    }


    public void continueButton() {

        diet_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AddKidsActivity.mViewPager.getCurrentItem()!=5) {
                    AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem()+1);
                }
            }
        });
        skipdiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AddKidsActivity.mViewPager.getCurrentItem()!=5) {
                    AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem()+1);
                }
            }
        });
    }
    private boolean _hasLoadedOnce= false; // your boolean field

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
