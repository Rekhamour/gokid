package com.gokids.yoda_tech.gokids.medical.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.eat.adapter.ImageSLiderAdapter;
import com.gokids.yoda_tech.gokids.eat.adapter.ReviewAdapter;
import com.gokids.yoda_tech.gokids.eat.model.Contact;
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.eat.model.Review;
import com.gokids.yoda_tech.gokids.eat.model.Specialization;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.medical.model.Doctor;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.StringTokenizer;

import me.relex.circleindicator.CircleIndicator;

public class MedicalDetail extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_CALL_PERMISSION = 123;
    TextView address, email, website, distance, timing, specialization;
    //ImageView img;
    RecyclerView reviews_list;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviews;
    Button write_review;
    MainBean m;
    String reviewText = "";
    String phoneNo = "";
    ImageButton bookmark,chat,direction;
    ScrollView scrollView;
    LinearLayout mLinearLayout;
    boolean bookmarkFlag = false;
    private ViewPager mViewpager;
    private CircleIndicator indicator;
    private SharedPreferences prefrence;
    private String TAG = getClass().getName();
    private ImageView thumbs_up;
    private TextView count_up;
    private TextView count_down;
    private ImageView thumb_down;
    private String upcount;
    private String downcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        prefrence= getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        address = findViewById(R.id.address_tv);
        email = findViewById(R.id.email_tv);
        website = findViewById(R.id.website_tv);
        timing = findViewById(R.id.timings_tv);
        specialization = findViewById(R.id.specialization_tv);
        distance = findViewById(R.id.distance_tv);
        thumbs_up = findViewById(R.id.thumbs_up_medical);
        count_up = findViewById(R.id.count_up_medical);
        count_down = findViewById(R.id.count_down_medical);
        thumb_down = findViewById(R.id.thumbs_down_medical);
        //img = (ImageView) findViewById(R.id.medical_detail_image);
        reviews_list = findViewById(R.id.reviews_list);
        bookmark = findViewById(R.id.bookmark);
        chat = findViewById(R.id.chat);
        direction = findViewById(R.id.direction);
        scrollView = findViewById(R.id.scrollVie);
        mLinearLayout = findViewById(R.id.doctors_list);
        Intent intent = getIntent();
        m = (MainBean) intent.getSerializableExtra("medical_data");

        mViewpager = findViewById(R.id.image_pager_md);
        if(m.getImages().size()>0)
        mViewpager.setAdapter(new ImageSLiderAdapter(MedicalDetail.this,m.getImages()));
        else
            mViewpager.setAdapter(new ImageSLiderAdapter(MedicalDetail.this,new ArrayList<String>()));
        indicator = findViewById(R.id.md_indicator);
        indicator.setViewPager(mViewpager);
       // setSupportActionBar(toolbar);

        toolbar.setTitle(m.getName());
        toolbar.setVisibility(View.GONE);
       // setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(m.getName());

        address.setText(m.getAddress());
        email.setText(m.getEmail());
        website.setText(m.getWebsite());
        timing.setText(m.getSchedule());
        getAllratingsthumbsdown();

        String speci = "";
        for (int i = 0;m.getSpecializations()!= null && i < m.getSpecializations().size(); i++, speci += ",") {
            speci += m.getSpecializations().get(i).getSpecializationHC();
        }
        specialization.setText(speci);
        distance.setText(m.getDistance() + "\nKms away");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    /*    Ion.with(img)
                .placeholder(R.drawable.med_error_image)
                .error(R.drawable.med_error_image)
                .load(m.getImages().get(0));*/

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reviews_list.setLayoutManager(layoutManager);
        reviews = new ArrayList<>();

        populateReviews();
        getIfBookmarked();

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefrence.getString("emailId", "").trim().isEmpty()) {
                    if (!bookmarkFlag) {
                        bookmarkFlag = true;
                        bookmark.setBackgroundResource(R.drawable.btn_badge_red_3x);
                        String url = Urls.BASE_URL + "api/setBookMark/email/" + MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("emailId", "") + "/class/CLS4/categoryItem/" + m.getMedicalID() + "/bookmark/1"+"/city/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("current_city","");
                        Log.e(TAG, "url" + url);
                        Ion.with(MedicalDetail.this)
                                .load(url)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        if (e == null) {
                                            String status = result.get("status").toString();
                                            Log.e(TAG, "status" + status);

                                            if (status.equalsIgnoreCase("200")) {


                                            }
                                        }

                                    }
                                });

                    } else {
                        bookmarkFlag = false;
                        bookmark.setBackgroundResource(R.drawable.btn_badge_3x);
                        String url = Urls.BASE_URL + "api/setBookMark/email/" + MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("emailId", "") + "/class/CLS4/categoryItem/" + m.getMedicalID() + "/bookmark/-"+"/city/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("current_city","");
                        Ion.with(MedicalDetail.this)
                                .load(url)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        if (e == null) {
                                            String status = result.get("status").toString();
                                            if (status.equalsIgnoreCase("200")) {

                                            }
                                        }

                                    }
                                });
                  /*  String url= Urls.BASE_URL+"api/setBookMark/email/"+prefrences.getString("emailId","")+"/class/CLS1/categoryItem/"+m.getRestaurantID()+"/bookmark/0";
                    Ion.with(FoodDetailActivity.this)
                            .load(url)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if(e==null)
                                    {
                                        String status= result.get("status").toString();
                                        if(status.equalsIgnoreCase("200"))
                                        {


                                        }
                                    }

                                }
                            });
*/
                    }
                } else {
                    Toast.makeText(MedicalDetail.this, "Please login or continue", Toast.LENGTH_SHORT).show();

                }
            }
        });


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MedicalDetail.this);
                dialog.setContentView(R.layout.review_layout);
                // set the custom dialog components - text, image and button
                final EditText input = dialog.findViewById(R.id.review_text);
                Button dialogButton = dialog.findViewById(R.id.btn_submit_review);

                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Log.e(TAG,"I M in click");
                        postReview(input.getText().toString());
                    }
                });

                dialog.show();

            }
        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, specialization.getBottom());
                    }
                });
            }
        });
        thumb_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ion.with(MedicalDetail.this)
                        .load(Urls.BASE_URL+"api/setRate/email/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("emailId","")+"/class/CLS4/categoryItem/"+m.getMedicalID()+"/rate/0"+"/city/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("current_city",""))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if(e==null)
                                {
                                    Log.e(TAG," thumbs_ down result"+result.toString());
                                    String status= result.get("status").toString();
                                    String message= result.get("message").toString();
                                    Log.e(TAG,"message thumb down"+message);
                                    getAllratingsthumbsdown();
                                    Toast.makeText(MedicalDetail.this, message, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Log.e(TAG," exception thumb down "+ e.getMessage());

                                }

                            }
                        });

            }
        });
        getAllratingsthumbsdown();
        thumbs_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ion.with(MedicalDetail.this)
                        .load(Urls.BASE_URL+"api/setRate/email/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("emailId","")+"/class/CLS4/categoryItem/"+m.getMedicalID()+"/rate/1"+"/city/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("current_city",""))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                if (e == null) {
                                    Log.e(TAG, " thumbs_ up result" + result.toString());
                                    String status = result.get("status").toString();
                                    String message = result.get("message").toString();

                                    getAllratingsthumbsdown();

                                    Toast.makeText(MedicalDetail.this, message, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Log.e(TAG," exception"+ e.getMessage());
                                }



                            }
                        });

            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MedicalDetail.this);
                builderSingle.setIcon(R.drawable.call);
                builderSingle.setTitle("Call..");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MedicalDetail.this, android.R.layout.select_dialog_singlechoice);
                for (int i = 0; i < m.getContacts().size(); i++) {
                    arrayAdapter.add(m.getContacts().get(i).getPhoneNo());
                }

                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phoneNo = arrayAdapter.getItem(which);
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
                        startActivity(intent);
                        /*if (ContextCompat.checkSelfPermission(MedicalDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MedicalDetail.this, new String[]{
                                    Manifest.permission.CALL_PHONE}, MY_CALL_PERMISSION);
                        }
                        else{
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNo));
                            startActivity(intent);
                        }*/
                    }
                });
                builderSingle.show();
            }
        });

    }


    private void getIfBookmarked() {
        Ion.with(this)
                .load(Urls.BASE_URL+"api/viewAllBookmarks/email/"+prefrence.getString("emailId","")+"/class/CLS4"+"/city/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("current_city",""))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null) {
                            // do stuff with the result or error
                            JsonArray res = result.getAsJsonObject().get("result").getAsJsonArray();
                            Log.e(TAG,"res in bookmark"+res.toString());
                            try {
                                JSONArray resarrey= new JSONArray(res.toString());


                                System.out.println(res);
                                for(int i= 0;i<resarrey.length();i++)
                                {
                                    JSONObject obj= resarrey.getJSONObject(i);
                                    String id=obj.getString("MedicalID");
                                    if(id.equalsIgnoreCase(m.getMedicalID()))
                                    {
                                        bookmarkFlag = true;
                                        bookmark.setBackgroundResource(R.drawable.btn_badge_red_3x);
                                    }

                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        else
                        {
                            Toast.makeText(MedicalDetail.this, getResources().getString(R.string.oops), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_CALL_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                    startActivity(intent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void postReview(String reviewText) {
    /*    Ion.with(this)
                .load("http://52.77.82.210/api/addDeleteReview/reviewee/" + m.getMedicalID() + "/review/" + reviewText + "/email/"+prefrence.getString("emailId","")+"/reviewID/-")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null) {
                            Log.e(TAG,"result in post review"+result.toString());
                            // do stuff with the result or error
                            if (result.getAsJsonObject().get("status").getAsString().equals("200")) {
                                Toast.makeText(MedicalDetail.this, "Review added successfully. Thanks", Toast.LENGTH_SHORT).show();
                                populateReviews();
                            }
                        }
                        else
                        {
                            e.printStackTrace();
                        }
                    }
                });*/
        if (!prefrence.getString("emailId", "").toString().trim().isEmpty()) {
            String url = Urls.BASE_URL+"api/addDeleteReview/reviewee/" + m.getMedicalID() + "/review/" + reviewText + "/email/" + prefrence.getString("emailId", "") + "/reviewID/-"+"/city/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("current_city","");

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                Log.e(TAG, "result in post review" + response.toString());
                                // do stuff with the result or error
                                if (response.getString("status").equals("200")) {
                                    Toast.makeText(MedicalDetail.this, "Review added successfully. Thanks", Toast.LENGTH_SHORT).show();
                                    populateReviews();
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
            Volley.newRequestQueue(MedicalDetail.this).add(jsonRequest);
        }
        else {
            Toast.makeText(MedicalDetail.this, "Please Login first to post your reviews", Toast.LENGTH_SHORT).show();
        }
    }

    public void populateReviews() {
        reviews = new ArrayList<>();
        Ion.with(getApplicationContext())
                .load(Urls.BASE_URL+"api/viewReviewPerReviewee/reviewee/" + m.getMedicalID()+"/city/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("current_city",""))
                .asString()

                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(e==null) {
                            Log.e(TAG,"String "+ result);

                            if (result != null) {
                                try {
                                    Object json = new JSONTokener(result).nextValue();
                                    if (json instanceof JSONObject)
                                    {

                                        Log.e(TAG,"obj out if"+json.toString());

                                        JSONArray arrey=((JSONObject) json).getJSONArray("result");
                                        if(arrey.length()>0) {
                                            for (int i = 0; i < arrey.length(); i++) {
                                                JSONObject obj = arrey.getJSONObject(i);
                                                Review r = new Review();
                                                r.setReview(obj.getString("Review"));
                                                r.setReviewer(obj.getString("Reviewer"));
                                                r.setUseername(obj.getString("Username"));
                                                reviews.add(r);
                                            }
                                            reviewAdapter = new ReviewAdapter(reviews);
                                            reviews_list.setAdapter(reviewAdapter);
                                            reviewAdapter.notifyDataSetChanged();
                                        }

                                    }
                                    else if(json instanceof JSONArray)
                                    {
                                        JSONArray arrey =((JSONArray) json);
                                        for (int i = 0; i < arrey.length(); i++) {
                                            JSONObject obj = arrey.getJSONObject(i);
                                            Review r = new Review();
                                            r.setReview(obj.getString("Review"));
                                            r.setReviewer(obj.getString("Reviewer"));
                                            r.setUseername(obj.getString("Username"));
                                            reviews.add(r);
                                        }
                                        reviewAdapter = new ReviewAdapter(reviews);
                                        reviews_list.setAdapter(reviewAdapter);
                                        reviewAdapter.notifyDataSetChanged();

                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }
                            else {
                                Log.e(TAG,"result" + result.toString());
                            }
                        }
                    }
                });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey check out this medical");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, m.getWebsite());
            startActivity(Intent.createChooser(sharingIntent, "Share via..."));
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if(item.getItemId()==R.id.house)
        {
            Intent intent = new Intent(MedicalDetail.this, GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String location = m.getLatLong();
        LatLng loc = null;
        if(location != null && location.contains(",")) {
            double lat = Double.parseDouble(location.split(",")[0].trim());
            double longi = Double.parseDouble(location.split(",")[1].trim());
            loc = new LatLng(lat, longi);
        }
        else{
            loc = new LatLng(1.23,103.23);
        }
        googleMap.addMarker(new MarkerOptions().position(loc)
                .title(m.getName()).snippet(m.getWebsite()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,0.5F));
    }
    private void getAllratingsthumbsdown() {
        String getthumbsdown="api/viewAllRatings/email/"+MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("emailId","") +"/class/CLS4/categoryItem/"+m.getMedicalID()+"/city/"+ MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("current_city","");
        Log.e("RATING THUMB","RATING THUMB"+getthumbsdown);
        Ion.with(MedicalDetail.this)
                .load(Urls.BASE_URL+getthumbsdown)
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if(e==null)
                {
                    Log.e(TAG,"rating  result"+ result);
                    //String resultString = result.get("result").getAsString();
                    //StringTokenizer ok = new StringTokenizer(resultString);
                    if(result.get("result") instanceof JsonArray) {
                        //getAsJsonArray();
                        Log.e(TAG, " all ratings tumnumber" + result.toString());
                        JsonArray resultarray= result.get("result").getAsJsonArray();
                        try {
                            JSONArray resultArray = new JSONArray(resultarray.toString());
                            if (resultArray.length() > 0) {
                                for (int i = 0; i < resultArray.length(); i++) {
                                    JSONObject obj = resultArray.getJSONObject(i);
                                    upcount = obj.getString("Up_Count");
                                    downcount = obj.getString("Down_Count");
                                    count_up.setText(upcount);
                                    count_down.setText(downcount);

                                }
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}