package com.gokids.yoda_tech.gokids.settings.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.settings.activity.AddKidsActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenderFragment extends Fragment {

    boolean boyClick = false;
    static int count = 1;
    boolean girlClick = false;
   static boolean click=false;
    public static String mgender = "";
    private TextView boyText;
    private TextView girlText;
    public static String GENDER="gender";
    public static String mAge="0";
    public static String mAboutkid="";


    public GenderFragment() {
        // Required empty public constructor
    }


    public static GenderFragment newInstance(String gender, String age, String aboutkid) {
        GenderFragment fragment = new GenderFragment();

            mgender=gender;
            mAge=age;
        mAboutkid=aboutkid;

        Bundle args = new Bundle();
        Log.e("TAG in fragment","GENDER"+gender);
        args.putString(GENDER,gender);

      //  setlayout(gender);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mgender= getArguments().getString(GENDER);
        View rootView = inflater.inflate(R.layout.fragment_gender, container, false);

        Bundle saved= getArguments();
       // mgender=saved.getString(GENDER);

        // Inflate the layout for this fragment
        Log.e("TAG in fragment","gender in reate"+mgender);
        Log.e("TAG in fragment","age in reate"+mAge);

        setupLinearClickListener(rootView);
        settingsgenderdetails();
        setAgedetails(rootView);
        AddKidsActivity.setAge(mAge);



        // AddKidsActivity.setSeekBarProgress(0);

        return rootView;
    }

    private void setAgedetails(View rootView) {

        final Spinner agespinner= (Spinner)rootView.findViewById(R.id.spinner_age);
        final TextView kidsdetailtext= (TextView)rootView.findViewById(R.id.about_kid);
        kidsdetailtext.setText(mAboutkid);
       // agespinner.setPrompt("Select your kid age");
        //agespinner.setSelection();

        String[]ags= getActivity().getResources().getStringArray(R.array.agearray);
        for(int i=0 ;i<ags.length;i++)
        {
            if(mAge.equalsIgnoreCase(ags[i]))
            {
                agespinner.setSelection(i);
            }
        }

        agespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Age= agespinner.getItemAtPosition(position).toString();
              //  agespinner.setSelection(parent.getPosition("Category 2"));

                if(mAge.equalsIgnoreCase(String.valueOf(position+1)))
                {
                    agespinner.setSelection(position,true);
                    //agespinner.setSelection();

                    //agespinner.setSelection(position,true);
                }
                if(position==0)
                {
                    AddKidsActivity.setAge(" ");


                }
               // Log.e("Age in gender","age in gender"+agespinner.getItemAtPosition(position).toString());
               else {
                    AddKidsActivity.setAge(agespinner.getItemAtPosition(position).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       // int Age= Integer.parseInt(agespinner.getSelectedItem().toString());
       // Log.e("Age in gender","age in gender"+agespinner.getSelectedItem().toString());
       // AddKidsActivity.setAge(agespinner.getSelectedItem().toString());
    }

    private void settingsgenderdetails() {
        if(mgender.equalsIgnoreCase("M")) {
            boyClick = true;
            girlClick = false;
            boyText.setBackground(getResources().getDrawable(R.drawable.bg_blue));
            girlText.setBackground(getResources().getDrawable(R.drawable.bg_default));
            // AddKidsActivity.setSeekBarProgress(20);

        }
        else if(mgender.equalsIgnoreCase("F"))
        {
            girlClick = true;
            boyClick = false;
            girlText.setBackground(getResources().getDrawable(R.drawable.bg_selected));
            boyText.setBackground(getResources().getDrawable(R.drawable.bg_default));
             AddKidsActivity.setSeekBarProgress(20);


        }
        if(!mgender.isEmpty())
        {
            AddKidsActivity.setGender(mgender);
        }
    }

    public void setupLinearClickListener(View rootView) {

        LinearLayout boyLayout = (LinearLayout) rootView.findViewById(R.id.layout_boy);
        LinearLayout girlLayout = (LinearLayout) rootView.findViewById(R.id.layout_girl);
        TextView skipgender = (TextView) rootView.findViewById(R.id.skip_gender);

        boyText = (TextView) rootView.findViewById(R.id.add_kid_boy_text);

        girlText = (TextView) rootView.findViewById(R.id.add_kid_girl_text);

        Button continueButton = (Button) rootView.findViewById(R.id.continueButton);

        boyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boyClick = true;
                girlClick = false;
                boyText.setBackground(getResources().getDrawable(R.drawable.bg_blue));
                girlText.setBackground(getResources().getDrawable(R.drawable.bg_default));
               // boyText.setBackgroundColor(getResources().getColor(R.color.red_500));
               // girlText.setBackgroundColor(getResources().getColor(R.color.cafe_americano));
                AddKidsActivity.setGender("M");

            }
        });

        girlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                girlClick = true;
                boyClick = false;
                girlText.setBackground(getResources().getDrawable(R.drawable.bg_selected));
                boyText.setBackground(getResources().getDrawable(R.drawable.bg_default));

               // girlText.setBackgroundColor(getResources().getColor(R.color.red_500));
                //boyText.setBackgroundColor(getResources().getColor(R.color.cafe_americano));
                AddKidsActivity.setGender("F");

            }
        });


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AddKidsActivity.mViewPager.getCurrentItem()!=5) {
                    AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem()+1);
                }
            }
        });
        skipgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AddKidsActivity.mViewPager.getCurrentItem()!=5) {
                    AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem()+1);
                }
            }
        });
    }

}
