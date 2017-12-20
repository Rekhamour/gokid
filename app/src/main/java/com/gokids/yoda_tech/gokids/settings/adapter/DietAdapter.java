package com.gokids.yoda_tech.gokids.settings.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gokids.yoda_tech.gokids.settings.model.Diet;
import com.gokids.yoda_tech.gokids.utils.GridItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoj2prabhakar on 05/06/17.
 */

public class DietAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Diet> diets;
   public List selectedPositions;
    public DietAdapter(Context c, ArrayList<Diet> diets) {

        mContext = c;
        this.diets = diets;
        selectedPositions= new ArrayList();

    }

    @Override
    public int getCount() {
        return diets.size();
    }

    @Override
    public Object getItem(int i) {
        return diets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(mContext) : (GridItemView) convertView;
        customView.display(diets.get(position).getDispText(),diets.get(position).getDrawableID(), diets.get(position).isSelected());

        return customView;
    }
}
