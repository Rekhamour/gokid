package com.gokids.yoda_tech.gokids.referfriend.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.gokids.yoda_tech.gokids.R;

public class ReferFriend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void shareapp(View view) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out the new Gokids app.Find kids friendly restaurants,shops and lot more just for your kids!It even suggest the most happening events in Singapore for your kids! Try it here..");
        startActivity(Intent.createChooser(sharingIntent, "Share via..."));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
