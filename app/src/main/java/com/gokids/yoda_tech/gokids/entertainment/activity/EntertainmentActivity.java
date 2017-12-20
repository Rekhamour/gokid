package com.gokids.yoda_tech.gokids.entertainment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.activity.EntertainmentFragment;
import com.gokids.yoda_tech.gokids.utils.Utils;

public class EntertainmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entertainment_menu_layout);
        setNavigation();
        openEntertainmentList();

    }

    private void setNavigation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void openEntertainmentList() {
        EntertainmentFragment fragment = new EntertainmentFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.entertainment_list_parent,fragment).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Utils.NavigatetoHome(EntertainmentActivity.this);
        }
        return true;
    }
}
