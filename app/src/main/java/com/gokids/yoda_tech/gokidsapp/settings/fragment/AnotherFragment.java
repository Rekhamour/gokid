package com.gokids.yoda_tech.gokidsapp.settings.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokidsapp.settings.activity.AddKidsActivity;
import com.gokids.yoda_tech.gokidsapp.settings.activity.SettingsActivity;
import com.gokids.yoda_tech.gokidsapp.settings.model.SpecialNeedBean;
import com.gokids.yoda_tech.gokidsapp.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokidsapp.utils.Urls;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.StreamPart;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnotherFragment extends Fragment {


    Button another_continue;
    private LinearLayout noLL;
    private LinearLayout yesLL;
    private String TAG = getClass().getName();
    private AddKidsActivity activity;
    private SpecialNeedBean bean;
    private String flag;
    private TextView noTv;
    private TextView yesTv;
    private boolean _hasLoadedOnce=false
            ;


    public AnotherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String url = "api/addDeleteKidDetail/email/:email/age/:age/gender/:gender/specialNeed/:specialNeed/kidID/:kidID";
        String another="api/addDeleteKidDetail/email/meme/age/7/gender/M/specialNeed/ [ { \"SpecialNeed\": \"SN2\" }, { \"SpecialNeed\": \"SN3\" }, { \"SpecialNeed\": \"SN4\" } ]/kidID/-";
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_another, container, false);
        another_continue = (Button) rootView.findViewById(R.id.another_continue);
        yesLL = (LinearLayout) rootView.findViewById(R.id.yes);
        noLL = (LinearLayout) rootView.findViewById(R.id.no);
        yesTv = (TextView) rootView.findViewById(R.id.yes_tv);
        noTv = (TextView) rootView.findViewById(R.id.no_tv);
        activity= new AddKidsActivity();
        bean= new SpecialNeedBean();

        AddKidsActivity.setSeekBarProgress(100);
        yesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="y";
                yesTv.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_selected_inanother));
                yesTv.setTextColor(getActivity().getResources().getColor(R.color.white));
                noTv.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_circle_text_default));
                noTv.setTextColor(getActivity().getResources().getColor(R.color.black));
                AddKidsActivity.YesNo= flag;
                redirect();



            }
        });
        noLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="N";
                noTv.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_selected_inanother));
                noTv.setTextColor(getActivity().getResources().getColor(R.color.white));
                yesTv.setTextColor(getActivity().getResources().getColor(R.color.black));

                yesTv.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_circle_text_default));
                AddKidsActivity.YesNo= flag;
                redirect();



            }
        });


        continueButton();

        return rootView;

    }

    public void continueButton() {

        another_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AddKidsActivity.mViewPager.getCurrentItem()<4) {
                    AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem()+1);
                }
                else
                {



                }
            }
        });
    }


    private void redirect() {
        if(flag.equalsIgnoreCase("Y"))
        {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            intent.putExtra("flag","0");
            startActivity(intent);
            getActivity().finish();

            //AddKidsActivity.mViewPager.setCurrentItem(0);
        }
        else if(flag.equalsIgnoreCase("N"))
        {
            /*Intent intent = new Intent(getActivity(), SettingsActivity.class);
            intent.putExtra("flag","1");
            startActivity(intent);*/
            getActivity().finish();

        }
    }


    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                AddKidsActivity.setSeekBarProgress(100);

                _hasLoadedOnce = true;
            }
        }
    }


}
