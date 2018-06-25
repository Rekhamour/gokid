package com.gokids.yoda_tech.gokids.learn.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.learn.Util.Contacts;
import com.gokids.yoda_tech.gokids.learn.Util.ProviderDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 20/04/17.
 */

public class ProviderClassAdapter extends BaseAdapter {

    private ArrayList<ProviderDetails> providerDetailses;
    private Context mContext;

    public ProviderClassAdapter(Context c,ArrayList<ProviderDetails> p){
        mContext = c;
        providerDetailses = p;
    }

    @Override
    public int getCount() {
        if(providerDetailses!=null){
            return providerDetailses.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return providerDetailses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_class_details, null);


        ImageView bgImage = view.findViewById(R.id.imageView);
        TextView titleView = view.findViewById(R.id.title);
        TextView name = view.findViewById(R.id.name);
        TextView address = view.findViewById(R.id.address);
        TextView amount = view.findViewById(R.id.amount);
        TextView distance = view.findViewById(R.id.distance);
        TextView kidScore = view.findViewById(R.id.kidScore);
        TextView kidsTit = view.findViewById(R.id.kidsTit);
        Button call = view.findViewById(R.id.call);



        if(providerDetailses!=null & providerDetailses.size()!=0){
            final ProviderDetails details = providerDetailses.get(i);
            Picasso.with(mContext).load(details.getImages().get(0).getImageURL())
                    .into(bgImage);

            titleView.setText(details.getTitle());
            name.setText(details.getName());
            address.setText(details.getAddress());
            amount.setText(details.getPriceSummary() + "/" +"\n"+ details.getPricePrefix());
            distance.setText(details.getDistance() + " km");
            kidScore.setText(details.getKidsAffinityScore());
            kidsTit.setText("Kids Friendly");
            view.setTag(i);


            final Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.dialog_call);

            ListView lv = dialog.findViewById(R.id.lv);
            dialog.setCancelable(true);
            dialog.setTitle("ListView");

            ArrayList<String> names = new ArrayList<>();

            for (Contacts contacts: details.getContacts()){
                names.add(contacts.getPhoneNumber());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.layout_text,names);
            lv.setAdapter(adapter);

            //Button callButton = (Button) view.findViewById(R.id.call);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + details.getContacts().get(i).getPhoneNumber()));
                    mContext.startActivity(intent);
                }
            });



        }

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                Intent intent = new Intent(mContext, DisplayClassActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("details",providerDetailses.get(position));
                mContext.startActivity(intent);
            }
        });*/


        return view;
    }

    public void swapItems(ArrayList<ProviderDetails> items){
        this.providerDetailses = items;
        notifyDataSetChanged();
    }
}
