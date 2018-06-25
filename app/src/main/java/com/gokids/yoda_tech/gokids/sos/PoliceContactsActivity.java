package com.gokids.yoda_tech.gokids.sos;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class PoliceContactsActivity extends AppCompatActivity {

    private RecyclerView rv_Police;
    private LinearLayoutManager lm;
    ArrayList<PoliceContactsBean> list= new ArrayList<>();
    private PoliceContactsAdapter adapter;
    private Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_contacts);
        initView();
    }

    private void initView() {
        rv_Police= findViewById(R.id.rv_Police);
        lm= new LinearLayoutManager(this);
        rv_Police.setLayoutManager(lm);
        adapter= new PoliceContactsAdapter(this,list);
        rv_Police.setAdapter(adapter);
        callContactApi();

    }

    private void callContactApi() {
        loc= Utils.getLatLong(this);
        String url = Urls.BASE_URL+"api/viewAllPolice/latitude/"+loc.getLatitude()+"/longitude/"+loc.getLongitude()+"/city/"+ MySharedPrefrence.getPrefrence(this).getString("current_city","");
        Ion.with(PoliceContactsActivity.this).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if(e==null)
                {
                    Log.e("rsult","result"+result.toString());
                    String status= result.get("status").getAsString();
                    String message= result.get("message").getAsString();
                    if(result.has("result"))
                    {
                             JsonArray jsonArray= result.get("result").getAsJsonArray();
                             for(int i= 0;i<jsonArray.size();i++)
                             {
                                 JsonObject obj= jsonArray.get(i).getAsJsonObject();
                                 String name= obj.get("Police").getAsString();
                                 String address= obj.get("Address").getAsString();
                                 String phone= obj.get("Phone").getAsString();
                                 PoliceContactsBean bean=  new PoliceContactsBean();
                                 bean.setPoliceAddress(address);
                                 bean.setPoliceContact(phone);
                                 bean.setPoliceName(name);
                                 list.add(bean);

                             }
                             adapter.notifyDataSetChanged();
                    }

                }
                else
                {

                }

            }
        });

    }
}
