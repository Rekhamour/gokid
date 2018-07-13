package com.gokids.yoda_tech.gokids.sos;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SosActivity extends AppCompatActivity {

    private ImageView imgSos;
    private ArrayList<ContactBean> list= new ArrayList<>();
    private ArrayList<String> contacts= new ArrayList<>();
    private ArrayList<SosLocationsBean> locationslist= new ArrayList<>();
    private Location loc;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    private String addressurl;
    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        initView();
    }

    private void initView() {
        imgSos= findViewById(R.id.btn_sos);
                   loc= Utils.getLatLong(SosActivity.this);
                  // getSupportActionBar().setHomeButtonEnabled(true);
                  // getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
          // loc= Utils.getLatLong(SosActivity.this);
           address=getCurrentaddress(Utils.getLatLong(SosActivity.this));
            /*address =Utils.getLocationCity(loc);
            city = address.getLocality();
            state = address.getAdminArea();
            country = address.getCountryName();
            postalCode = address.getPostalCode();
            knownName = address.getFeatureName();
            addressurl=address.getUrl();*/
        }
        catch(NullPointerException e){
            e.printStackTrace();

        }


        imgSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContacts();
                //sendSenderLocation();


            }
        });

    }

    private String getCurrentaddress(Location loc) {
        final String[] addressgenerated = {""};
        String lati= String.valueOf(loc.getLatitude());
        String longi= String.valueOf(loc.getLongitude());
        String urltogetaddress ="http://maps.googleapis.com/maps/api/geocode/json?latlng="+lati+","+longi+"&sensor=true";
        Log.e("url to address","url to address"+urltogetaddress);
        Ion.with(SosActivity.this)
                .load(urltogetaddress)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null)
                        {
                           JsonArray array=  result.getAsJsonArray("results");
                              //   JsonObject obj= array.get(0).getAsJsonObject();
                                String generatedaddress="";// obj.get("formatted_address").getAsString();
                               address =generatedaddress;
                        }
                    }
                });
        return  addressgenerated[0];

    }

    public void callPoliceContacts(View view)
    {
        Intent intent = new Intent(SosActivity.this,PoliceContactsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_contact_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.edit_contacts)
        {
            Intent intent = new Intent(SosActivity.this,ContactsActivity.class);
            startActivity(intent);

        }
        else if(item.getItemId()==R.id.home_sos)
        {
            Intent intent = new Intent(SosActivity.this,GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
        }
        else if(item.getItemId()==android.R.id.home)
        {
            Intent intent = new Intent(SosActivity.this,GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void getContacts() {
        String getContactsUrl= Urls.BASE_URL+"api/viewAllSOSContact/email/"+ MySharedPrefrence.getPrefrence(SosActivity.this).getString("emailId","");
        Ion.with(SosActivity.this).load(getContactsUrl).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    Log.e("rsult", "result" + result.toString());
                    String status = result.get("status").getAsString();
                    String message = result.get("message").getAsString();
                    if (result.has("result")) {

                        JsonArray jsonArray = result.get("result").getAsJsonArray();
                        if(jsonArray.size()>0) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject obj = jsonArray.get(i).getAsJsonObject();
                                String name = obj.get("ContactName").getAsString();
                                String Phone = obj.get("Phone").getAsString();
                                String Email = obj.get("Email").getAsString();
                                String CurrentLocation = obj.get("CurrentLocation").getAsString();
                                ContactBean bean = new ContactBean();
                                if (name.isEmpty()) {
                                    name = "Add details from address book";
                                }
                                bean.setContact(Phone);
                                bean.setName(name);
                                bean.setEmail(Email);
                                bean.setCurrentLocation(CurrentLocation);
                                contacts.add(Phone);
                                list.add(bean);
                            }
                            try {
                                String stringurl="http://maps.google.com/maps?q="+address;
                                URI uri= new URI(stringurl);
                                url = uri.toURL();
                            } catch (URISyntaxException e1) {
                                e1.printStackTrace();
                            } catch (MalformedURLException e1) {
                                e1.printStackTrace();
                            }
                            String helpMessage= null;

                                helpMessage = MySharedPrefrence.getPrefrence(SosActivity.this).getString("SOS_message","")+getResources().getString(R.string.helpmessage)+"\n"
                                  +address+"\n"
                                  +"http://maps.google.com/maps?q="+Uri.encode(address);


                            Log.e("helpmsg","helpmsg"+helpMessage);
                            /*Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+contacts));
                            smsIntent.putExtra("sms_body", helpMessage);
                            startActivity(smsIntent);*/
                            SmsManager sms = SmsManager.getDefault();
                            for(int i= 0;i<contacts.size();i++) {
                                sms.sendTextMessage(contacts.get(i), null, helpMessage, null, null);
                                Toast.makeText(SosActivity.this, "msg sent"+i, Toast.LENGTH_SHORT).show();

                            }
                            Intent intent = new Intent(SosActivity.this,SenderLocationActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            //setList();
                            Utils.getContactsAlert(SosActivity.this);


                        }
                    }
                }
            }
        });
    }


    public void videoRedirect(View view) {
        Intent intent = new Intent(SosActivity.this,SenderLocationActivity.class);
        startActivity(intent);
    }


}

