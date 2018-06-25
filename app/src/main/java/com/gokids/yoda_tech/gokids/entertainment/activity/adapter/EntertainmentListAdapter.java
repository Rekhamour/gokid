package com.gokids.yoda_tech.gokids.entertainment.activity.adapter;

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
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sab99r
 */
public class EntertainmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<MainBean> list;
   public OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private static DecimalFormat df = new DecimalFormat(".##");
    private EntertainlistAdapter.ItemClickCallback itemClickCallback;
    Context ctx;
   String flag;


    public EntertainmentListAdapter(Context ctx, ArrayList<MainBean> list, String flagfirst){
        this.list = list;
        this.ctx= ctx;
        this.flag=flagfirst;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        if(viewType==TYPE_MOVIE){
            return new MyViewHolder(inflater.inflate(R.layout.entertainment_list_row,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((MyViewHolder)holder).bindData(list.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getType().equals("data")){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /* VIEW HOLDERS */

     class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView event_date;
        ImageView img;
        TextView name,dist,address,kids,kidfinity_Score;
        RelativeLayout single_row;
        public MyViewHolder(View itemView) {
            super(itemView);
            single_row = itemView.findViewById(R.id.entertainment_single_row_container);
            img = itemView.findViewById(R.id.entertainment_image);
            name = itemView.findViewById(R.id.entertainment_name);
            event_date = itemView.findViewById(R.id.event_date);
            dist = itemView.findViewById(R.id.entertainment_distance);
            address = itemView.findViewById(R.id.entertainment_address);
            kids = itemView.findViewById(R.id.entertainment_kidsfinity);
            kidfinity_Score = itemView.findViewById(R.id.entertainment_list_kidfinity_Score);
        }
        void bindData(final MainBean m){

            name.setText(m.getEntertainmentTitle());
            address.setText(m.getAddress());
//        double dist = m.getDistance()/1000
            dist.setText(m.getDistance() + "\nKm ");
            //kids.setText(m.getKidsfinityScore() + "");
            if(m.getImages().size()>0) {
                Ion.with(img)
                        .error(R.drawable.watermark_for_all)
                        .load(m.getImages().get(0));
            }
            single_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, EntertainmentDetailActivity.class).putExtra("medical_data", m);
                    ctx.startActivity(intent);
                }
            });
            Log.e("flag check","flag value"+ flag);
            if(!flag.isEmpty() )
            {
                event_date.setVisibility(View.VISIBLE);
                event_date.setText(m.getEventDate().toString().replaceAll("\"",""));
            }

            kidfinity_Score.setText(String.valueOf((m.getKidsfinityScore())));
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
