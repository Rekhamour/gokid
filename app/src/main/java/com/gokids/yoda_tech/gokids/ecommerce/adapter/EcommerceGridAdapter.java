package com.gokids.yoda_tech.gokids.ecommerce.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.ecommerce.EcommercProductsListActivity;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.settings.model.Allergy;
import com.gokids.yoda_tech.gokids.settings.model.CityBean;
import com.gokids.yoda_tech.gokids.utils.ItemClickListener;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;

import java.util.ArrayList;
import java.util.List;

import static com.gokids.yoda_tech.gokids.ecommerce.EcommerceMainActivity.selectedallergie;

/**
 * Created by rigoe on 6/3/2017.
 */

public class EcommerceGridAdapter extends RecyclerView.Adapter<EcommerceGridAdapter.MyViewHolder> {
    List<String> list;
    Context context;
    private ItemClickListener clickListener;
    public int pos=0;

    public EcommerceGridAdapter(Context context, List<String> list) {
        this.list = list;
        this.context= context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_image_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String name= list.get(position);
        holder.itemName.setText(name);
        Log.e("adapter"," i m in grid adapterr"+name);
        if(name.equalsIgnoreCase("Gluten Free"))
        {
            Log.e("adapter"," i m in grid adapterr i m in if "+name);

            holder.itemImage.setImageResource(R.drawable.allergy_gluttenfree);

        }
        if(name.equalsIgnoreCase("Egg Free"))
        {
            Log.e("adapter"," i m in grid adapterr i m in if "+name);

            holder.itemImage.setImageResource(R.drawable.allergy_eggfree);


        }
        if(name.equalsIgnoreCase("Dairy Free"))
        {
            Log.e("adapter"," i m in grid adapterr i m in if "+name);

            holder.itemImage.setImageResource(R.drawable.allergy_dairyfree);


        }
        if(name.equalsIgnoreCase("Lactose Free"))
        {
            Log.e("adapter"," i m in grid adapterr i m in if "+name);

            holder.itemImage.setImageResource(R.drawable.allergy_lactosefree);

        }
        if(name.equalsIgnoreCase("Shelfish Free"))
        {
            Log.e("adapter"," i m in grid adapterr i m in if "+name);

            holder.itemImage.setImageResource(R.drawable.allergy_shelfishfree);

        }
        if(name.equalsIgnoreCase("Sesame  Free"))
        {
            Log.e("adapter"," i m in grid adapterr i m in if "+name);

            holder.itemImage.setImageResource(R.drawable.allergy_sesame);

        }
        if(name.equalsIgnoreCase("Shelfish Free"))
        {
            Log.e("adapter"," i m in grid adapterr i m in if "+name);

            holder.itemImage.setImageResource(R.drawable.allergy_shelfishfree);

        }
        if(name.equalsIgnoreCase("Wheat Free"))
        {
            Log.e("adapter"," i m in grid adapterr i m in if "+name);

            holder.itemImage.setImageResource(R.drawable.allergy_wheatfree);

        }
        if(name.equalsIgnoreCase("Soy Free"))
        {
            Log.e("adapter"," i m in grid adapterr i m in if "+name);

            holder.itemImage.setImageResource(R.drawable.allergy_soyfree);

        }


    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final LinearLayout rowLL;
        private final TextView itemName;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemImage= itemView.findViewById(R.id.settings_imageitem);
            itemName = itemView.findViewById(R.id.free_text);
            rowLL = itemView.findViewById(R.id.row_LL);
            //itemView.setOnClickListener(this);
        }

    }

}
