package com.gokids.yoda_tech.gokidsapp.learn.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;


public class LearnActivity extends AppCompatActivity {

    Integer tabSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain1);
        //toolbar.setTitle(getString(R.string.learn));
        getSupportActionBar().setTitle("Learn");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);

        TabLayout.Tab beginner = tabLayout.newTab().setText(getResources().getString(R.string.beginner));
        TabLayout.Tab intermediate = tabLayout.newTab().setText(getResources().getString(R.string.inter));
        TabLayout.Tab advanced = tabLayout.newTab().setText(getResources().getString(R.string.advance));

        tabLayout.addTab(beginner);
        tabLayout.addTab(intermediate);
        tabLayout.addTab(advanced);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelected = tab.getPosition();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("level",tab.getPosition());
                editor.apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.isNetworkConnected(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(),DialogActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Make sure you are connected to the internet",Toast.LENGTH_LONG).show();
                }

            }
        });
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
