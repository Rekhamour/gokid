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

import me.relex.circleindicator.CircleIndicator;

public class MedicalDetail extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_CALL_PERMISSION = 123;
    private static final String BASE_URL = "http://52.77.82.210/";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        prefrence= getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        address = (TextView) findViewById(R.id.address_tv);
        email = (TextView) findViewById(R.id.email_tv);
        website = (TextView) findViewById(R.id.website_tv);
        timing = (TextView) findViewById(R.id.timings_tv);
        specialization = (TextView) findViewById(R.id.specialization_tv);
        distance = (TextView) findViewById(R.id.distance_tv);
        //img = (ImageView) findViewById(R.id.medical_detail_image);
        reviews_list = (RecyclerView) findViewById(R.id.reviews_list);
        bookmark = (ImageButton) findViewById(R.id.bookmark);
        chat = (ImageButton) findViewById(R.id.chat);
        direction = (ImageButton) findViewById(R.id.direction);
        scrollView = (ScrollView) findViewById(R.id.scrollVie);
        mLinearLayout = (LinearLayout) findViewById(R.id.doctors_list);
        Intent intent = getIntent();
        m = (MainBean) intent.getSerializableExtra("medical_data");

        mViewpager = (ViewPager) findViewById(R.id.image_pager_md);
        mViewpager.setAdapter(new ImageSLiderAdapter(MedicalDetail.this,m.getImages()));
        indicator = (CircleIndicator) findViewById(R.id.md_indicator);
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
        getDoctorsForMedical(m.getMedicalID(),null,-91,-181,null,null,0,100);
/*
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bookmarkFlag){
                    bookmark.setBackgroundResource(R.drawable.btn_badge_red_3x);
                    bookmarkFlag = true;
                }
                else{
                    bookmark.setBackgroundResource(R.drawable.btn_badge_3x);
                    bookmarkFlag = false;
                }
            }
        });*/
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefrence.getString("emailId", "").trim().isEmpty()) {
                    if (!bookmarkFlag) {
                        bookmarkFlag = true;
                        bookmark.setBackgroundResource(R.drawable.btn_badge_red_3x);
                        String url = Urls.BASE_URL + "api/setBookMark/email/" + MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("emailId", "") + "/class/CLS4/categoryItem/" + m.getMedicalID() + "/bookmark/1";
                        Log.e(TAG, "url" + url);
                        String urls = "http://52.77.82.210/api/setBookMark/email/shwetasirsa93556@gmail.com/class/CLS1/categoryItem/R367/bookmark/1";
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
                        String url = Urls.BASE_URL + "api/setBookMark/email/" + MySharedPrefrence.getPrefrence(MedicalDetail.this).getString("emailId", "") + "/class/CLS4/categoryItem/" + m.getMedicalID() + "/bookmark/-";
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
             /*   AlertDialog.Builder builder = new AlertDialog.Builder(MedicalDetail.this);
                //  builder.setTitle("Write Review");

                CardView reviewLL = (CardView)getLayoutInflater().inflate(R.layout.review_layout,null);
                final EditText input = (EditText)reviewLL.findViewById(R.id.review_text);
                final Button submit = (Button) reviewLL.findViewById(R.id.btn_submit_review);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postReview(input.getText().toString());

                    }
                });
                // Set up the input
                //final EditText input = new EditText(FoodDetailActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(reviewLL);
                Dialog alertDialog = new Dialog(MedicalDetail.this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(R.layout.review_layout);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();*/
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
                        if(e==null) {
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
                                View doc_layout = LayoutInflater.from(MedicalDetail.this).inflate(R.layout.doctor_single, mLinearLayout, false);
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
                        else
                        {
                            Toast.makeText(MedicalDetail.this, getResources().getString(R.string.oops), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return doctors;
    }

    private void getIfBookmarked() {
        Ion.with(this)
                .load("http://52.77.82.210/api/viewAllBookmarks/email/"+prefrence.getString("emailId","")+"/class/CLS4")
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
            String url = "http://52.77.82.210/api/addDeleteReview/reviewee/" + m.getMedicalID() + "/review/" + reviewText + "/email/" + prefrence.getString("emailId", "") + "/reviewID/-";

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
                .load("http://52.77.82.210/api/viewReviewPerReviewee/reviewee/" + m.getMedicalID())
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
                .title(m.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }
}