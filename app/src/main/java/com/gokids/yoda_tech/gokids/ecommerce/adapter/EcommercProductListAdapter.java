package com.gokids.yoda_tech.gokids.ecommerce.adapter;

import android.app.Activity;
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
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.ecommerce.ProductDetailActivity;
import com.gokids.yoda_tech.gokids.ecommerce.model.ShopifyProductBean;
import com.gokids.yoda_tech.gokids.shop.activity.ShopDetailActivity;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sab99r
 */
public class EcommercProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<ShopifyProductBean> list;
   public OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private static DecimalFormat df = new DecimalFormat(".##");

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public EcommercProductListAdapter(Context context, ArrayList<ShopifyProductBean> list) {
        EcommercProductListAdapter.context = context;
        this.list = list;
        df.setRoundingMode(RoundingMode.UP);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MyViewHolder(inflater.inflate(R.layout.product_list_row,parent,false));
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

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name,price;
        RelativeLayout single_row;
        public MyViewHolder(View itemView) {
            super(itemView);
            single_row = itemView.findViewById(R.id.allproducts_single_row_container);
            img = itemView.findViewById(R.id.product_list_image);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_list_price);

        }
        void bindData(final ShopifyProductBean m){
            name.setText(m.getTitle());
            price.setText("$"+m.getPrice());
                Log.e("adapter","price"+price.getText().toString());
//        double dist = m.getDistance()/1000
           // dist.setText(m.getDistance() + "\nKm ");
            //holder.kids.setText(m.getKidsfinityScore() + "");
            if(m.getImgeslist().size()>0) {
                Ion.with(img)
                        .error(R.drawable.watermark_for_all)
                        .load(m.getImgeslist().get(0));
            }
            single_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // itemClickCallback.onItemClick(position);
                    Intent intent = new Intent(context, ProductDetailActivity.class).putExtra("productdetails", m);
                    context.startActivity(intent);
                    //((Activity)context).finish();
                }
            });
           // food_list_kidfinity_Score.setText(String.valueOf((m.getKidsfinityScore())));
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
