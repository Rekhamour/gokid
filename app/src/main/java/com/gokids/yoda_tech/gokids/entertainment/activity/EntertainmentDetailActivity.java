package com.gokids.yoda_tech.gokids.entertainment.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.gokids.yoda_tech.gokids.utils.Utils;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class EntertainmentDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_CALL_PERMISSION = 123;

    TextView address, email, website, distance, timing, cuisines;
    ImageView img;
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
    private ArrayList<Integer> indicatorarray = new ArrayList<Integer>();
    private CircleIndicator indicator;
    private static int  currentpage;
    private SharedPreferences prefrences;
    private String emailid;
    private Location latlon;
    private String TAG= getClass().getName();
    private TextView kidfinityscore_detail;
    private TextView knownfor;
    private TextView detail;
    private TextView endtime;
    private RatingBar addRatingView;
    private float selectedratings;
    private Date enddate;
    private Date startdate;
    private ImageView thumbs_up;
    private TextView count_up;
    private ImageView thumb_down;
    private TextView count_down;
    private String upcount;
    private String downcount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_detail);
        Intent intent = getIntent();
        m = (MainBean) intent.getSerializableExtra("medical_data");
        prefrences= getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        emailid=prefrences.getString("emailId","");
        latlon= Utils.getLatLong(EntertainmentDetailActivity.this);

        Toolbar toolbar = findViewById(R.id.entertainment_detail_toolbar);
        address = findViewById(R.id.entertainment_detail_address_tv);
        email = findViewById(R.id.entertainment_detail_email_tv);
        kidfinityscore_detail = findViewById(R.id.kidfinityscore_detail);
        website = findViewById(R.id.entertainment_detail_website_tv);
        timing = findViewById(R.id.entertainment_detail_timings_tv);
        endtime = findViewById(R.id.endtime_entertainment);
        addRatingView = findViewById(R.id.addRating);
        thumbs_up = findViewById(R.id.thumbs_up_entertain);
        count_up = findViewById(R.id.count_up_entertain);
        count_down = findViewById(R.id.count_down_entertain);
        thumb_down = findViewById(R.id.thumbs_down_entertain);
        knownfor = findViewById(R.id.entertainment_detail_knownfor_tv);
        distance = findViewById(R.id.entertainment_detail_distance_tv);
        reviews_list = findViewById(R.id.entertainment_detail_reviews_list);
        bookmark = findViewById(R.id.entertainment_detail_bookmark);
        chat = findViewById(R.id.entertainment_detail_chat);
        direction = findViewById(R.id.entertainment_detail_direction);
        scrollView = findViewById(R.id.entertainment_detail_scrollVie);
        mLinearLayout = findViewById(R.id.entertainment_detail_doctors_list);
        mViewpager = findViewById(R.id.entertainment_image_pager);
        mViewpager.setAdapter(new ImageSLiderAdapter(EntertainmentDetailActivity.this,m.getImages()));
        indicator = findViewById(R.id.entertainment_indicator);
        String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        Log.e(TAG,"startdate parsing "+ m.getStartDate());
        Log.e(TAG,"enddate parsing"+ m.getEndDate());
        try {
            startdate = formatter.parse(m.getStartDate());
            enddate = formatter.parse(m.getEndDate());
            Log.e(TAG,"startdate parsing "+ startdate);
            Log.e(TAG,"enddate parsing"+ enddate);
        } catch (ParseException e) {
            Log.e(TAG," parce exception");

            e.printStackTrace();
        }
        getRatings();
        addRatingView.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                selectedratings= rating;
                updateratings(selectedratings);

            }
        });


        indicator.setViewPager(mViewpager);
       int indicatorcount = m.getImages().size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                if (currentpage == m.getImages().size()) {
                    currentpage = 0;
                }
                mViewpager.setCurrentItem(currentpage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);

        setSupportActionBar(toolbar);



        //toolbar.setTitle(m.getRestaurantName());
        //toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(m.getEntertainmentTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        address.setText(m.getAddress()+","+m.getPostal());
        email.setText(m.getEmail());
        getAllratingsthumbsdown();

        website.setText(m.getWebsite());
        timing.setText("SartDate: "+ m.getStartDate().substring(0,11).replaceAll("\"",""));
        endtime.setText("EndDate: "+ m.getEndDate().substring(0,11).replaceAll("\"",""));
        knownfor.setText(m.getEntertainmentdetail());
        String speci = "";
        if(!m.getDistance().equalsIgnoreCase("nan"))
            distance.setText(m.getDistance() + "Km ");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

      /*  Ion.with(img)
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
                if (!prefrences.getString("emailId", "").trim().isEmpty()) {
                    if (!bookmarkFlag) {
                        bookmarkFlag = true;
                        bookmark.setBackgroundResource(R.drawable.btn_badge_red_3x);

                        String url = Urls.BASE_URL + "api/setBookMark/email/" + prefrences.getString("emailId", "") + "/class/CLS3/categoryItem/" + m.getEntertainmentID() + "/bookmark/1"+"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city","");
                        Log.e(TAG, "url" + url);
                        Ion.with(EntertainmentDetailActivity.this)
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

                    } else {
                        bookmarkFlag = false;
                        bookmark.setBackgroundResource(R.drawable.btn_badge_3x);
                        String url = Urls.BASE_URL + "api/setBookMark/email/" + prefrences.getString("emailId", "") + "/class/CLS3/categoryItem/" + m.getEntertainmentID() + "/bookmark/-"+"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city","");
                        Ion.with(EntertainmentDetailActivity.this)
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


                    }
                } else {
                    Toast.makeText(EntertainmentDetailActivity.this, "Please login first or continue", Toast.LENGTH_SHORT).show();

                }
            }
        });
        thumb_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ion.with(EntertainmentDetailActivity.this)
                        .load(Urls.BASE_URL+"api/setRate/email/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("emailId","")+"/class/CLS3/categoryItem/"+m.getEntertainmentID()+"/rate/0"+"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city",""))
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
                                    Toast.makeText(EntertainmentDetailActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Ion.with(EntertainmentDetailActivity.this)
                        .load(Urls.BASE_URL+"api/setRate/email/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("emailId","")+"/class/CLS3/categoryItem/"+m.getEntertainmentID()+"/rate/1"+"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city",""))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                if (e == null) {
                                    Log.e(TAG, " thumbs_ up result" + result.toString());
                                    String status = result.get("status").toString();
                                    String message = result.get("message").toString();

                                    getAllratingsthumbsdown();

                                    Toast.makeText(EntertainmentDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Log.e(TAG," exception"+ e.getMessage());
                                }



                            }
                        });

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailActivity.this);
              //  builder.setTitle("Write Review");


                final Dialog dialog = new Dialog(EntertainmentDetailActivity.this);
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
                        scrollView.smoothScrollTo(0, knownfor.getBottom());
                    }
                });
            }
        });


        FloatingActionButton fab = findViewById(R.id.entertainment_detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(EntertainmentDetailActivity.this);
                builderSingle.setIcon(R.drawable.call);
                builderSingle.setTitle("Call..");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EntertainmentDetailActivity.this, android.R.layout.select_dialog_singlechoice);
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
                       /* if (ContextCompat.checkSelfPermission(EntertainmentDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EntertainmentDetailActivity.this, new String[]{
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

    private void getRatings() {
        String ratings= String.valueOf(selectedratings);
        String path= Urls.BASE_URL+"api/viewRatingPerRatee/ratee/" + m.getEntertainmentID() +"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city","");
        Log.e(TAG,"path url for viewing ratings "+ path);

        Ion.with(EntertainmentDetailActivity.this)
                .load(path)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject obj=result.get(0).getAsJsonObject();
                        String rate=obj.get("Rate").toString();
                        if(!rate.equalsIgnoreCase("null"))
                        addRatingView.setRating(Float.parseFloat(rate));


                    }
                });

    }

    private void updateratings(float selectedratings) {
        String ratings= String.valueOf(selectedratings);
        String path= Urls.BASE_URL+"api/addDeleteRating/ratee/" + m.getEntertainmentID() + "/rating/" +ratings +"/email/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("emailId","").toString()+"/rateID/-"+"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city","") ;
        Log.e(TAG,"path url for adding ratings "+ path);

        Ion.with(EntertainmentDetailActivity.this)
                .load(path)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                    }
                });

    }


    private void getIfBookmarked() {
        Ion.with(this)
                .load(Urls.BASE_URL+"api/viewAllBookmarks/email/" +prefrences.getString("emailId","")+"/class/CLS3"+"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city",""))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        JsonArray res = result.getAsJsonObject().get("result").getAsJsonArray();
                        try {
                            JSONArray  resarrey= new JSONArray(res.toString());

                            System.out.println(res);
                            for(int i= 0;i<resarrey.length();i++)
                            {
                                JSONObject obj= resarrey.getJSONObject(i);
                                String id=obj.getString("EntertainmentID");
                                if(id.equalsIgnoreCase(m.getEntertainmentID()))
                                {
                                    bookmarkFlag = true;
                                    bookmark.setBackgroundResource(R.drawable.btn_badge_red_3x);
                                }

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
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
        if (!prefrences.getString("emailId", "").toString().trim().isEmpty()) {
            String url = Urls.BASE_URL + "api/addDeleteReview/reviewee/" + m.getEntertainmentID() + "/review/" + reviewText + "/email/" + prefrences.getString("emailId", "") + "/reviewID/-"+"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city","");
            Log.e(TAG, "url" + url);


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            if (response != null) {
                                Log.e(TAG, "result" + response.toString());
                                // do stuff with the result or erro
                                // r
                                try {
                                    JSONObject result = new JSONObject(response);
                                    if (result.get("status").equals("200")) {
                                        Toast.makeText(EntertainmentDetailActivity.this, "Review added successfully. Thanks", Toast.LENGTH_SHORT).show();
                                        populateReviews();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // Error handling
                    System.out.println("Something went wrong!");
                    error.printStackTrace();

                }
            });
            Volley.newRequestQueue(this).add(stringRequest);

        }
        else {
            Toast.makeText(EntertainmentDetailActivity.this, "Please Login first to post your reviews", Toast.LENGTH_SHORT).show();
        }
    }

    public void populateReviews() {
        reviews = new ArrayList<>();
        String apipath=Urls.BASE_URL+"api/viewReviewPerReviewee/reviewee/" + m.getEntertainmentID()+"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city","");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apipath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                            if (response != null) {
                                try {
                                    Object json = new JSONTokener(response).nextValue();
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
                                    else if (json instanceof JSONArray)
                                    {
                                        Log.e(TAG,"obj out if"+json.toString());

                                        JSONArray arrey=((JSONArray) json);
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




                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }
                            else {
                                Log.e(TAG,"result" + response.toString());
                            }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

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
            Intent intent= new Intent(EntertainmentDetailActivity.this, GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);

            finish();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String location = m.getLatlong();
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

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15));
    }
    private void getAllratingsthumbsdown() {
        String getthumbsdown="api/viewAllRatings/email/"+MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("emailId","") +"/class/CLS3/categoryItem/"+m.getEntertainmentID()+"/city/"+ MySharedPrefrence.getPrefrence(EntertainmentDetailActivity.this).getString("current_city","");
        Ion.with(EntertainmentDetailActivity.this)
                .load(Urls.BASE_URL+getthumbsdown)
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if(e==null)
                {
                    JsonArray resultarray = result.get("result").getAsJsonArray();
                    Log.e(TAG," all ratings tumnumber" + result.toString());
                    try {
                        JSONArray resultArray= new JSONArray( resultarray.toString());
                        if(resultArray.length()>0)
                        {
                            for(int i=0;i<resultArray.length();i++)
                            {
                                JSONObject obj= resultArray.getJSONObject(i);
                                upcount=  obj.getString("Up_Count");
                                downcount=   obj.getString("Down_Count");
                                count_up.setText(upcount);
                                count_down.setText(downcount);

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