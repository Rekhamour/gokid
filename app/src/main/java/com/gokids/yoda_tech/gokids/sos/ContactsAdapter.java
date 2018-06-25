package com.gokids.yoda_tech.gokids.sos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.activity.FoodDetailActivity;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.entertainment.activity.EntertainmentDetailActivity;
import com.gokids.yoda_tech.gokids.medical.activity.MedicalDetail;
import com.gokids.yoda_tech.gokids.shop.activity.ShopDetailActivity;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    ArrayList<ContactBean> list;
    Context context;
    private ContactBean obj;
    public ItemClickCallback itemClickCallback;

    public ContactsAdapter(Context context, ArrayList<ContactBean> list) {
        this.list = list;
        this.context= context;
    }
    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemCallback(final ItemClickCallback itemCallback) {
        this.itemClickCallback = itemCallback;
        if(itemClickCallback != null);
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.e("list ","list size"+list.toString());
       if(!list.isEmpty()) {
            obj = list.get(position);
           Log.e("list ","list elements"+ obj.getContact()+ " "+ obj.getName());


           holder.name.setText(obj.getName());
            holder.contact.setText(obj.getContact());

           holder.contact_row_view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   itemClickCallback.onItemClick(position);
               }
           });

       }
//        Log.e("adapter"," i m in adapter"+list.size());


       /* holder.contact_row_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
               context.startActivity(intent);
                ((Activity)context).startActivityForResult(intent,);

                   // list[position]=name;


            }
        });

*/
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout contact_row_view;
        private final TextView contact;
        private final TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            contact_row_view = itemView.findViewById(R.id.contact_row_view);
            contact = itemView.findViewById(R.id.contact);
            name = itemView.findViewById(R.id.name);

        }
    }


}
