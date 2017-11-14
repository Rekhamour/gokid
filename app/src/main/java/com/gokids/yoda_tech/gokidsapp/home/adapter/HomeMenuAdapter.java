package com.gokids.yoda_tech.gokidsapp.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.eat.activity.FoodListActivity;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.Entertainment;
import com.gokids.yoda_tech.gokidsapp.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokidsapp.learn.activity.LearnActivity;
import com.gokids.yoda_tech.gokidsapp.medical.activity.MedicalMainActivty;
import com.gokids.yoda_tech.gokidsapp.shop.activity.Shopping;

import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 16/05/17.
 */

public class HomeMenuAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Integer> intIDs;

    public HomeMenuAdapter(Context context, ArrayList<Integer> ids){
        mContext = context;
        intIDs = ids;
    }

    @Override
    public int getCount() {
        return intIDs.size();
    }

    @Override
    public Object getItem(int i) {
        return intIDs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View rootView = inflater.inflate(R.layout.list_home_menu, null );

        ImageView imageView = (ImageView) rootView.findViewById(R.id.menuItemImage);
        imageView.setImageResource(intIDs.get(i));


        rootView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, GoKidsHome.height1));

        final Integer position = i;

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0 ||position==1||position==2||position==3||position==6) {
                        Intent intent = new Intent(mContext, getClassFromPos(position));
                        mContext.startActivity(intent);
                    }
                }
            });


        return rootView;
    }

    public Class getClassFromPos(int pos){
        switch (pos){
            case 0:
                return FoodListActivity.class;
            case 1:
                return Shopping.class;
            case 2:
                return Entertainment.class;
            case 3:
                return MedicalMainActivty.class;
            case 4:
                return GoKidsHome.class;
            case 5:
                return GoKidsHome.class;
            case 6:
                return LearnActivity.class;
            case 7:
                return GoKidsHome.class;
            case 8:
                return GoKidsHome.class;
            default:
                return GoKidsHome.class;
        }
    }


}