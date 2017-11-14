package com.gokids.yoda_tech.gokidsapp.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.settings.model.Diet;
import com.gokids.yoda_tech.gokidsapp.utils.GridItemView;

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

/*    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.settings_image_item,null);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.settings_imageitem);
        TextView textView = (TextView) rootView.findViewById(R.id.free_text);
        textView.setText(diets.get(i).getDispText());

        imageView.setImageResource(diets.get(i).getDrawableID());

        return rootView;
    }*/


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(mContext) : (GridItemView) convertView;
        customView.display(diets.get(position).getDispText(),diets.get(position).getDrawableID(), diets.get(position).isSelected());

        return customView;
    }
}
