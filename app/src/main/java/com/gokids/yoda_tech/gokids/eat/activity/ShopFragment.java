package com.gokids.yoda_tech.gokids.eat.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.adapter.FoodAdapter;


/**
 * Created by benepik on 23/6/17.
 */

public class ShopFragment  extends Fragment implements FoodAdapter.ItemClickCallback  {
    RecyclerView shop_rv_list;
    TextView numshops;
    public static ShopFragment newInstance(){
        ShopFragment itemOnFragment = new ShopFragment();
        return itemOnFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_shop, container, false);
       // Intent intententer = new Intent(getActivity(), Shopping.class);
        //getActivity().startActivity(intententer);
        return view;
    }

    @Override
    public void onItemClick(int p) {

    }
}
