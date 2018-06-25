package com.gokids.yoda_tech.gokids.settings.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.settings.activity.AddKidsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgeFragment extends Fragment {

   // CircularSeekBar mCircularSeekBar;
    SeekBar mSeekbar;
    TextView seekBarText;
    Button age_continue;
    static String age;
    private String TAG= getClass().getName();


    public AgeFragment() {
        // Required empty public constructor
    }

    public static AgeFragment newInstance(String mAge) {
        age=mAge;
        Bundle args = new Bundle();

        AgeFragment fragment = new AgeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_age, container, false);
        AddKidsActivity.setSeekBarProgress(20);

      //  mCircularSeekBar = (CircularSeekBar) rootView.findViewById(R.id.circularSeekBar);
        mSeekbar = rootView.findViewById(R.id.seek_bar_age);
        seekBarText = rootView.findViewById(R.id.seekBarText);
        age_continue = rootView.findViewById(R.id.age_continue);
        Log.e(TAG,"age in fragment"+age);
        setupSeeks();
       /* if(age>=0) {
            mSeekbar.setProgress(age);
            mCircularSeekBar.setProgress(age);

        }*/
        continueButton();


        return rootView;
    }




    public void setupSeeks() {

/*
        mSeekbar.incrementProgressBy(1);
        mSeekbar.setProgress(0);

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                seekBarText.setText(String.valueOf(i));
               // mCircularSeekBar.setProgress(i);
               // age = i;
               // AddKidsActivity.setAge(age);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCircularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {

                mSeekbar.setProgress(progress);
               // age = progress;
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
*/

    }

    public void continueButton() {

        age_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AddKidsActivity.mViewPager.getCurrentItem()!=5) {
                    AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem()+1);
                }
            }
        });
    }

}
