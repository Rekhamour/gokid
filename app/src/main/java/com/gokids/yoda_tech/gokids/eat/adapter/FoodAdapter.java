package com.gokids.yoda_tech.gokids.eat.adapter;

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


import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.activity.FoodDetailActivity;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    ArrayList<MainBean> list;
    private ItemClickCallback itemClickCallback;
    Context ctx;
    private ItemFilter mFilter = new ItemFilter();


    public FoodAdapter(Context ctx,ArrayList<MainBean> list){
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        MainBean m = list.get(position);
        holder.name.setText(m.getRestaurantName());
        holder.address.setText(m.getAddress());
//        double dist = m.getDistance()/1000
        holder.dist.setText(m.getDistance() + "\nKm ");
        //holder.kids.setText(m.getKidsfinityScore() + "");
        if(m.getImages().size()>0) {
            Ion.with(holder.img)
                    .error(R.drawable.watermark_for_all)
                    .load(m.getImages().get(0));
        }
        holder.single_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // itemClickCallback.onItemClick(position);
                Intent intent = new Intent(ctx, FoodDetailActivity.class).putExtra("medical_data", (Serializable) list.get(position));
                ctx.startActivity(intent);
            }
        });
        holder.food_list_kidfinity_Score.setText(String.valueOf((m.getKidsfinityScore())));

    }
    public Filter getFilter() {
        return mFilter;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,dist,address,kids,food_list_kidfinity_Score;
        RelativeLayout single_row;
        public MyViewHolder(View itemView) {
            super(itemView);
            single_row = (RelativeLayout) itemView.findViewById(R.id.food_single_row_container);
            img = (ImageView) itemView.findViewById(R.id.food_image);
            name = (TextView) itemView.findViewById(R.id.food_name);
            dist = (TextView) itemView.findViewById(R.id.food_distance);
            address = (TextView) itemView.findViewById(R.id.food_address);
            kids = (TextView) itemView.findViewById(R.id.food_kidsfinity);
            food_list_kidfinity_Score = (TextView) itemView.findViewById(R.id.food_list_kidfinity_Score);
        }
    }
    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            int count = list.size();

            final ArrayList<MainBean> tempFilterList = new ArrayList<MainBean>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
               /* filterableString = list.get(i).getNum_backers();
                if (filterableString.toLowerCase().contains(filterString)) {
                    tempFilterList.add(list.get(i));
                }*/
            }

            results.values = tempFilterList;
            results.count = tempFilterList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list = (ArrayList<MainBean>) results.values;
            notifyDataSetChanged();
        }

    }

}
