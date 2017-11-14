package com.gokids.yoda_tech.gokidsapp.eat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.eat.activity.FoodDetailActivity;
import com.gokids.yoda_tech.gokidsapp.eat.model.MainBean;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class HintAdapter extends RecyclerView.Adapter<HintAdapter.MyViewHolder> {

    ArrayList<String> list;
    private ItemClickCallback itemClickCallback;
    Context ctx;


    public HintAdapter(Context ctx, ArrayList<String> list){
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hint_layout_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        String  hint = list.get(position);
        holder.hint_text.setText(hint);


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hint_text;
        public MyViewHolder(View itemView) {
            super(itemView);
            hint_text = (TextView) itemView.findViewById(R.id.hint_text);
        }
    }

}
