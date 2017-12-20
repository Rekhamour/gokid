package com.gokids.yoda_tech.gokids.settings.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gokids.yoda_tech.gokids.settings.model.Allergy;
import com.gokids.yoda_tech.gokids.utils.GridItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoj2prabhakar on 01/06/17.
 */


public class AllergyAdapter extends BaseAdapter {

    private Context mContext;
    public ArrayList<Allergy> allergy;
    public List selectedPositions;



    public AllergyAdapter(Context c, ArrayList<Allergy> allergies) {
        this.mContext = c;
        this.allergy = allergies;
        selectedPositions= new ArrayList();
    }

    @Override
    public int getCount() {
        return allergy.size();
    }

    @Override
    public Object getItem(int i) {
        return allergy.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GridItemView customView = (convertView == null) ? new GridItemView(mContext) : (GridItemView) convertView;
        customView.display(allergy.get(position).getDispText(),allergy.get(position).getDrawableID(),allergy.get(position).isSelected());
        return customView;
    }
}




