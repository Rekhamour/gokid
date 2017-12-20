package com.gokids.yoda_tech.gokids.utils;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.adapter.FullImageAdapter;

import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    private ViewPager imgView;
    private ArrayList<String> Allimageslist;
    private FullImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setUpview();
    }

    private void setUpview() {

        imgView= (ViewPager)findViewById(R.id.full_image);
        int positon= getIntent().getIntExtra("position",0);
         Allimageslist = getIntent().getStringArrayListExtra("Allimageslist");
        adapter= new FullImageAdapter(FullImageActivity.this,Allimageslist);
        imgView.setAdapter(adapter);
        imgView.setCurrentItem(positon);

        //Picasso.with(FullImageActivity.this).load(imagepath).into(imgView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
