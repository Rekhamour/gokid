package com.gokids.yoda_tech.gokidsapp.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokidsapp.R;

/**
 * Created by benepik on 13/7/17.
 */
public class GridItemView extends FrameLayout {

    private final ImageView imgView;
    private TextView textView;

    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.settings_image_item, this);
        textView = (TextView) getRootView().findViewById(R.id.free_text);
        imgView = (ImageView) getRootView().findViewById(R.id.settings_imageitem);
    }

    public void display(String text, int drawableID, boolean isSelected) {
        textView.setText(text);
        //Log.e("kitem","grid kitem"+text);
        imgView.setImageResource(drawableID);
        display(isSelected);
    }

    public void display(boolean isSelected) {
        //textView.setBackgroundResource(isSelected ? R.drawable.green_square : R.drawable.gray_square);
        if(isSelected)
        imgView.setBackground(getResources().getDrawable(R.drawable.bg_circle_trans));
        else
        {
            imgView.setBackground(getResources().getDrawable(R.drawable.bg_circle_white));


        }
    }
}