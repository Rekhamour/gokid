package com.gokids.yoda_tech.gokids.sos;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.gokids.yoda_tech.gokids.R;

public class VideoSOSActivity extends AppCompatActivity {
    private String tabTitles[] = new String[] { "Infant", "Child"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_sos);


        initView();

    }

    private void initView() {
        LinearLayout LL = findViewById(R.id.tab_layout_SOS);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new InfantFragment()).commit();


    }

    public void showinfants(View view) {
     getSupportFragmentManager().beginTransaction().replace(R.id.container,new InfantFragment()).commit();
    }

    public void showchild(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChildFragment()).commit();

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
