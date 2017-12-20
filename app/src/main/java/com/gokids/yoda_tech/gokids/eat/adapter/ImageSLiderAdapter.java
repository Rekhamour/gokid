package com.gokids.yoda_tech.gokids.eat.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageSLiderAdapter extends PagerAdapter {

    private final String TAG = getClass().getName();
    private ArrayList<String> images=new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    //Integer[] images= new Integer[]{R.drawable.btn_misc,R.drawable.btn_chat_3x,R.drawable.btn_misc,R.drawable.btn_badge_red_3x,R.drawable.btn_badge_3x};

    public ImageSLiderAdapter(Context context, ArrayList<String> images) {
        Log.e(TAG," i m in adapter");
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.food_detail_image);
       Picasso.with(context).load(images.get(position)).into(myImage);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openfullimage(context,position,images);
            }
        });
        //myImage.setImageResource(images[position]);
        view.addView(myImageLayout);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}