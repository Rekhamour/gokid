package com.gokids.yoda_tech.gokidsapp.learn.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.learn.Util.SubTopic;

import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 19/04/17.
 */

public class TopicsAdapter extends ArrayAdapter<SubTopic> {
    public ArrayList<SubTopic> academic;
    private Context mContext;

    public TopicsAdapter(Context c, int textResourceId, ArrayList<SubTopic> topics){
        super(c, textResourceId, topics);
        this.academic = topics;
        mContext = c;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater.from(mContext));
            convertView = inflater.inflate(R.layout.list_topics,null);

            holder = new ViewHolder();
            holder.className = (TextView) convertView.findViewById(R.id.classText);
            holder.select = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);

            holder.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkBox = (CheckBox) view;
                    SubTopic topic = (SubTopic) checkBox.getTag();
                    academic.get(position).setSelected(checkBox.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        SubTopic subTopic = academic.get(position);
        holder.className.setText(subTopic.getTopicName());
        holder.select.setChecked(subTopic.isSelected());
        holder.select.setTag(subTopic);


        return convertView;
    }

    public class ViewHolder{
        TextView className;
        CheckBox select;
    }


    public void swapContent(ArrayList<SubTopic> subTopics){
        this.academic = subTopics;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        if(academic!=null){
            return academic.size();
        }
        return 0;
    }
}
