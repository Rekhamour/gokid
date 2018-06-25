package com.gokids.yoda_tech.gokids.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;

public class DetailActivity extends AppCompatActivity {

    private int mpos;
    private TextView msgView;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
    }

    private void initUi() {
       imgView= findViewById(R.id.icon_page);
       msgView= findViewById(R.id.message);
      mpos=  getIntent().getIntExtra("pos",0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
      if(mpos==4)
      {
          imgView.setImageResource(R.drawable.ic_sos);

      }
        if(mpos==5)
        {
            imgView.setImageResource(R.drawable.ic_buy);

        }
        if(mpos==6)
        {
            imgView.setImageResource(R.drawable.ic_learn);

        }
        if(mpos==7)
        {
            imgView.setImageResource(R.drawable.sample);

        }
        if(mpos==8)
        {
            imgView.setImageResource(R.drawable.ic_advice);

        }
        if(mpos==3)
        {
            imgView.setImageResource(R.drawable.ic_medical);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }
}
