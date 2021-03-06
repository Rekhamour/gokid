package com.gokids.yoda_tech.gokids.sos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gokids.yoda_tech.gokids.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 4/4/2018.
 */

public class InfantFragment extends Fragment {


    private RecyclerView listView;
    private LinearLayoutManager lm;
    private InfantsAdapter adapter;
    public ArrayList<String> list=new ArrayList<>();
    public ArrayList<String> namelist=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.infant_layout,null,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listView = view.findViewById(R.id.infnats_list);
        lm=new LinearLayoutManager(getActivity());
        listView.setLayoutManager(lm);
        preparePath();
        adapter= new InfantsAdapter(getActivity(),list,namelist);
        listView.setAdapter(adapter);
    }

    private void preparePath() {
       // String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.v1;

        list.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v1);
        list.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v2);
        list.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v3);
        list.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v4);
        list.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v5);
        list.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v6);
        namelist.add("Primary survey Babies");
        namelist.add("How to give baby CPR");
        namelist.add("How to treat a chocking baby");
        namelist.add("Recovery position babies");
        namelist.add("How to treat Allergic reacttions");
        namelist.add("How to treat fever");
    }
}
