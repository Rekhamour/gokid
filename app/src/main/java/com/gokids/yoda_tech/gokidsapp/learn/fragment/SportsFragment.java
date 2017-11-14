package com.gokids.yoda_tech.gokidsapp.learn.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.learn.Util.AllTopics;
import com.gokids.yoda_tech.gokidsapp.learn.adapter.TopicsAdapter;
import com.gokids.yoda_tech.gokidsapp.learn.async.GetTopics;


/**
 * Created by manoj2prabhakar on 19/04/17.
 */

public class SportsFragment extends Fragment{
    ListView listView;
    public static TopicsAdapter topicsAdapter;
    AllTopics allTopics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subtopic,container,false);

        listView = (ListView) rootView.findViewById(R.id.listView);

        topicsAdapter = new TopicsAdapter(getContext(),R.layout.list_topics,null);
        listView.setAdapter(topicsAdapter);

        if(savedInstanceState==null){
            GetTopics getTopics = new GetTopics(getContext(), new GetTopics.AsyncCompleteTopics() {
                @Override
                public void processCompleted(Object obj) {
                    allTopics = (AllTopics) obj;
                    topicsAdapter.swapContent(allTopics.getSports());
                }
            });
            getTopics.execute(" ");
        }
        else {
            allTopics = (AllTopics) savedInstanceState.getSerializable("alltopics");
            topicsAdapter.swapContent(allTopics.getSports());
        }

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("alltopics",allTopics);
    }
}
