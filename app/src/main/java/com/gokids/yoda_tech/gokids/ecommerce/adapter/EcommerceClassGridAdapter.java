package com.gokids.yoda_tech.gokids.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.ecommerce.EcommercProductsListActivity;
import com.gokids.yoda_tech.gokids.ecommerce.EcommerceClothingActivity;
import com.gokids.yoda_tech.gokids.ecommerce.EcommerceMainActivity;
import com.gokids.yoda_tech.gokids.ecommerce.model.EcommercProductBean;
import com.gokids.yoda_tech.gokids.settings.model.Allergy;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class EcommerceClassGridAdapter extends RecyclerView.Adapter<EcommerceClassGridAdapter.MyViewHolder> {
    ArrayList<EcommercProductBean> list;
    Context context;

    public EcommerceClassGridAdapter(Context context, ArrayList<EcommercProductBean> list) {
        this.list = list;
        this.context= context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_image_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final EcommercProductBean obj= list.get(position);
        holder.itemName.setText(obj.getProductClass());
        if(position==0)
       holder.itemImage.setImageResource(R.drawable.ic_allergy_free);
        if(position==1)
        {
            holder.itemImage.setImageResource(R.drawable.ic_clothing);
        }
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0) {
                    Intent intent = new Intent(context, EcommerceMainActivity.class);
                    context.startActivity(intent);
                }
                if(position==1) {
                    Intent intent = new Intent(context, EcommerceClothingActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        Log.e("adapter"," i m in adapter"+list.size()+""+ obj.getProductClassID());
      holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
                    Intent intent = new Intent(context, EcommerceMainActivity.class);
                    context.startActivity(intent);
                }
                if(position==1) {
                    Intent intent = new Intent(context, EcommerceClothingActivity.class);
                    context.startActivity(intent);
                }
            }
        });

    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final TextView itemName;
        private final LinearLayout rowLL;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemImage= itemView.findViewById(R.id.settings_imageitem);
            itemName = itemView.findViewById(R.id.free_text);
            rowLL = itemView.findViewById(R.id.row_LL);
        }
    }

}
