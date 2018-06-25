package com.gokids.yoda_tech.gokids.medical.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.model.Review;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    ArrayList<Review> reviews;

    public ReviewAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_review_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(reviews.get(position).getUseername());
        holder.review.setText(reviews.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,review;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.reviewer);
            review = itemView.findViewById(R.id.review_tv);
        }
    }
}
