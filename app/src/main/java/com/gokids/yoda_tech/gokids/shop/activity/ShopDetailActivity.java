package com.gokids.yoda_tech.gokids.shop.activity;

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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class ShopDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_CALL_PERMISSION = 123;
    private static final String BASE_URL = "http://52.77.82.210/";
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
    private RatingBar addratingShopping;
    private float ratings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        Intent intent = getIntent();
        m = (MainBean) intent.getSerializableExtra("medical_data");
        prefrences= getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        emailid=prefrences.getString("emailId","");
        latlon= Utils.getLatLong(ShopDetailActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.shop_detail_toolbar);
        address = (TextView) findViewById(R.id.shop_detail_address_tv);
        email = (TextView) findViewById(R.id.shop_detail_email_tv);
        kidfinityscore_detail = (TextView) findViewById(R.id.kidfinityscore_detail);
        website = (TextView) findViewById(R.id.shop_detail_website_tv);
        timing = (TextView) findViewById(R.id.shop_detail_timings_tv);
        detail = (TextView) findViewById(R.id.shop_detail_specialization_tv);
        knownfor = (TextView) findViewById(R.id.shop_detail_knownfor_tv);
        distance = (TextView) findViewById(R.id.shop_detail_distance_tv);
        reviews_list = (RecyclerView) findViewById(R.id.shop_detail_reviews_list);
        write_review = (Button) findViewById(R.id.shop_detail_write_review);
        addratingShopping = (RatingBar) findViewById(R.id.addRating_shoping);
        bookmark = (ImageButton) findViewById(R.id.shop_detail_bookmark);
        chat = (ImageButton) findViewById(R.id.shop_detail_chat);
        direction = (ImageButton) findViewById(R.id.shop_detail_direction);
        scrollView = (ScrollView) findViewById(R.id.shop_detail_scrollVie);
        mLinearLayout = (LinearLayout) findViewById(R.id.shop_detail_doctors_list);
        mViewpager = (ViewPager) findViewById(R.id.shop_image_pager);
        mViewpager.setAdapter(new ImageSLiderAdapter(ShopDetailActivity.this,m.getImages()));
        indicator = (CircleIndicator) findViewById(R.id.shop_indicator);
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
        getRatings();

        addratingShopping.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings= rating;
                updateratings(ratings);

            }
        });



        //toolbar.setTitle(m.getRestaurantName());
        //toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(m.getShopName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        address.setText(m.getAddress()+","+m.getPostal());
        email.setText(m.getEmail());
        website.setText(m.getWebsite());
        timing.setText(m.getWorkingHour());
        knownfor.setText(m.getSpecialty());
        detail.setText(m.getShopDetail());

        //kidfinityscore_detail.setText(String.valueOf((m.getKidsfinityScore())));
        String speci = "";

       /* if(m.getCuisines().size()>0) {
            for (int i = 0; m.getCuisines() != null && i < m.getCuisines().size(); i++, speci += ",") {
                speci += m.getCuisines().get(i);
            }
            cuisines.setText(speci);
        }*/
       // Log.e(TAG,"cusine"+m.getCuisines().toString());
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
        getDoctorsForMedical(m.getShopID(),null,-91,-181,null,null,0,100);

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefrences.getString("emailId", "").trim().isEmpty()) {

                    if (!bookmarkFlag) {
                        bookmarkFlag = true;
                        bookmark.setBackgroundResource(R.drawable.btn_badge_red_3x);
                        String url = Urls.BASE_URL + "api/setBookMark/email/" + prefrences.getString("emailId", "") + "/class/CLS2/categoryItem/" + m.getShopID() + "/bookmark/1";
                        Log.e(TAG, "url" + url);
                        Ion.with(ShopDetailActivity.this)
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
                        String url = Urls.BASE_URL + "api/setBookMark/email/" + prefrences.getString("emailId", "") + "/class/CLS2/categoryItem/" + m.getShopID() + "/bookmark/-";
                        Ion.with(ShopDetailActivity.this)
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
                }
                else
                {
                    Toast.makeText(ShopDetailActivity.this, "Please login or continue", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailActivity.this);
              //  builder.setTitle("Write Review");


                final Dialog dialog = new Dialog(ShopDetailActivity.this);
                dialog.setContentView(R.layout.review_layout);
                // set the custom dialog components - text, image and button
                final EditText input = (EditText) dialog.findViewById(R.id.review_text);
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_submit_review);

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


              /* // builder.setView(reviewLL);
                final Dialog alertDialog = new Dialog(FoodDetailActivity.this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(R.layout.review_layout);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                CardView reviewLL = (CardView)getLayoutInflater().inflate(R.layout.review_layout,null);
                final EditText input = (EditText)reviewLL.findViewById(R.id.review_text);
                Button submit = (Button) reviewLL.findViewById(R.id.btn_submit_review);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG,"I M in click");
                        alertDialog.dismiss();
                        postReview(input.getText().toString());

                    }
                });*/
                // Set up the input
                //final EditText input = new EditText(FoodDetailActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


                // Set up the buttons
              /*  builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //reviewText = input.getText().toString();
                        if (!reviewText.trim().equals("")) {
                            postReview(reviewText);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });*/

              //  builder.show();
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.shop_detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ShopDetailActivity.this);
                builderSingle.setIcon(R.drawable.call);
                builderSingle.setTitle("Call..");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShopDetailActivity.this, android.R.layout.select_dialog_singlechoice);
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
                      /*  if (ContextCompat.checkSelfPermission(ShopDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ShopDetailActivity.this, new String[]{
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
      //  String ratings= String.valueOf(selectedratings);
        String path= Urls.BASE_URL+"api/viewRatingPerRatee/ratee/" + m.getShopID() ;
        Log.e(TAG,"path url for viewing ratings "+ path);

        Ion.with(ShopDetailActivity.this)
                .load(path)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject obj=result.get(0).getAsJsonObject();
                        String rate=obj.get("Rate").toString();
                        Log.e(TAG," get rating"+ rate);
                        if(!rate.equalsIgnoreCase("null"))
                            addratingShopping.setRating(Float.parseFloat(rate));


                    }
                });

    }

    private void updateratings(float selectedratings) {
        String ratings= String.valueOf(selectedratings);
        String path= Urls.BASE_URL+"api/addDeleteRating/ratee/" + m.getShopID() + "/rating/" +ratings +"/email/"+ MySharedPrefrence.getPrefrence(ShopDetailActivity.this).getString("emailId","").toString()+"/rateID/-" ;
        Log.e(TAG,"path url for adding ratings "+ path);

        Ion.with(ShopDetailActivity.this)
                .load(path)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.e(TAG," api complete update"+result.toString());

                    }
                });

    }

    public ArrayList<Doctor> getDoctorsForMedical(String medical, String name, double lat, double longi, String sortBy, String specialization, int start, int count){
        String url = BASE_URL + "api/viewAllDoctorsForMedical";
        url += "/latitude/" + lat;
        url += "/longitude/" + longi;
        if(specialization != null){
            url += "/specialization/" + specialization;
        }
        else{
            url += "/specialization/-";
        }
        url += "/medical/" + medical + "/limitStart/"+ start +"/count/" + count;
        if(sortBy != null){
            url += "/sortBy/" + sortBy;
        }
        if(name != null){
            url += "/searchBy/" + name;
        }
        final ArrayList<Doctor> doctors = new ArrayList<>();
        System.out.println(url);

        Ion.with(getApplicationContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            JsonArray doc = result.getAsJsonArray("result");
                            for (int i = 0; i < doc.size(); i++) {
                                Doctor d = new Doctor();
                                JsonElement obj = doc.get(i);
                                d.setDoctorId(obj.getAsJsonObject().get("DoctorID").toString());
                                d.setDoctorname(obj.getAsJsonObject().get("DoctorName").getAsString());
                                d.setDoctorDetail(obj.getAsJsonObject().get("DoctorDetail").toString());
                                d.setEmail(obj.getAsJsonObject().get("Email").toString());
                                d.setProcedure(obj.getAsJsonObject().get("Procedure").toString());
                                d.setPrice(obj.getAsJsonObject().get("Price").toString());
                                d.setSchedule(obj.getAsJsonObject().get("Schedule").toString());
                                d.setFrequency(obj.getAsJsonObject().get("Frequency").toString());
                                d.setImage(obj.getAsJsonObject().get("Image").getAsString());
                                d.setName(obj.getAsJsonObject().get("Name").toString());
                                d.setAddress(obj.getAsJsonObject().get("Address").toString());
                                d.setPostal(obj.getAsJsonObject().get("Postal").toString());
                                d.setLatlong(obj.getAsJsonObject().get("LatLong").toString());
                                d.setKidsfinityScore(obj.getAsJsonObject().get("KidsfinityScore").getAsInt());
                                d.setDistance(obj.getAsJsonObject().get("Distance").toString());
                                d.setYearOfExp(obj.getAsJsonObject().get("YearExperience").toString());
                                d.setMySchedule(obj.getAsJsonObject().get("MySchedule").toString());
                                d.setRates(obj.getAsJsonObject().get("Rate").toString());
                                d.setTags(obj.getAsJsonObject().get("Tags").toString());
                                ArrayList<Specialization> spe = new ArrayList<Specialization>();
                                if (obj.getAsJsonObject().has("Specialization") && obj.getAsJsonObject().get("Specialization").isJsonArray()) {
                                    JsonArray spec = obj.getAsJsonObject().get("Specialization").getAsJsonArray();
                                    for (int j = 0; j < spec.size(); j++) {
                                        Specialization s = new Specialization();
                                        s.setSpecialization(spec.get(j).getAsJsonObject().get("Specialization").getAsString());
                                        s.setSpecializationId(spec.get(j).getAsJsonObject().get("SpecializationID").getAsString());
                                        spe.add(s);
                                    }
                                }
                                d.setSpecializations(spe);
                                ArrayList<Contact> con = new ArrayList<>();
                                JsonArray cont = obj.getAsJsonObject().get("Contacts").getAsJsonArray();
                                for (int j = 0; j < cont.size(); j++) {
                                    Contact c = new Contact();
                                    c.setContactId(cont.get(j).getAsJsonObject().get("ContactID").getAsLong());
                                    c.setOwnerId(cont.get(j).getAsJsonObject().get("OwnerID").getAsString());
                                    c.setPhoneNo(cont.get(j).getAsJsonObject().get("PhoneNo").getAsString());
                                    con.add(c);
                                }
                                d.setContacts(con);
                                ArrayList<String> images = new ArrayList<String>();

                                if (obj.getAsJsonObject().get("Images").isJsonArray()) {
                                    for (int j = 0; j < obj.getAsJsonObject().get("Images").getAsJsonArray().size(); j++) {
                                        images.add(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageURL").getAsString());
                                    }
                                }
                                d.setImages(images);
                                doctors.add(d);
                            }
                            for (int i = 0; i < doctors.size(); i++) {
                                View doc_layout = LayoutInflater.from(ShopDetailActivity.this).inflate(R.layout.doctor_single, mLinearLayout, false);
                                ImageView img = (ImageView) doc_layout.findViewById(R.id.doctor_img);
                                TextView tv = (TextView) doc_layout.findViewById(R.id.doctor_name_tv);
                                Doctor doct = doctors.get(i);
                                if (doct.getImages().size() > 0) {
                                    Ion.with(img)
                                            .placeholder(R.drawable.doc_dummy)
                                            .error(R.drawable.doc_dummy)
                                            .load(doct.getImages().get(0));
                                }
                                tv.setText(doct.getDoctorname());
                                mLinearLayout.addView(doc_layout);
                            }

                        }
                    }
                });

        return doctors;
    }

    private void getIfBookmarked() {
        Ion.with(this)
                .load(Urls.BASE_URL+"api/viewAllBookmarks/email/" +prefrences.getString("emailId","")+"/class/CLS2")
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
                                String id=obj.getString("ShopID");
                                if(id.equalsIgnoreCase(m.getShopID()))
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
            String url = Urls.BASE_URL + "api/addDeleteReview/reviewee/" + m.getShopID() + "/review/" + reviewText + "/email/" + prefrences.getString("emailId", "") + "/reviewID/-";
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
                                        Toast.makeText(ShopDetailActivity.this, "Review added successfully. Thanks", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ShopDetailActivity.this, "Please Login first to post your reviews", Toast.LENGTH_SHORT).show();
        }
    }

    public void populateReviews() {
        reviews = new ArrayList<>();
        String apipath=Urls.BASE_URL+"api/viewReviewPerReviewee/reviewee/" + m.getShopID();

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
           // startActivity(new Intent(ShopDetailActivity.this, GoKidsHome.class));
            Intent intent= new Intent(ShopDetailActivity.this, GoKidsHome.class);
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
                .title(m.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }
}