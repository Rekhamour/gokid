package com.gokids.yoda_tech.gokidsapp.settings.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.eat.model.Review;
import com.gokids.yoda_tech.gokidsapp.settings.activity.AddKidsActivity;
import com.gokids.yoda_tech.gokidsapp.settings.fragment.AllergyFragment;
import com.gokids.yoda_tech.gokidsapp.settings.fragment.CuisineFragment;
import com.gokids.yoda_tech.gokidsapp.settings.fragment.DietFragment;
import com.gokids.yoda_tech.gokidsapp.settings.model.KidsDetailBean;
import com.gokids.yoda_tech.gokidsapp.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokidsapp.utils.Urls;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rigoe on 6/3/2017.
 */

public class KidsDetailsAdapter extends RecyclerView.Adapter<KidsDetailsAdapter.MyViewHolder> {
    ArrayList<KidsDetailBean> list = new ArrayList<>();
    Context context;
    private String resultforspecialneed;

    private String TAG= getClass().getName();
    public String allergytext="Tell us about any allergies so we can personalise dining \r\nsafe for your kids.";
    private ArrayList<String> DeitNeeds=new ArrayList<>();
    private ArrayList<String> AllergyNeeds=new ArrayList<>();
    private ArrayList<String> CuisineNeeds= new ArrayList<>();


    public KidsDetailsAdapter(Context context,ArrayList<KidsDetailBean> list) {
        this.list = list;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kids_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //getAllspecialNeeds(list.get(position).getKidID());
        holder.detail.setText(list.get(position).getGender() +"("+ list.get(position).getAge() +" years"+")");
        if(list.get(position).getGender().toString().equalsIgnoreCase("M"))
        {
            holder.img.setImageResource(R.drawable.edit_boy);
        }
        else
        {
            holder.img.setImageResource(R.drawable.edit_girl);

        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllspecialNeeds(list.get(position).getKidID(), position);

               // openActivity(position);

            }
        });
        holder.delete.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletekid(position);
            }
        });

    }

    private void deletekid(int position) {
        getAllSpecialNeeds(position);

    }

    private void getAllSpecialNeeds(final int position) {

        if(!MySharedPrefrence.getPrefrence(context).getString("emailId","").trim().isEmpty()) {
            String path = Urls.BASE_URL + "http://52.77.82.210/api/viewAllKidSpecialNeed/kidID/" + list.get(position).getKidID();
            Ion.with(context)
                    .load(path)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (e == null) {
                                try {
                                    Object resultstring = new JSONTokener(result).nextValue();
                                    JSONArray areyspecialneed = new JSONArray();
                                    if (resultstring instanceof JSONObject) {
                                        resultforspecialneed = "";

                                    } else if (resultstring instanceof JSONArray) {
                                        JSONArray arrey = (JSONArray) resultstring;
                                        if (arrey.length() > 0) {
                                            for (int i = 0; i < arrey.length(); i++) {
                                                JSONObject obj = arrey.getJSONObject(i);
                                                String KidSpecialNeed = obj.getString("KidSpecialNeed");
                                                JSONObject objresult = new JSONObject();
                                                objresult.put("SpecialNeed", KidSpecialNeed);
                                                areyspecialneed.put(objresult);
                                                resultforspecialneed = areyspecialneed.toString();

                                            }
                                        }

                                    }
                                    deletekidnow(position);

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }

                        }
                    });
        }
        else
        {
            Toast.makeText(context, "Please Login or continue", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletekidnow(final int position) {
        String apipath= Urls.BASE_URL+"api/addDeleteKidDetail/email/"+MySharedPrefrence.getPrefrence(context).getString("emailId","")+"/age/"+list.get(position).getAge()+"/gender/"+list.get(position).getGender()+"/specialNeed/"+resultforspecialneed+"/kidID/"+list.get(position).getKidID();
        Ion.with(context)
                .load(apipath)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null)
                        {
                            String status= result.get("status").toString();
                            String message= result.get("message").toString();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            list.remove(position);
                            notifyDataSetChanged();
                        }

                    }
                });


    }

    private void openActivity(int position) {


        if(!MySharedPrefrence.getPrefrence(context).getString("emailId","").trim().isEmpty()) {
            Intent intent = new Intent(context, AddKidsActivity.class);
            intent.putExtra("flag", "1");
            intent.putExtra("age", list.get(position).getAge());
            intent.putExtra("kidid", list.get(position).getKidID());
            intent.putExtra("gender", list.get(position).getGender());
            intent.putExtra("allergyiesArrey", AllergyNeeds);
            intent.putExtra("DietsArrey", DeitNeeds);
            intent.putExtra("CousineArrey", CuisineNeeds);
            context.startActivity(intent);
        }
        else {
            Toast.makeText(context, "Please Login or continue", Toast.LENGTH_SHORT).show();
        }
       // intent.putExtra("gender",(Serializable)list.get(position).getNeedsbean());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img,delete;
        TextView detail;
        Button edit;
        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.kid_icon);
            detail = (TextView) itemView.findViewById(R.id.age_kid);
            edit = (Button) itemView.findViewById(R.id.edit_detail);
            delete = (ImageView) itemView.findViewById(R.id.delete_detail);
        }
    }
    private void getAllspecialNeeds(String kidid, final int position) {
        Log.e(TAG,"i m in method");
        AllergyNeeds= new ArrayList<>();
        CuisineNeeds= new ArrayList<>();
        DeitNeeds=  new ArrayList<>();
        String path= Urls.BASE_URL+"api/viewAllKidSpecialNeed/kidID/"+ kidid;
        Log.e(TAG,"i m in path" + path);

        Ion.with(context)
                .load(path)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(e==null)
                        {
                            Log.e(TAG,"i m in result ion");

                            try {
                                Object resultstring = new JSONTokener(result).nextValue();
                                JSONArray areyspecialneed =  new JSONArray();
                                if(resultstring instanceof JSONObject)
                                {
                                    Log.e(TAG,"i m in string");

                                    resultforspecialneed="";

                                }
                                else if(resultstring instanceof JSONArray)
                                {
                                    Log.e(TAG,"i m in methodarrey");

                                    JSONArray arrey = (JSONArray)resultstring;
                                    if(arrey.length()>0)
                                    {
                                        for(int i = 0;i<arrey.length();i++)
                                        {
                                            JSONObject obj= arrey.getJSONObject(i);
                                            String KidSpecialNeed = obj.getString("KidSpecialNeed");
                                            String KidSpecialNeedID = obj.getString("KidSpecialNeedID");
                                            String SpecialNeed = obj.getString("SpecialNeed");
                                            String SpecialNeedCategory = obj.getString("SpecialNeedCategory");
                                            Log.e(TAG,"special"+SpecialNeedCategory);
                                            if(SpecialNeedCategory.equalsIgnoreCase(allergytext))
                                            {
                                                AllergyNeeds.add(KidSpecialNeed);
                                                //Log.e(TAG," allergysize"+AllergyNeeds);


                                            }
                                            else if(SpecialNeedCategory.equalsIgnoreCase("What is your kid(s) favourite cuisine?"))
                                            {
                                                CuisineNeeds.add(KidSpecialNeed);

                                            }
                                            else if(SpecialNeedCategory.equalsIgnoreCase("What is your kid(s) diet?"))
                                            {
                                                DeitNeeds.add(KidSpecialNeed);

                                            }
                                            //makeAlllist(SpecialNeedCategory,SpecialNeed);
                                            //  SelectedNeeds.add(KidSpecialNeed);
                                            JSONObject objresult= new JSONObject();
                                            objresult.put("SpecialNeed",KidSpecialNeed);
                                            areyspecialneed.put(objresult);
                                            resultforspecialneed = areyspecialneed.toString();
                                            //Log.e(TAG," allergysize"+AllergyNeeds);


                                        }
                                        Log.e(TAG," allergysize"+AllergyNeeds);
                                        Log.e(TAG," DeitNeeds"+DeitNeeds);
                                        Log.e(TAG," CuisineNeeds"+CuisineNeeds);
                                        openActivity(position);
                                      //  AllergyFragment.newInstance(AllergyNeeds);
                                       // DietFragment.newInstance(DeitNeeds);
                                      //  CuisineFragment.newInstance(CuisineNeeds);
                                    }

                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }

                    }
                });


    }

}
