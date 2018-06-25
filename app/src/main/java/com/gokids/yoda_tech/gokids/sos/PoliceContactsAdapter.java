package com.gokids.yoda_tech.gokids.sos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class PoliceContactsAdapter extends RecyclerView.Adapter<PoliceContactsAdapter.MyViewHolder> {
    ArrayList<PoliceContactsBean> list;
    Context context;
    private PoliceContactsBean obj;

    public PoliceContactsAdapter(Context context, ArrayList<PoliceContactsBean> list) {
        this.list = list;
        this.context= context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.police_contact_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.e("list ","list size"+list.size());
       if(!list.isEmpty()) {
            obj = list.get(position);
           Log.e("list ","list elements"+ obj.getPoliceContact()+ " "+ obj.getPoliceName());
            holder.name.setText(obj.getPoliceName());
            holder.contact.setText(obj.getPoliceContact());
            holder.address.setText(obj.getPoliceAddress());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout contact_row_view;
        private final TextView contact;
        private final TextView name;
        private final TextView address;

        public MyViewHolder(View itemView) {
            super(itemView);
            contact_row_view = itemView.findViewById(R.id.parent_row_LL);
            address = itemView.findViewById(R.id.address_contact);
            contact = itemView.findViewById(R.id.contact_police);
            name = itemView.findViewById(R.id.police_name);

        }
    }


}
