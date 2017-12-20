package com.gokids.yoda_tech.gokids.medical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.medical.network.ApiCalls;

import java.util.ArrayList;
import java.util.List;

public class MedicalMainActivty extends AppCompatActivity {

    ApiCalls apiCalls;
    final List<CharSequence> medical_types = new ArrayList<>();
    Button search;
    String medical_select=null,spec_select=null;
    private String tabTitles[] = new String[] { "Doctor", "Clinic", "Hospital", "Pharmacy", "Child Care"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_main);
        //startActivity(new Intent(MainActivity.this,MedicalDetail.class));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[2]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[3]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[4]));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position=tab.getPosition();
                if(position == 2) {
                    medical_select = "CAT15";
                }
                if(position == 3) {
                    medical_select = "CAT14";
                }
                if(position == 4) {
                    medical_select = "CAT16";
                }
                if(position == 5) {
                    medical_select = "CAT17";
                }
                if(position == 0) {
                    medical_select = "-";
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Spinner specialization_spinner = (Spinner) findViewById(R.id.specialization_spinner);
        ArrayAdapter<CharSequence> spec_adapter = ArrayAdapter.createFromResource(this,R.array.spec_array,android.R.layout.simple_spinner_item);
        specialization_spinner.setAdapter(spec_adapter);
        search = (Button) findViewById(R.id.search_btn);
        specialization_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    spec_select = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MedicalMainActivty.this,SearchResultActivity.class).putExtra("spec_select",spec_select).putExtra("medical_slecet",medical_select);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        spec_select = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
