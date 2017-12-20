package com.gokids.yoda_tech.gokids.entertainment.activity.adapter;

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
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.entertainment.activity.EntertainmentDetailActivity;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class EntertainlistAdapter extends RecyclerView.Adapter<EntertainlistAdapter.MyViewHolder> {

    ArrayList<MainBean> list;
    private ItemClickCallback itemClickCallback;
    Context ctx;
    String flag;
    private ItemFilter mFilter = new ItemFilter();


    public EntertainlistAdapter(Context ctx, ArrayList<MainBean> list, String flagfirst){
        this.list = list;
        this.ctx= ctx;
        this.flag=flagfirst;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.entertainment_list_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        MainBean m = list.get(position);
        holder.name.setText(m.getEntertainmentTitle());
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
                Intent intent = new Intent(ctx, EntertainmentDetailActivity.class).putExtra("medical_data", (Serializable) list.get(position));
                ctx.startActivity(intent);
            }
        });
        if(!flag.isEmpty() )
        {
            holder.event_date.setVisibility(View.VISIBLE);
            holder.event_date.setText(m.getEventDate().toString().replaceAll("\"",""));
        }

        holder.kidfinity_Score.setText(String.valueOf((m.getKidsfinityScore())));

    }
    public Filter getFilter() {
        return mFilter;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView event_date;
        ImageView img;
        TextView name,dist,address,kids,kidfinity_Score;
        RelativeLayout single_row;
        public MyViewHolder(View itemView) {
            super(itemView);
            single_row = (RelativeLayout) itemView.findViewById(R.id.entertainment_single_row_container);
            img = (ImageView) itemView.findViewById(R.id.entertainment_image);
            name = (TextView) itemView.findViewById(R.id.entertainment_name);
            event_date = (TextView) itemView.findViewById(R.id.event_date);
            dist = (TextView) itemView.findViewById(R.id.entertainment_distance);
            address = (TextView) itemView.findViewById(R.id.entertainment_address);
            kids = (TextView) itemView.findViewById(R.id.entertainment_kidsfinity);
            kidfinity_Score = (TextView) itemView.findViewById(R.id.entertainment_list_kidfinity_Score);
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
