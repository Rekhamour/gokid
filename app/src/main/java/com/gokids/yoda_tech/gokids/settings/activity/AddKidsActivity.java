package com.gokids.yoda_tech.gokids.settings.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.settings.fragment.AgeFragment;
import com.gokids.yoda_tech.gokids.settings.fragment.AllergyFragment;
import com.gokids.yoda_tech.gokids.settings.fragment.AnotherFragment;
import com.gokids.yoda_tech.gokids.settings.fragment.CuisineFragment;
import com.gokids.yoda_tech.gokids.settings.fragment.DietFragment;
import com.gokids.yoda_tech.gokids.settings.fragment.GenderFragment;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class AddKidsActivity extends AppCompatActivity {

    public static ViewPager mViewPager;
    public static TabLayout mTabLayout;
    private static String TAG= "Addkidsdetails";
    FragmentStatePagerAdapter mFragmentPagerAdapter;
   public  static SeekBar mSeekbar;
  public    static TextView profileprogress;
   public static String mGender;
   public static String mAge;
   public static String KidId="-";
   public  static ArrayList<String> SelectedNeeds=new ArrayList<>();
   public  static ArrayList<String> AllergyNeeds=new ArrayList<>();
   public  static ArrayList<String> DeitNeeds=new ArrayList<>();
   public  static ArrayList<String> CuisineNeeds=new ArrayList<>();
    public static String YesNo;
    public String flag;
    public String kidid;
    public int mProgress=4;
    public String gender;
    public String age="0";
    public String allergytext="Tell us about any allergies so we can personalise dining \r\nsafe for your kids.";
    private String resultforspecialneed;
    private Button continuebtn;
    private TextView skipbtn;
    private LinearLayout indicatorLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kids);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        AllergyNeeds.clear();
        DeitNeeds.clear();
        CuisineNeeds.clear();
        getIntentsvalues();
        mTabLayout = (TabLayout) findViewById(R.id.add_kid_tablayout);
        indicatorLL = (LinearLayout) findViewById(R.id.indicators_layout);
        setupSeekBar();
        setupViewPager();
    }

    private void getIntentsvalues() {
        Intent intent= getIntent();
        Log.e("intent"," i m in intent");
        flag = intent.getStringExtra("flag");
        Log.e("tag","flag"+flag);
        if(flag.equalsIgnoreCase("1"))
        {
            age= intent.getStringExtra("age");
            gender= intent.getStringExtra("gender");
            AllergyNeeds= intent.getStringArrayListExtra("allergyiesArrey");
            DeitNeeds= intent.getStringArrayListExtra("DietsArrey");
            CuisineNeeds= intent.getStringArrayListExtra("CousineArrey");
            Log.e("gender","gender"+gender);
            kidid= intent.getStringExtra("kidid");
            KidId=kidid;

           // getAllspecialNeeds(kidid);
            GenderFragment.newInstance(gender,age,"About your kid");
            AgeFragment.newInstance(age);
            AllergyFragment.newInstance(AllergyNeeds);
            DietFragment.newInstance(DeitNeeds);
            CuisineFragment.newInstance(CuisineNeeds);

        }
        else
        {
            age=" ";
            AgeFragment.newInstance(age);
            gender=" ";
            kidid="-";
            GenderFragment.newInstance(gender, age, "It is a ");
            AllergyNeeds.clear();
            DeitNeeds.clear();
            CuisineNeeds.clear();
            AllergyFragment.newInstance(AllergyNeeds);
            DietFragment.newInstance(DeitNeeds);
            CuisineFragment.newInstance(CuisineNeeds);
        }

    }

    private void getAllspecialNeeds(String kidid) {
        Log.e(TAG,"i m in method");
        String path= Urls.BASE_URL+"api/viewAllKidSpecialNeed/kidID/"+ kidid;
        Log.e(TAG,"i m in path" + path);

        Ion.with(AddKidsActivity.this)
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
                                        AllergyFragment.newInstance(AllergyNeeds);
                                        DietFragment.newInstance(DeitNeeds);
                                        CuisineFragment.newInstance(CuisineNeeds);
                                    }

                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }

                    }
                });


    }

    private void makeAlllist(String specialNeedCategory, String specialNeed) {
        Log.e(TAG,"i m in method2");
        if(specialNeedCategory.equalsIgnoreCase("Allergy"))
        {
            AllergyNeeds.add(specialNeed);

        }
        else if(specialNeedCategory.equalsIgnoreCase("Cuisine"))
        {
            DeitNeeds.add(specialNeed);

        }
        else if(specialNeedCategory.equalsIgnoreCase("diet"))
        {
            CuisineNeeds.add(specialNeed);

        }
    }

    public void setupSeekBar() {

        mSeekbar = (SeekBar) findViewById(R.id.seekbar_addkid);
        continuebtn = (Button) findViewById(R.id.btn_continue);
        skipbtn = (TextView) findViewById(R.id.skip_addkid);
        mSeekbar.setThumb(getResources().getDrawable(android.R.color.transparent));
        profileprogress = (TextView) findViewById(R.id.profile_progress);
        // if(mViewPager.getCurrentItem()==0)
        // {
        setSeekBarProgress(0);
        //}
        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equalsIgnoreCase("1")) {
                    // handleContinueclick(flag);
                    if (AddKidsActivity.mViewPager.getCurrentItem() != 3) {
                        AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem() + 1);
                        if (mViewPager.getCurrentItem() == 2) {
                            Log.e(TAG, "i m here in 2 click");
                            continuebtn.setText("Done");
                            finish();
                            //AddKidsActivity.mViewPager.setCurrentItem(3);
                            //indicatorLL.setVisibility(View.GONE);
                        }
                        else if(mViewPager.getCurrentItem()==3) {
                             finish();
                        }
                    }

                }
                    else{
                        if (AddKidsActivity.mViewPager.getCurrentItem() != 4) {
                            AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem() + 1);

                            if (mViewPager.getCurrentItem() == 3) {
                                Log.e(TAG, "i m here in 3 click");
                                continuebtn.setText("Done");
                                //indicatorLL.setVisibility(View.GONE);
                            }
                            else if(mViewPager.getCurrentItem()==4) {
                                indicatorLL.setVisibility(View.GONE);

                            }

                        }
                }
            }
        });
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equalsIgnoreCase("1")) {
                    // handleContinueclick(flag);
                    if(mViewPager.getCurrentItem()==0)
                    {
                        AllergyFragment.newInstance(AllergyNeeds);

                    }
                    if (mViewPager.getCurrentItem() == 2) {
                        Log.e(TAG, "i m here in 2 click");
                        continuebtn.setText("Done");

                        //setSeekBarProgress(60);
                    } else if (mViewPager.getCurrentItem() == 3) {
                        Log.e(TAG, "i m here 3 click");
                        //setSeekBarProgress(80);
                        continuebtn.setVisibility(View.GONE);
                        indicatorLL.setVisibility(View.GONE);
                        updateKidsdetails();
                        if (flag.equalsIgnoreCase("1")) {
                            Intent intent = new Intent(AddKidsActivity.this, SettingsActivity.class);
                            intent.putExtra("flag", "1");
                            startActivity(intent);
                            finish();
                            //redirect();
                        }
                    }


                    if (AddKidsActivity.mViewPager.getCurrentItem() != 3) {
                        AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem() + 1);
                    } else {

                        //redirect();
                    }
                } else {
                    //handleContinueclick(flag);

                    if (mViewPager.getCurrentItem() == 2) {
                        Log.e(TAG, "i m here in 2 click");
                        continuebtn.setText("Done");

                        //setSeekBarProgress(60);
                    } else if (mViewPager.getCurrentItem() == 3) {
                        Log.e(TAG, "i m here 3 click");
                        //setSeekBarProgress(80);
                        continuebtn.setVisibility(View.GONE);
                        indicatorLL.setVisibility(View.GONE);
                        adddetails();

                    }

                    if (AddKidsActivity.mViewPager.getCurrentItem() != 4) {

                        if (!AddKidsActivity.mAge.trim().isEmpty() && !AddKidsActivity.mGender.trim().isEmpty()) {
                            AddKidsActivity.mViewPager.setCurrentItem(AddKidsActivity.mViewPager.getCurrentItem() + 1);
                        } else {
                            Toast.makeText(AddKidsActivity.this, "Please select Gender/Age first", Toast.LENGTH_SHORT).show();
                        }
                    } else {


                    }
                }
            }
        });
    }







public void handleContinueclick(String flag){
    if(mViewPager.getCurrentItem()==0)
    {


        //setSeekBarProgress(20);
    }
    else if(mViewPager.getCurrentItem()==1)
    {
       // setSeekBarProgress(40);
    }
    else if(mViewPager.getCurrentItem()==2)
    {
        Log.e(TAG,"i m here in 2 click");
        continuebtn.setText("Done");

        //setSeekBarProgress(60);
    }
    else if(mViewPager.getCurrentItem()==3)
    {
        Log.e(TAG,"i m here 3 click");
        //setSeekBarProgress(80);
        continuebtn.setVisibility(View.GONE);
        indicatorLL.setVisibility(View.GONE);
        callApis();
        if(flag.equalsIgnoreCase("1")) {
            Intent intent = new Intent(AddKidsActivity.this, SettingsActivity.class);
            intent.putExtra("flag","1");
            startActivity(intent);
            finish();
            //redirect();
        }





        //setSeekBarProgress(100)
        }

}
    public void callApis()
    {
        if(AddKidsActivity.KidId.equalsIgnoreCase("-"))
        {
            adddetails();

        }
        else  if(!AddKidsActivity.KidId.equalsIgnoreCase("-"))
        {
            updateKidsdetails();
        }

    }

    private void updateKidsdetails() {
        JSONArray jsonArray = new JSONArray();
        ArrayList<String> list= AddKidsActivity.getSelectedNeeds();
        Log.e(TAG,"size"+ list.toString());
        for(int i = 0; i< list.size(); i++)
        {
            JSONObject jsonObject= new JSONObject();

            try {
                jsonObject.put("SpecialNeed",list.get(i).toString());
                jsonArray.put(i,jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // Log.e(TAG,"jsonarrey value"+ jsonArray.toString());

        String url= Urls.BASE_URL+"api/updateKidProfile/kidID/"+AddKidsActivity.KidId+"/age/"+AddKidsActivity.mAge+"/gender/"+AddKidsActivity.mGender+"/specialNeed/"+jsonArray.toString();
        //String apipath= Urls.BASE_URL+"api/addDeleteKidDetail/email/"+MySharedPrefrence.getPrefrence(getActivity()).getString("emailId","")+"/age/"+AddKidsActivity.mAge+"/gender/"+AddKidsActivity.mGender+"/specialNeed/"+jsonArray.toString()+"/kidID/-";
        Log.e(TAG,"url"+url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            Log.e(TAG," i m there  in response "+ response.toString());
                            String status= response.get("status").toString();
                            String message= response.get("message").toString();
                            if(status.equalsIgnoreCase("200"))
                            {
                                Toast.makeText(AddKidsActivity.this, message, Toast.LENGTH_SHORT).show();
                               // redirect();

                            }

                            // response = response.getJSONObject("args");
                            //String site = response.getString("site"),
                            //   network = response.getString("network");
                            // System.out.println("Site: "+site+"\nNetwork: "+network);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(AddKidsActivity.this).add(jsonRequest);
    /*   StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("update result","update result"+response);

                        // Result handling
                       // System.out.println(response.substring(0,100));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);*/






     /*
        Ion.with(getActivity())
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.e(TAG,"update result"+result+ "exception");
                        e.printStackTrace();

                    }
                });*/

       /* Ion.with(getActivity())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null)
                        {
                            String status= result.get("status").toString();
                            String message= result.get("message").toString();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            e.printStackTrace();
                        }

                    }
                });
*/

    }
    public  void adddetails()
    {
        JSONArray jsonArray = new JSONArray();
        //bean.setNeeds(bean.getAlleryneeds());
        ArrayList<String> list= AddKidsActivity.getSelectedNeeds();
        Log.e(TAG,"size"+ list.toString());


        for(int i = 0; i< list.size(); i++)
        {
            JSONObject jsonObject= new JSONObject();

            try {
                jsonObject.put("SpecialNeed",list.get(i).toString());
                jsonArray.put(i,jsonObject);
                //Log.e(TAG,"arrey value"+ list.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        String apipath= Urls.BASE_URL+"api/addDeleteKidDetail/email/"+ MySharedPrefrence.getPrefrence(AddKidsActivity.this).getString("emailId","")+"/age/"+AddKidsActivity.mAge+"/gender/"+AddKidsActivity.mGender+"/specialNeed/"+jsonArray.toString()+"/kidID/-";

        Log.e(TAG,"url"+apipath);
      /*  Ion.with(AddKidsActivity.this)
                .load(apipath)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String response) {
                        if(e==null)
                        {
                            Log.e("add result","add result"+response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.get("status").toString();
                                String message = result.get("message").toString();
                                if(status.equalsIgnoreCase("200"))
                                {
                                    // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    //redirect();
                                }
                                Toast.makeText(AddKidsActivity.this, message, Toast.LENGTH_SHORT).show();
                                // redirect();


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        else
                        {
                            e.printStackTrace();

                            Toast.makeText(AddKidsActivity.this, getResources().getString(R.string.oops), Toast.LENGTH_SHORT).show();


                        }

                    }
                });*/

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apipath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("add result","add result"+response);
                        JSONObject result = null;
                        try {
                            result = new JSONObject(response);
                            String status = result.get("status").toString();
                            String message = result.get("message").toString();
                            if(status.equalsIgnoreCase("200"))
                            {
                                // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                //redirect();
                            }
                            Toast.makeText(AddKidsActivity.this, message, Toast.LENGTH_SHORT).show();
                           // redirect();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        // Result handling
                        // System.out.println(response.substring(0,100));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });
        Volley.newRequestQueue(AddKidsActivity.this).add(stringRequest);

       /* Ion.with(getActivity())
                .load(apipath)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null)
                        {
                            String status= result.get("status").toString();
                            String message= result.get("message").toString();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                })*/;


        /*Ion.with(getActivity())
                .load(Urls.BASE_URL+"api/addDeleteKidDetail/email/"+MySharedPrefrence.getPrefrence(getActivity()).getString("emailId","")+"/age/"+AddKidsActivity.mAge+"/gender/"+AddKidsActivity.mGender+"/specialNeed/"+jsonArray.toString()+"/kidID/-")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(e==null)
                        {
                            Log.e(TAG, "result" + result.toString());

                          *//*  String status = result.get("status").toString();
                            String message = result.get("message").toString();
                            Log.e(TAG, "status  " + status);
                            Log.e(TAG, "message " + message);
*//*



                        }
                        else
                        {
                            Log.e(TAG,"exception");
                            e.printStackTrace();
                        }



                          *//*  if(status.equalsIgnoreCase("200"))
                            {
                               // Log.e(TAG," i m done");
                            }*//*





                    }
                });*/
    }
    private void redirect() {
        if(AddKidsActivity.YesNo.equalsIgnoreCase("Y"))
        {
            Intent intent = new Intent(AddKidsActivity.this, SettingsActivity.class);
            intent.putExtra("flag","0");
            startActivity(intent);
            finish();

            //AddKidsActivity.mViewPager.setCurrentItem(0);
        }
        else if(AddKidsActivity.YesNo.equalsIgnoreCase("N"))
        {
            Intent intent = new Intent(AddKidsActivity.this, GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
            finish();

        }
    }

    public static void setSeekBarProgress(int progress) {
        Log.e(TAG,"progress"+progress);
       // mSeekbar.setMax(100);
        mSeekbar.setProgress(progress);

        profileprogress.setText((progress)+"% Profile Completed");
    }
    public static void setGender(String gender) {
        mGender= gender;

    }
    public ArrayList<String> setSpecialNeeds(String needs) {
        SelectedNeeds.add(needs);
        return SelectedNeeds;

    }
public static ArrayList<String> getSelectedNeeds()
{
    SelectedNeeds.clear();
    SelectedNeeds.addAll(AddKidsActivity.AllergyNeeds);
    SelectedNeeds.addAll(AddKidsActivity.DeitNeeds);
    SelectedNeeds.addAll(AddKidsActivity.CuisineNeeds);
    return SelectedNeeds;
}
    public static void setAllerygies(ArrayList<String> list)
    {
        AllergyNeeds=list;
        Log.e(TAG,"AllergyNeeds size"+AllergyNeeds);

    }

    public static void setDiets(ArrayList<String> list)
    {
        DeitNeeds=list;
        Log.e(TAG,"DeitNeeds size"+DeitNeeds);

    }

    public static void setCuisines(ArrayList<String> list)
    {
        CuisineNeeds=list;
        Log.e(TAG,"cuisineneeds size"+CuisineNeeds);
    }


    public static void setAge(String age) {
        mAge=age;
        Log.e("Age in activity","age in activity"+age);

    }

    public void setupViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.addKid_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0)
                {
                    setSeekBarProgress(0);
                }
               else if(position==1)
                {
                    setSeekBarProgress(25);
                }
                else if(position==2)
                {
                    setSeekBarProgress(50);
                }
                else if(position==3)
                {
                    setSeekBarProgress(75);
                }
                else if(position==4)
                {
                    setSeekBarProgress(100);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mTabLayout.setupWithViewPager(mViewPager);



        mFragmentPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                       fragment=   new GenderFragment();
                        break;
                    case 1:
                        fragment=  AllergyFragment.newInstance(AllergyNeeds);
                        break;
                    case 2:
                        fragment=  new DietFragment();
                        break;
                    case 3:
                        fragment=  new CuisineFragment();
                        break;
                    case 4:
                        fragment=  new AnotherFragment();
                        break;

                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 5;
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

            }
        };

        mViewPager.setAdapter(mFragmentPagerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
            return true;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
