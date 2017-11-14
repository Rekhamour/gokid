package com.gokids.yoda_tech.gokidsapp.learn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.learn.Util.Reviews;

import java.util.ArrayList;


/**
 * Created by manoj2prabhakar on 22/04/17.
 */

public class ReviewAdapter extends BaseAdapter {
    private ArrayList<Reviews> reviewses;
    private Context mContext;

    public ReviewAdapter(Context c, ArrayList<Reviews> r){
        mContext =c;
        reviewses = r;
    }

    @Override
    public int getCount() {
        if(reviewses!=null && reviewses.size()!=0){
            return reviewses.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return reviewses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_review_layout, null);

        TextView userName = (TextView) view.findViewById(R.id.userName);
        TextView review = (TextView) view.findViewById(R.id.review);

        userName.setText(reviewses.get(i).getReviewer());
        review.setText(reviewses.get(i).getReview());


        return view;
    }

    public void swapContent(ArrayList<Reviews> reviewses1){
        reviewses = reviewses1;
        notifyDataSetChanged();
    }
}
