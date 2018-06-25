package com.gokids.yoda_tech.gokids.sos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.RecyclerItemClickListener;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity  {

    private ListView contsListview;
    private LinearLayoutManager lm;
    //ArrayList<ContactBean> list=  new ArrayList<>();
    //String[] list=  new String[5];
    private final int REQUEST_CODE=99;
    private CustomAdapter adapter;
    public static int pos;
    ContactBean[] list=new ContactBean[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initView();

    }
    private void initView() {
        contsListview= findViewById(R.id.contact_list);
        //lm= new LinearLayoutManager(this);
        setList();
      // getContacts();

        /*contsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //list.set();
                pos= i;

                ImageView add=view.findViewById(R.id.address_contact);
                phonename=(TextView)view.findViewById(R.id.name);



            }
        });
*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        //getContacts();

    }

    public  void getContacts() {
        String getContactsUrl= Urls.BASE_URL+"api/viewAllSOSContact/email/"+ MySharedPrefrence.getPrefrence(ContactsActivity.this).getString("emailId","");
        Ion.with(ContactsActivity.this).load(getContactsUrl).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
                                list[i]=bean;
                            }
                            adapter= new CustomAdapter(list,ContactsActivity.this);
                            contsListview.setAdapter(adapter);
                           /* for(int i= jsonArray.size();i<5;i++)
                            {
                                ContactBean bean = new ContactBean();
                                bean.setName("Add details from address book");
                                bean.setContact(" ");
                                list[i]=bean;

                            }*/
                            Log.e("original list"," list"+list.toString());



                        }
                        else
                        {
                            setList();

                        }
                    }
                }
            }
        });
    }


    private void setList() {
        for(int i= 0;i<5;i++) {
            ContactBean bean = new ContactBean();
            bean.setName("Add details from address book");
            bean.setContact("");
            list[i]=bean;
        }
        adapter= new CustomAdapter(list,ContactsActivity.this);
        contsListview.setAdapter(adapter);
        getContacts();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        String name = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                name = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                Toast.makeText(ContactsActivity.this, "Number="+num, Toast.LENGTH_LONG).show();
                                // adapter.notifyDataSetChanged();
                                //adapter.notifyItemChanged(pos);

                            }
                            int position=MySharedPrefrence.getPrefrence(ContactsActivity.this).getInt("pos",1);
                            Log.e(" pos iin resuly","res"+position);
                            ContactBean bean= list[position];
                            String number= num.replaceAll("\\s","").replaceAll("[\\-\\+\\.\\^:,]","").replaceAll("[^\\w\\s]","");
                            String contactName=name.replaceAll("\\s","").replaceAll("[\\-\\+\\.\\^:,]","").replaceAll("[^\\w\\s]","");
                            bean.setContact(number);
                            bean.setName(contactName);
                            //list[position]=bean;
                            adddeleteContact(position,contactName,number);
                            getContacts();
                            //adapter= new CustomAdapter(list,ContactsActivity.this);
                           // contsListview.setAdapter(adapter);
                           // adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sos_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.send_msg)
        {
            Intent intent=  new Intent(ContactsActivity.this,SendMessageActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void adddeleteContact(int position, String name, String contact) {
        String urltoadd= Urls.BASE_URL+"api/addDeleteSOSContact/email/"+ MySharedPrefrence.getPrefrence(ContactsActivity.this).getString("emailId","")+"/contactName/"+name.trim()+"/phone/"+contact.trim()+"/mode/1";
        Ion.with(ContactsActivity.this).load(urltoadd).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if(e==null)
                {
                    Log.e("rsult", "result" + result.toString());
                    String status = result.get("status").getAsString();
                    String message = result.get("message").getAsString();

                }

            }
        });
    }

    public void redirectSOS(View view) {
        finish();
    }
}
