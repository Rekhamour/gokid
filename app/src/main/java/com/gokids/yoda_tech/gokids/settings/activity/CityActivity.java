package com.gokids.yoda_tech.gokids.settings.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.settings.adapter.CityAdapter;
import com.gokids.yoda_tech.gokids.settings.model.CityBean;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class CityActivity extends AppCompatActivity {

    private RecyclerView cityView;
    private ArrayList<CityBean> list= new ArrayList<>();
    private CityAdapter adapter;
    private LinearLayoutManager layoutmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        iniiUi();
    }

    private void iniiUi() {
        cityView= findViewById(R.id.city_rv);
        adapter= new CityAdapter(CityActivity.this,list);
        layoutmanager= new LinearLayoutManager(this);
        MySharedPrefrence.getPrefrence(CityActivity.this).edit().putString("current_city","CITY1").commit();
        cityView.setLayoutManager(layoutmanager);
        cityView.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getCity();

    }

    private void getCity() {
        Ion.with(CityActivity.this)
                .load(Urls.BASE_URL+"api/viewAllCities")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                                  if(e==null)
                                  {
                                      Log.e("","result"+ result.toString());
                                         String status= result.get("status").getAsString();
                                         String message= result.get("message").getAsString();
                                         if(status.equalsIgnoreCase("200"))
                                         {
                                             JsonArray resultArray=result.getAsJsonArray("result");
                                             if(resultArray.size()>=0)
                                             {
                                                 for(int i=0;i<resultArray.size();i++)
                                                 {
                                                    JsonObject obj= resultArray.get(i).getAsJsonObject();
                                                            String Counter=obj.get("Counter").getAsString();
                                                            String CityID=obj.get("CityID").getAsString();
                                                            String City=obj.get("City").getAsString();
                                                            CityBean bean= new CityBean();
                                                            bean.setCity(City);
                                                            bean.setCityID(CityID);
                                                            bean.setCounter(Counter);
                                                            list.add(bean);

                                                 }
                                                 adapter.notifyDataSetChanged();
                                             }
                                         }
                                  }
                                  else {
                                      e.printStackTrace();
                                  }

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
