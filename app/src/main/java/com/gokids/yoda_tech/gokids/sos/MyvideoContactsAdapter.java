package com.gokids.yoda_tech.gokids.sos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokids.yoda_tech.gokids.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyvideoContactsAdapter extends ArrayAdapter {

    private View view;
    public Context mcontext;
    public List<SenderLocationsBean> list;

    public MyvideoContactsAdapter(Context context, int resource, int textViewResourceId, List objects) {
super(context, resource, textViewResourceId, objects);
}
    public MyvideoContactsAdapter(Context context, int textViewResourceId, ArrayList<SenderLocationsBean> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
        mcontext=context;

    }

@Override
public int getCount() {
return super.getCount();
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
    view = LayoutInflater.from(getContext()).inflate(R.layout.videocontacts_single_row, null);//set layout for displaying items
    ImageView icon = (ImageView) view.findViewById(R.id.video_call_icon);//get id for image view
    TextView names = (TextView) view.findViewById(R.id.SOS_name);//get id for image view
    TextView soscontact = (TextView) view.findViewById(R.id.SOS_contact);//get id for image view
    names.setText(list.get(position).getContactName());
    soscontact.setText(list.get(position).getContactPhone());
    icon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent callIntent = new Intent(mcontext,VideoActivity.class);
            mcontext.startActivity(callIntent);
        }
    });

    return view;}
}