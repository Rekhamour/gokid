package com.gokids.yoda_tech.gokids.ecommerce.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gokids.yoda_tech.gokids.ecommerce.model.ClothingBean;
import com.gokids.yoda_tech.gokids.settings.model.Allergy;
import com.gokids.yoda_tech.gokids.utils.ClothingGridItem;
import com.gokids.yoda_tech.gokids.utils.GridItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoj2prabhakar on 01/06/17.
 */


public class EcommerceClothingAdapter extends BaseAdapter {

    private Context mContext;
    public ArrayList<ClothingBean> list;
    public List selectedPositions;



    public EcommerceClothingAdapter(Context c, ArrayList<ClothingBean> list) {
        this.mContext = c;
        this.list = list;
        selectedPositions= new ArrayList();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ClothingGridItem customView = (convertView == null) ? new ClothingGridItem(mContext) : (ClothingGridItem) convertView;
        customView.display(list.get(position).getDispText(),list.get(position).isSelected());
        return customView;
    }
}




