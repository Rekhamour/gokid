package com.gokids.yoda_tech.gokids.settings.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gokids.yoda_tech.gokids.settings.model.Cuisine;
import com.gokids.yoda_tech.gokids.utils.GridItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoj2prabhakar on 05/06/17.
 */

public class CuisineAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Cuisine> cuisines;
    public List selectedPositions;


    public CuisineAdapter(Context c, ArrayList<Cuisine> cuisines) {
        mContext = c;
        this.cuisines = cuisines;
        selectedPositions= new ArrayList();
    }
    @Override
    public int getCount() {
        return cuisines.size();
    }

    @Override
    public Object getItem(int i) {
        return cuisines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(mContext) : (GridItemView) convertView;
        customView.display(cuisines.get(position).getDispText(),cuisines.get(position).getDrawableID(), cuisines.get(position).isSelected());

        return customView;
    }
}
