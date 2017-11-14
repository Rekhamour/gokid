package com.gokids.yoda_tech.gokidsapp.eat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.eat.adapter.FoodAdapter;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.Entertainment;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.EntertainmentCategoryBean;
import com.gokids.yoda_tech.gokidsapp.entertainment.activity.adapter.EntertainCategoriesAdapter;
import com.gokids.yoda_tech.gokidsapp.utils.Urls;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


/**
 * Created by benepik on 23/6/17.
 */

public class EntertainmentFragment extends Fragment implements FoodAdapter.ItemClickCallback {
    private RecyclerView categoriesListview;
    private String TAG= getClass().getName();
    private ArrayList<EntertainmentCategoryBean> categoryBeanArrayList= new ArrayList<>();
    private LinearLayoutManager layoutmanager;
    private EntertainCategoriesAdapter adapter;

    public static EntertainmentFragment newInstance(){
        EntertainmentFragment itemOnFragment = new EntertainmentFragment();
        return itemOnFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.entertain_food_layout, container, false);
        categoriesListview= (RecyclerView)view.findViewById(R.id.entertainment_cat_list);
        layoutmanager= new LinearLayoutManager(getActivity());
        adapter= new EntertainCategoriesAdapter(getActivity(),categoryBeanArrayList);
        categoriesListview.setLayoutManager(layoutmanager);
        categoriesListview.setAdapter(adapter);
        getAllcategories();
        return  view;

    }

    private void getAllcategories() {
        String All_sucategories= Urls.BASE_URL+"api/viewAllCategory/class/CLS3";
        Ion.with(getActivity())
                .load(All_sucategories)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if(e==null) {
                            Log.e(TAG, "result " + result.toString());
                            for(int i = 0;i<result.size();i++)
                            {
                                JsonObject obj= result.get(i).getAsJsonObject();
                                String CategoryID = obj.get("CategoryID").getAsString();
                                String Category = obj.get("Category").getAsString();
                                String ImageURL = obj.get("ImageURL").getAsString();
                                EntertainmentCategoryBean bean= new EntertainmentCategoryBean();
                                bean.setCategory(Category);
                                bean.setCategoryID(CategoryID);
                                bean.setImageURL(ImageURL);
                                categoryBeanArrayList.add(bean);

                            }
                            adapter.notifyDataSetChanged();
                           // result.getAsJsonObject();
                        }

                    }
                });
    }

    @Override
    public void onItemClick(int p) {

    }
}
