package com.gokids.yoda_tech.gokidsapp.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.settings.model.Allergy;
import com.gokids.yoda_tech.gokidsapp.utils.GridItemView;

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

   /* @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.settings_image_item,null);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.settings_imageitem);
        TextView textView = (TextView) rootView.findViewById(R.id.free_text);
        textView.setText(allergy.get(i).getDispText());

        imageView.setImageResource(allergy.get(i).getDrawableID());

        return rootView;
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GridItemView customView = (convertView == null) ? new GridItemView(mContext) : (GridItemView) convertView;
        customView.display(allergy.get(position).getDispText(),allergy.get(position).getDrawableID(),allergy.get(position).isSelected());
/*        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allergy.get(position).isSelected())
                {
                    allergy.get(position).setSelected(false);
                    customView.display(allergy.get(position).getDispText(),allergy.get(position).getDrawableID(),allergy.get(position).isSelected());
                }
                else
                {
                    allergy.get(position).setSelected(true);
                    customView.display(allergy.get(position).getDispText(),allergy.get(position).getDrawableID(),allergy.get(position).isSelected());

                }
            }
        });*/


        return customView;
    }
}




