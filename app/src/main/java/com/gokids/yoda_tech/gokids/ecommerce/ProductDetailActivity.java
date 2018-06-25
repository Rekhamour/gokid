package com.gokids.yoda_tech.gokids.ecommerce;

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
import android.support.v4.view.MenuItemCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.gokids.yoda_tech.gokids.eat.model.MainBean;
import com.gokids.yoda_tech.gokids.eat.model.Review;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceGridAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.model.ShopifyProductBean;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.shop.activity.ShopDetailActivity;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static com.gokids.yoda_tech.gokids.ecommerce.ChackoutActivity.addedItemList;

public class ProductDetailActivity extends AppCompatActivity {

    private static final int MY_CALL_PERMISSION = 123;

    RecyclerView reviews_list;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviews;
    Button write_review;
    ShopifyProductBean m;
    String reviewText = "";
    String phoneNo = "";
    ImageButton bookmark,chat,direction;
    ScrollView scrollView;
    RecyclerView tagList;
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
    private float ratings;
    private RatingBar addrating;
    private TextView score;
    private TextView price;
    private Spinner spinnerQnt;
    private ArrayList<String> list;
    private Button notifCount;
    private int h=0;
    private int mNotifCount;
    private ImageView thumbs_up;
    private TextView count_up;
    private ImageView thumb_down;
    private TextView count_down;
    private String downcount;
    private String upcount;
    private RelativeLayout RLcart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_product_details);
        Intent intent = getIntent();
        prefrences= getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        emailid=prefrences.getString("emailId","");
        m= (ShopifyProductBean) getIntent().getSerializableExtra("productdetails");
        latlon= Utils.getLatLong(ProductDetailActivity.this);
        Toolbar toolbar = findViewById(R.id.product_detail_toolbar);
        reviews_list = findViewById(R.id.product_detail_reviews_list);
        write_review = findViewById(R.id.product_detail_write_review);
        addrating = findViewById(R.id.product_detail_ratings);
        score = findViewById(R.id.score);
        spinnerQnt = findViewById(R.id.product_quantity);
        price = findViewById(R.id.product_price);
        thumbs_up = findViewById(R.id.thumbs_up_product);
        count_up = findViewById(R.id.count_up_product);
        count_down = findViewById(R.id.count_down_product);
        thumb_down = findViewById(R.id.thumbs_down_product);

        detail = findViewById(R.id.product_detail_description_tv);
        tagList = findViewById(R.id.product_detail_tags_list);
        if(m.getTags().size()<=0)
        {
            tagList.setVisibility(View.GONE);
        }
        //list = new ArrayList<String>(Arrays.asList(m.getTags().split(" , ")));
        Log.e("tags list size","" +m.getTags().size()+" " +m.getTags().toString());
        tagList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        tagList.setAdapter(new EcommerceGridAdapter(ProductDetailActivity.this,m.getTags()));
        mViewpager = findViewById(R.id.product_detail_image_pager);
        mViewpager.setAdapter(new ImageSLiderAdapter(ProductDetailActivity.this,m.getImgeslist()));
        indicator = findViewById(R.id.product_detail_indicator);
        indicator.setViewPager(mViewpager);
       int indicatorcount = m.getImgeslist().size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                if (currentpage == m.getImgeslist().size()) {
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
        //setSupportActionBar(toolbar);
        getRatings();
        getAllratingsthumbsdown();

        getSupportActionBar().setTitle(m.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        detail.setText(m.getBody_html());
        
        ArrayAdapter<CharSequence> Ageadapter = ArrayAdapter.createFromResource(this,R.array.agearray,R.layout.support_simple_spinner_dropdown_item);
        spinnerQnt.setAdapter(Ageadapter);
        spinnerQnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                   String  age = parent.getItemAtPosition(position).toString();
                   spinnerQnt.setSelected(true);
                    spinnerQnt.setSelection(position,true);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getAllratingsthumbsdown();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reviews_list.setLayoutManager(layoutManager);
        reviews = new ArrayList<>();

        thumb_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // http://52.77.82.210/api/setRate/email/manjularavula69@gmail.com/class/775076184153/categoryItem/775076184153/rate/1/city/CITY1
                String thumbs_down=Urls.BASE_URL+"api/setRate/email/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("emailId","")+"/class/"+m.getProduct_id()+"/categoryItem/"+m.getProduct_id()+"/rate/0"+"/city/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("current_city","");
               Log.e(TAG,"thumbs_down"+thumbs_down);
                Ion.with(ProductDetailActivity.this)
                        .load(thumbs_down)
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
                                    Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Log.e(TAG," exception thumb down "+ e.getMessage());

                                }

                            }
                        });

            }
        });

        thumbs_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thumbs_up=Urls.BASE_URL+"api/setRate/email/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("emailId","")+"/class/"+m.getProduct_id()+"/categoryItem/"+m.getProduct_id()+"/rate/1"+"/city/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("current_city","");
                Log.e(TAG,"thumbs_up_url"+thumbs_up);
                Ion.with(ProductDetailActivity.this)
                        .load(thumbs_up)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                if (e == null) {
                                    Log.e(TAG, " thumbs_ up result" + result.toString());
                                    String status = result.get("status").toString();
                                    String message = result.get("message").toString();

                                    getAllratingsthumbsdown();

                                    Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Log.e(TAG," exception"+ e.getMessage());
                                }



                            }
                        });

            }
        });
        populateReviews();
        getAllratingsthumbsdown();
        price.setText("$"+m.getPrice());
        write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ProductDetailActivity.this);
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
    }

    private void getRatings() {
      //  String ratings= String.valueOf(selectedratings);
        String path= Urls.BASE_URL+"api/viewProductRatingPerRatee/ratee/" + m.getProduct_id()+"/city/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("current_city","");
        Log.e(TAG,"path url for viewing ratings "+ path);
        Ion.with(ProductDetailActivity.this)
                .load(path)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                       // JsonObject obj=result.get(0).getAsJsonObject();
                      //  String rate=obj.get("Rate").toString();
                       // Log.e(TAG," get rating"+ rate);
                        //if(!rate.equalsIgnoreCase("null"))
                            //addrating.setRating(Float.parseFloat(rate));


                    }
                });

    }

    private void updateratings(float selectedratings) {
        String ratings= String.valueOf(selectedratings);
        String path= Urls.BASE_URL+"api/addProductRating/ratee/" + m.getProduct_id() + "/rating/" +ratings +"/email/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("emailId","").toString()+"/city/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("current_city","");
        Log.e(TAG,"path url for adding ratings "+ path);

        Ion.with(ProductDetailActivity.this)
                .load(path)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.e(TAG," api complete update"+result.toString());

                    }
                });

    }






    private void postReview(String reviewText) {
        if (!prefrences.getString("emailId", "").toString().trim().isEmpty()) {
            //http://52.77.82.210/api/addProductReview/reviewee/775076184153/review/Test/email/manjularavula69@gmail.com/city/City1
            String url = Urls.BASE_URL + "api/addProductReview/reviewee/" + m.getProduct_id() + "/review/" + reviewText + "/email/" + prefrences.getString("emailId", "") +"/city/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("current_city","");
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
                                        Toast.makeText(ProductDetailActivity.this, "Review added successfully. Thanks", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ProductDetailActivity.this, "Please Login first to post your reviews", Toast.LENGTH_SHORT).show();
        }
    }

    public void populateReviews() {
        reviews = new ArrayList<>();

        String apipath=Urls.BASE_URL+"api/viewReviewPerProduct/reviewee/" + m.getProduct_id()+"/city/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("current_city","");
        Log.e(TAG,"apipath for getting reviews"+apipath);
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
                                                r.setReviewer(obj.getString("ReviewDate"));
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
                                                r.setReviewer(obj.getString("ReviewDate"));
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
    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if(item.getItemId()==R.id.house)
        {
           // startActivity(new Intent(ShopDetailActivity.this, GoKidsHome.class));
            Intent intent= new Intent(ProductDetailActivity.this, GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
            finish();



        }
        return super.onOptionsItemSelected(item);
    }

    public void submitRating(View view)
    {
        updateratings(ratings);

    }

    private void getAllratingsthumbsdown() {
        String getthumbsdown=Urls.BASE_URL+"api/viewAllRatings/email/"+MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("emailId","")+"/class/CLS1/categoryItem/"+m.getProduct_id()+"/city/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("current_city","");
       // http://52.77.82.210/api/viewAllRatings/email/manjularavula69@gmail.com/class/CLS1/categoryItem/775076184153/city/City1
       // String getthumbsdown="api/viewAllRatings/email/"+MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("emailId","") +"/class/CLS1/categoryItem/"+m.getProduct_id()+"/city/"+ MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getString("current_city","");
        Log.e(TAG,"get thumbs urls" + getthumbsdown);
        Ion.with(ProductDetailActivity.this)
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
    public void addtoCart(View view)
    {
        setNotifCount(Integer.parseInt(spinnerQnt.getSelectedItem().toString()));
        m.setProductQuantity(Integer.parseInt(spinnerQnt.getSelectedItem().toString()));
        addedItemList.add(m);
        getAlertForcontinuencheckout();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_details_menu, menu);
        MenuItem item = menu.findItem(R.id.badge);

        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        View count = menu.findItem(R.id.badge).getActionView();
        notifCount = count.findViewById(R.id.notif_count);
        int finalcount= MySharedPrefrence.getPrefrence(ProductDetailActivity.this).getInt("cartCount",0);
        Log.e("cart","artcount"+finalcount);
        notifCount.setText(String.valueOf(mNotifCount));
        RLcart = count.findViewById(R.id.RL_cart);

        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this,ChackoutActivity.class);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }



    private void setNotifCount(int count){
       int  cartCount=Integer.parseInt(notifCount.getText().toString());

        mNotifCount = count+cartCount;
        MySharedPrefrence.getPrefrence(ProductDetailActivity.this).edit().putInt("cartCount",mNotifCount);
        invalidateOptionsMenu();
    }
    public void setcount(View view)
    {
        h++;
        setNotifCount(h);
    }

   public void  getAlertForcontinuencheckout()
   {
       AlertDialog.Builder alert = new AlertDialog.Builder(ProductDetailActivity.this);
       LayoutInflater inflater = LayoutInflater.from(ProductDetailActivity.this);
       View alertLayout = inflater.inflate(R.layout.checkout_continue_dialog, null);
       // this is set the view from XML inside AlertDialog
       alert.setView(alertLayout);
       // disallow cancel of AlertDialog on click of back button and outside touch
       alert.setCancelable(false);

       final AlertDialog dialog = alert.create();
       dialog.show();

       final Button loginclick = alertLayout.findViewById(R.id.checkoutclick);
       final Button continueclcik = alertLayout.findViewById(R.id.continueshoppingclick);
       loginclick.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Toast.makeText(context, "login clicked", Toast.LENGTH_SHORT).show();
               Intent intent= new Intent(ProductDetailActivity.this, ChackoutActivity.class);
               startActivity(intent);
               dialog.dismiss();


           }
       });
       continueclcik.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();

           }
       });

   }
}