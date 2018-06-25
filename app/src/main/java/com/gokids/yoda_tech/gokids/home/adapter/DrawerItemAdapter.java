package com.gokids.yoda_tech.gokids.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.home.model.DrawerItems;
import com.gokids.yoda_tech.gokids.home.model.LoginDetails;

/**
 * Created by manoj2prabhakar on 19/05/17.
 */

public class DrawerItemAdapter extends BaseAdapter {

    Context mContext;
    int layoutResourceId;
    LoginDetails loginDetails;

    public DrawerItemAdapter(Context mContext, int layoutResourceId, LoginDetails loginDetails1) {

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        loginDetails = loginDetails1;
    }

    @Override
    public int getCount() {
        return loginDetails.getItemses().size();
    }

    @Override
    public Object getItem(int i) {
        return loginDetails.getItemses().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = listItem.findViewById(R.id.textViewName);

        DrawerItems items = loginDetails.getItemses().get(position);


        imageViewIcon.setImageResource(items.getImage());
        textViewName.setText(items.getNameText());

        return listItem;
    }
}