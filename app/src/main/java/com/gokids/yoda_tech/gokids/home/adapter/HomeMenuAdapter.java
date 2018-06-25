package com.gokids.yoda_tech.gokids.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.activity.FoodListActivity;
import com.gokids.yoda_tech.gokids.ecommerce.EcommerceClassActivity;
import com.gokids.yoda_tech.gokids.ecommerce.EcommerceMainActivity;
import com.gokids.yoda_tech.gokids.entertainment.activity.EntertainmentActivity;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.learn.activity.LearnActivity;
import com.gokids.yoda_tech.gokids.medical.activity.MedicalMainActivty;
import com.gokids.yoda_tech.gokids.shop.activity.Shopping;
import com.gokids.yoda_tech.gokids.sos.SosActivity;
import com.gokids.yoda_tech.gokids.utils.DetailActivity;

import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 16/05/17.
 */

public class HomeMenuAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Integer> intIDs;
    private String[] menuNames;

    public HomeMenuAdapter(Context context, ArrayList<Integer> ids, String[] menuNames){
        mContext = context;
        intIDs = ids;
        this.menuNames=menuNames;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View rootView = inflater.inflate(R.layout.list_home_menu, null );

        ImageView imageView = rootView.findViewById(R.id.menuItemImage);
        LinearLayout row = rootView.findViewById(R.id.parent_LL);
        TextView menuTextview = rootView.findViewById(R.id.menu_name);
        imageView.setImageResource(intIDs.get(i));
        menuTextview.setText(menuNames[i]);
        if (i > 5) {
            row.setBackgroundColor(mContext.getResources().getColor( R.color.pale_white));


        }
        rootView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, GoKidsHome.height1));

        final Integer position = i;

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0 ||position==1||position==2||position==3||position==4||position==5) {
                        Intent intent = new Intent(mContext, getClassFromPos(position));
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("pos",position);
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
                return EntertainmentActivity.class;
            case 3:
                return MedicalMainActivty.class;
            case 4:
                return SosActivity.class;
            case 5:
                return EcommerceClassActivity.class;
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
