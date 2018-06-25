package com.gokids.yoda_tech.gokids.learn.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gokids.yoda_tech.gokids.R;
import com.squareup.picasso.Picasso;

/**
 * Created by manoj2prabhakar on 21/04/17.
 */

public class SlidingViewPageAdapter extends PagerAdapter {
    private Context mContext;
    private AdapterView.OnItemClickListener mOnPagerItemClick;
    private String mImageURLs[];

    public SlidingViewPageAdapter(Context context,  String[] mImageURLs) {
        mContext = context;
        this.mImageURLs = mImageURLs;
    }


    @Override
    public int getCount() {
        return mImageURLs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.image_item,container,false);
        assert rootView != null;

        ImageView imageView = rootView.findViewById(R.id.img_pager_item);

        Picasso.with(mContext).load(mImageURLs[position]).into(imageView);

        container.addView(rootView);

        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
