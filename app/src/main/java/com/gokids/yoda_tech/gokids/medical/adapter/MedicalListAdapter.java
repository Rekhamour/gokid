package com.gokids.yoda_tech.gokids.medical.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.activity.FoodDetailActivity;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by sab99r
 */
public class MedicalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    private static MedicalAdapter.ItemClickCallback itemClickCallback;

    static Context context;
    List<MainBean> list;
   public OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private static DecimalFormat df = new DecimalFormat(".##");

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public MedicalListAdapter(Context context,List<MainBean> list) {
        MedicalListAdapter.context = context;
        this.list = list;
        df.setRoundingMode(RoundingMode.UP);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MyViewHolder(inflater.inflate(R.layout.medical_row,parent,false));
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
            ((MyViewHolder)holder).bindData(list.get(position),position);
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

    static class MyViewHolder extends RecyclerView.ViewHolder{
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
        void bindData(final MainBean m, final int position){

            name.setText(m.getName());
            address.setText(m.getAddress());
//        double dist = m.getDistance()/1000
            dist.setText(m.getDistance() + "\nKms Away");
            //holder.kids.setText(m.getKidsfinityScore() + "");
            if(m.getImages().size()>0) {
                if (!m.getImages().get(0).isEmpty()) {
                    Ion.with(img)
                            .placeholder(R.drawable.med_error_image)
                            .error(R.drawable.med_error_image)
                            .load(m.getImages().get(0));
                }
            }
            single_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickCallback.onItemClick(position);
                }
            });
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
    public interface ItemClickCallback {
        void  onItemClick(int p);
    }

    public void setItemCallback(final MedicalAdapter.ItemClickCallback itemCallback) {
        itemClickCallback = itemCallback;
        if(itemClickCallback != null);
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
