package com.gokids.yoda_tech.gokidsapp.entertainment.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.eat.model.MainBean;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.Entertainment;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.EntertainmentCategoryBean;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.EntertainmentDetailActivity;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class EntertainCategoriesAdapter extends RecyclerView.Adapter<EntertainCategoriesAdapter.MyViewHolder> {

    ArrayList<EntertainmentCategoryBean> list;
    private ItemClickCallback itemClickCallback;
    Context ctx;


    public EntertainCategoriesAdapter(Context ctx, ArrayList<EntertainmentCategoryBean> list){
        this.list = list;
        this.ctx= ctx;
    }
    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemCallback(final ItemClickCallback itemCallback) {
        this.itemClickCallback = itemCallback;
        if(itemClickCallback != null);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.entertain_category_low, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        EntertainmentCategoryBean m = list.get(position);
        holder.cat_name.setText(m.getCategory());
        Picasso.with(ctx).load(m.getImageURL()).into(holder.cat_img);
        holder.cate_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ADapter","position"+position);
                Intent intententer = new Intent(ctx, Entertainment.class);
                intententer.putExtra("position",position);
                ctx.startActivity(intententer);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout cate_row;
        private final ImageView cat_img;
        private final TextView cat_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            cate_row = (RelativeLayout) itemView.findViewById(R.id.cate_row);
            cat_img = (ImageView) itemView.findViewById(R.id.cat_img);
            cat_name = (TextView) itemView.findViewById(R.id.cat_name);

        }
    }

}
