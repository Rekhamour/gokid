package com.gokids.yoda_tech.gokidsapp.bookmark.adapter;

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

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.eat.activity.FoodDetailActivity;
import com.gokids.yoda_tech.gokidsapp.eat.model.MainBean;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.EntertainmentDetailActivity;
import com.gokids.yoda_tech.gokidsapp.medical.activity.MedicalDetail;
import com.gokids.yoda_tech.gokidsapp.shop.activity.ShopDetailActivity;
import com.gokids.yoda_tech.gokidsapp.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokidsapp.utils.Urls;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.MyViewHolder> {
    ArrayList<MainBean> list;
    Context context;

    public BookmarksAdapter(Context context, ArrayList<MainBean> list) {
        this.list = list;
        this.context= context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_bookmark_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MainBean obj= list.get(position);
        Log.e("adapter"," i m in adapter"+list.size());
        if(obj.getClassname().equalsIgnoreCase("Food"));
        {
            holder.name.setText(list.get(position).getRestaurantName());
            holder.distance.setText(list.get(position).getPrice());
            holder.address.setText(list.get(position).getAddress());
            Picasso.with(context).load(list.get(position).getImage()).into(holder.bookmark_image);
            holder.bookmark_flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.bookmark_flag.setBackgroundResource(R.drawable.btn_badge_3x);
                    removebookmark(obj.getRestaurantID(),position);

                }
            });


        }
        if(obj.getClassname().equalsIgnoreCase("Entertainment"))
        {

            holder.name.setText(list.get(position).getEntertainmentTitle());
            //holder.distance.setText(list.get(position).getDistance());
            holder.address.setText(list.get(position).getAddress());
            Picasso.with(context).load(list.get(position).getImage()).into(holder.bookmark_image);
            holder.bookmark_flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.bookmark_flag.setBackgroundResource(R.drawable.btn_badge_3x);
                    removebookmark(obj.getEntertainmentID(),position);


                }
            });

        }
        else if(obj.getClassname().equalsIgnoreCase("Shopping"))
        {
            holder.name.setText(list.get(position).getShopName());
           // holder.distance.setText(list.get(position).getDistance());
            holder.address.setText(list.get(position).getAddress());
            Picasso.with(context).load(list.get(position).getImage()).into(holder.bookmark_image);
            holder.bookmark_flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.bookmark_flag.setBackgroundResource(R.drawable.btn_badge_3x);
                    removebookmark(obj.getMedicalId(),position);


                }
            });
        }
        holder.single_row_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(position,obj.getClassname());
            }
        });

       // holder.name.setText(list.get(position).getUseername());
        //holder.review.setText(reviews.get(position).getReview());
    }

    private void openActivity(int position, String classname) {
        if(classname.equalsIgnoreCase("Food"))
        {
            Intent intent =new Intent(context, FoodDetailActivity.class);
                    intent.putExtra("medical_data",list.get(position));
            context.startActivity(intent);
        }
        else if(classname.equalsIgnoreCase("Entertainment"))
        {

            Intent intent =new Intent(context, EntertainmentDetailActivity.class);
            intent.putExtra("medical_data",list.get(position));
            context.startActivity(intent);
        }
        else if(classname.equalsIgnoreCase("Medical"))
        {


            Intent intent =new Intent(context, MedicalDetail.class);
            intent.putExtra("medical_data",list.get(position));
            context.startActivity(intent);
        }
        else if(classname.equalsIgnoreCase("Shopping"))
        {

            Intent intent =new Intent(context, ShopDetailActivity.class);
            intent.putExtra("medical_data",list.get(position));
            context.startActivity(intent);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout single_row_bookmark;
        private final ImageView bookmark_flag;
        ImageView bookmark_image;
        TextView name,address,distance;
        public MyViewHolder(View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.bookmark_address);
            single_row_bookmark = (RelativeLayout) itemView.findViewById(R.id.single_row_bookmark);
            distance = (TextView) itemView.findViewById(R.id.bookmark_distance);
            bookmark_image = (ImageView) itemView.findViewById(R.id.bookmark_image);
            name = (TextView) itemView.findViewById(R.id.bookmark_name);
            bookmark_flag = (ImageView) itemView.findViewById(R.id.bookmark_flag);
        }
    }
    public void removebookmark(String Id, final int position)
    {
        String url= Urls.BASE_URL+"api/setBookMark/email/"+ MySharedPrefrence.getPrefrence(context).getString("emailId","")+"/class/CLS1/categoryItem/"+ Id+"/bookmark/-";
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null)
                        {
                            String status= result.get("status").toString();
                            if(status.equalsIgnoreCase("200"))
                            {


                            }
                            list.remove(position);
                            notifyDataSetChanged();
                        }


                    }
                });

    }
}
