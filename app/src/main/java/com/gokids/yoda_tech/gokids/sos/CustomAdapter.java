package com.gokids.yoda_tech.gokids.sos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ContactBean> {

    private static final int REQUEST_CODE = 99;
    public static ContactBean[] dataSet=new ContactBean[5];
    static Context mContext;
 
    // View lookup cache
    private static class ViewHolder {

    }
 
    public CustomAdapter(ContactBean[] data, Context context) {
        super(context, R.layout.contact_view, data);
        dataSet = data;
        mContext=context;
  Log.e("Adapter","list");
  for (int i=0;i<dataSet.length;i++)
  {
      Log.e("object","ame"+dataSet[i].getName());
      Log.e("object","numbr"+dataSet[i].getContact());
  }
    }
 

 
    private int lastPosition = -1;
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
       /* ContactBean dataModel = new ContactBean();

        if(dataSet.length>0)
        {
             dataModel = dataSet[position];

        }
        else {
             dataModel = new ContactBean();
            dataModel.setName("Add details from Address book");
            dataModel.setContact(" ");
            dataModel.setEmail(" ");
            dataModel.setCurrentLocation(" ");
        }*/

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
 
        final View result;
        final ContactBean dataModel=dataSet[position];
 
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.contact_view, parent, false);

            LinearLayout contact_row_view = convertView.findViewById(R.id.contact_row_view);
            final TextView contact = convertView.findViewById(R.id.contact);
            final TextView name = convertView.findViewById(R.id.name);
            ImageView add = convertView.findViewById(R.id.address_contact);
            final ContactBean finalDataModel = dataModel;
            name.setText(dataModel.getName());
            contact.setText(dataModel.getContact());
            contact_row_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dataModel.getContact().toString().trim().isEmpty())
                    {
                        Log.e(" pos iin clicking","res"+position);
                        MySharedPrefrence.getPrefrence(mContext).edit().putInt("pos",position).commit();
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        ((Activity)  mContext).startActivityForResult(intent, REQUEST_CODE);
                    }
                    else
                    getDeleteContactAlert(mContext,position,finalDataModel.getName(), finalDataModel.getContact(),name,contact);
                    //deleteContact(position, finalDataModel.getName(), finalDataModel.getContact());

                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(" pos iin clicking","res"+position);
                    MySharedPrefrence.getPrefrence(mContext).edit().putInt("pos",position).commit();
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    ((Activity)  mContext).startActivityForResult(intent, REQUEST_CODE);
                }
            });
        }
        return convertView;
    }



    public  void deleteContact(final int position, String name, String contact) {
        String urltodeletete= Urls.BASE_URL+"api/addDeleteSOSContact/email/"+ MySharedPrefrence.getPrefrence(mContext).getString("emailId","")+"/contactName/"+name+"/phone/"+contact+"/mode/0";
        Ion.with(mContext).load(urltodeletete).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if(e==null)
                {
                    Log.e("rsult", "result" + result.toString());
                    String status = result.get("status").getAsString();
                    String message = result.get("message").getAsString();
                    ContactBean bean=dataSet[position];
                    bean.setName("Add details from Address Book");
                    bean.setCurrentLocation("");
                    bean.setEmail("");
                    bean.setContact("");
                    dataSet[position]=bean;

                }

                notifyDataSetChanged();


            }
        });

    }

    @Override
    public int getCount() {
        return 5;
    }
    public  void getContacts() {
        String getContactsUrl= Urls.BASE_URL+"api/viewAllSOSContact/email/"+ MySharedPrefrence.getPrefrence(mContext).getString("emailId","");
        Ion.with(mContext).load(getContactsUrl).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
                                dataSet[i]=bean;
                            }
                            //notifyDataSetChanged();
                            //adapter= new CustomAdapter(list,ContactsActivity.this);
                           // contsListview.setAdapter(adapter);
                           /* for(int i= jsonArray.size();i<5;i++)
                            {
                                ContactBean bean = new ContactBean();
                                bean.setName("Add details from address book");
                                bean.setContact(" ");
                                list[i]=bean;

                            }*/
                            Log.e("original list"," list"+dataSet.toString());



                        }
                        else
                        {
                            //setList();

                        }
                    }
                }
            }
        });
    }
    public  void getDeleteContactAlert(Context contextm, final int position, final String name, final String contact, final TextView nametextView, final TextView contactview) {
        AlertDialog.Builder alert = new AlertDialog.Builder(contextm);
        LayoutInflater inflater = LayoutInflater.from(contextm);
        View alertLayout = inflater.inflate(R.layout.delete_contact_layout, null);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        dialog.show();

        final Button deleteclick = alertLayout.findViewById(R.id.deletecontact);
        final Button cancelclcik = alertLayout.findViewById(R.id.cancelclick);
        deleteclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nametextView.setText("Add details from address book");
                contactview.setText(" ");
                //Toast.makeText(context, "login clicked", Toast.LENGTH_SHORT).show();
                deleteContact(position,name,contact);
               // CustomAdapter.getContacts();
                dialog.dismiss();


            }
        });
        cancelclcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });




    }


}