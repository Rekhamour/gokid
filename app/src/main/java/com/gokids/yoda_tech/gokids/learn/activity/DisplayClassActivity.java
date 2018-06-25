package com.gokids.yoda_tech.gokids.learn.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.learn.Util.ProviderDetails;
import com.gokids.yoda_tech.gokids.learn.adapter.ProviderClassAdapter;
import com.gokids.yoda_tech.gokids.learn.async.GetClass;

import java.util.ArrayList;

public class DisplayClassActivity extends AppCompatActivity {

    public ArrayList<ProviderDetails> details = null;
    ProviderClassAdapter adapter;
    String[] imageURL;
    GetClass getClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.listClass));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ProviderClassAdapter(DisplayClassActivity.this,details);

        final ListView listView = findViewById(R.id.listViewClasses);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(DisplayClassActivity.this, ProviderDetailsActivity.class);
                intent.putExtra("details",details.get(i));
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);

        if(savedInstanceState!=null){
            details = (ArrayList<ProviderDetails>) savedInstanceState.getSerializable("details");
            adapter.swapItems(details);
        }
        else {
            getClass = new GetClass(DisplayClassActivity.this, new GetClass.AsyncComplete() {
                @Override
                public void processCompleted(Object obj) {
                    details = (ArrayList<ProviderDetails>) obj;

                    Log.v("url",details.get(0).getImages().get(0).getImageURL());

                    adapter.swapItems(details);


                }
            });
            getClass.execute("");

        }




    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("details",details);
    }
}
