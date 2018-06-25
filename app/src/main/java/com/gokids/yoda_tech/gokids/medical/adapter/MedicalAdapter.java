package com.gokids.yoda_tech.gokids.medical.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class MedicalAdapter extends RecyclerView.Adapter<MedicalAdapter.MyViewHolder> {

    ArrayList<MainBean> list;
    private ItemClickCallback itemClickCallback;

    public MedicalAdapter(ArrayList<MainBean> list){
        this.list = list;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_row, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        MainBean m = list.get(position);
        holder.name.setText(m.getName());
        holder.address.setText(m.getAddress());
//        double dist = m.getDistance()/1000
        holder.dist.setText(m.getDistance() + "\nKms Away");
        //holder.kids.setText(m.getKidsfinityScore() + "");
        Ion.with(holder.img)
                .placeholder(R.drawable.med_error_image)
                .error(R.drawable.med_error_image)
                .load(m.getImages().get(0));
        holder.single_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickCallback.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,dist,address,kids;
        RelativeLayout single_row;
        public MyViewHolder(View itemView) {
            super(itemView);
            single_row = itemView.findViewById(R.id.single_row_container);
            img = itemView.findViewById(R.id.medical_image);
            name = itemView.findViewById(R.id.medical_name);
            dist = itemView.findViewById(R.id.medical_distance);
            address = itemView.findViewById(R.id.medical_address);
            kids = itemView.findViewById(R.id.kidsfinity);
        }
    }
}
