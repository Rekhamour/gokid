package com.gokids.yoda_tech.gokids.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;

/**
 * Created by benepik on 13/7/17.
 */
public class ClothingGridItem extends FrameLayout {

    private final ImageView imgView;
    private TextView textView;

    public ClothingGridItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.clothing_item_layout, this);
        textView = getRootView().findViewById(R.id.clothin_age_text);
        imgView = getRootView().findViewById(R.id.clothing_imageitem);
    }

    public void display(String text, boolean isSelected) {
        textView.setText(text);
        display(isSelected);
    }

    public void display(boolean isSelected) {
        if(isSelected)
            imgView.setBackground(getResources().getDrawable(R.drawable.bg_circle_red_blck_outline));
        else
        {
            imgView.setBackground(getResources().getDrawable(R.drawable.bg_cicle_grey_filled_black_otline));
        }
    }
}