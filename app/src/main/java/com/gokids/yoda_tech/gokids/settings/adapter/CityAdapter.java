package com.gokids.yoda_tech.gokids.settings.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.activity.FoodDetailActivity;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.entertainment.activity.EntertainmentDetailActivity;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.medical.activity.MedicalDetail;
import com.gokids.yoda_tech.gokids.settings.model.CityBean;
import com.gokids.yoda_tech.gokids.shop.activity.ShopDetailActivity;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {
    ArrayList<CityBean> list;
    Context context;

    public CityAdapter(Context context, ArrayList<CityBean> list) {
        this.list = list;
        this.context= context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_single_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CityBean obj= list.get(position);
        holder.city.setText(obj.getCity());
        Log.e("adapter"," i m in adapter"+list.size()+""+ obj.getCityID());
        holder.cityRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MySharedPrefrence.getPrefrence(context).edit().putString("current_city",obj.getCityID()).commit();
                Intent homeintent = new Intent(context,GoKidsHome.class);
                homeintent.putExtra("flag","3");
                context.startActivity(homeintent);
            }
        });

    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout cityRL;
        private final TextView city;

        public MyViewHolder(View itemView) {
            super(itemView);
            cityRL= (RelativeLayout) itemView.findViewById(R.id.single_row_city);
            city = (TextView) itemView.findViewById(R.id.city);
        }
    }

}
