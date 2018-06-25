package com.gokids.yoda_tech.gokids.ecommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.ecommerce.ChackoutActivity;
import com.gokids.yoda_tech.gokids.ecommerce.model.ShopifyProductBean;
import com.koushikdutta.ion.Ion;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.gokids.yoda_tech.gokids.ecommerce.ChackoutActivity.addedItemList;

/**
 * Created by sab99r
 */
public class EcommercEditCartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
  static   List<ShopifyProductBean> list;
   public OnLoadMoreListener loadMoreListener;
   public static Button editbutton;
    boolean isLoading = false, isMoreDataAvailable = true;
    private static DecimalFormat df = new DecimalFormat(".##");

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public EcommercEditCartListAdapter(Context context, ArrayList<ShopifyProductBean> list) {
        EcommercEditCartListAdapter.context = context;
        EcommercEditCartListAdapter.list = list;
        df.setRoundingMode(RoundingMode.UP);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MyViewHolder(inflater.inflate(R.layout.checkout_single_row,parent,false));
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
        private final TextView increaseQnt;
        private final TextView decreaseQnt;
        ImageView img;
        TextView name,price,QntTv;
        RelativeLayout single_row;
        LinearLayout LLDelete;
        LinearLayout delete_LL;
        public MyViewHolder(View itemView) {
            super(itemView);
            single_row = itemView.findViewById(R.id.cart_single_row_container);
            LLDelete = itemView.findViewById(R.id.ll_delete);
            delete_LL = itemView.findViewById(R.id.delete_LL);
            img = itemView.findViewById(R.id.cart_item_image);
            name = itemView.findViewById(R.id.cart_product_name);
            price = itemView.findViewById(R.id.cart_product_list_price);
            increaseQnt = itemView.findViewById(R.id.increase_qnt);
            decreaseQnt = itemView.findViewById(R.id.decrease_qnt);
            QntTv = itemView.findViewById(R.id.cart_product_quantity);

        }
        void bindData(final ShopifyProductBean m){
            name.setText(m.getTitle());

            decreaseQnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int a= Integer.parseInt(QntTv.getText().toString())-1;
                    QntTv.setText(String.valueOf(a));
                }
            });
            QntTv.setText(String.valueOf(m.getProductQuantity()));
            increaseQnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int a= Integer.parseInt(QntTv.getText().toString())+1;
                     //a=a++;
                     Log.e("value","value"+a);
                    QntTv.setText(String.valueOf(a));
                }
            });

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
            LLDelete.setVisibility(View.VISIBLE);
            LLDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete_LL.setVisibility(View.VISIBLE);
                    LLDelete.setVisibility(View.GONE);
                }
            });
            delete_LL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(m);
                    addedItemList.remove(m);
                    delete_LL.setVisibility(View.GONE);
                    Intent intent= new Intent(context, ChackoutActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
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
