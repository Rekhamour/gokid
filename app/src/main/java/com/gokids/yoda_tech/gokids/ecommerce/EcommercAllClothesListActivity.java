package com.gokids.yoda_tech.gokids.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommercProductListAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.model.ClothingBean;
import com.gokids.yoda_tech.gokids.ecommerce.model.ShopifyProductBean;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EcommercAllClothesListActivity extends AppCompatActivity {

    private EcommercProductListAdapter adapter;
    private RecyclerView listView;
    private LinearLayoutManager layoutmanager;
    private ArrayList<ShopifyProductBean> BeanArrayList= new ArrayList<>();
    private String TAG="ecommerce";

    private int mNotifCount;
    private Button notifCount;
    private int h=1;
    private String classId;
    private String productCategory;
    private ArrayList<String> imageslist= new ArrayList<>();
    private ArrayList<String> genderList;
    private ArrayList<String> ageList;
    private ArrayList<ShopifyProductBean> filteredList= new ArrayList<>();
    private ProgressDialog progressDialog;
    private RelativeLayout RLcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecommerce_all_clothes_activity);

        initView();

    }

    private void initView() {
        productCategory=getIntent().getStringExtra("productcategory");
        genderList= getIntent().getStringArrayListExtra(",genderList");
        ageList= getIntent().getStringArrayListExtra("ageList");
        Log.e(TAG,"productcategory"+getIntent().getStringExtra("productcategory"));

        listView= findViewById(R.id.ecommerce_allclothes_list);
        layoutmanager= new LinearLayoutManager(EcommercAllClothesListActivity.this);
        BeanArrayList.clear();
        adapter= new EcommercProductListAdapter(EcommercAllClothesListActivity.this,BeanArrayList);
        listView.setLayoutManager(layoutmanager);
        listView.setAdapter(adapter);
        getSupportActionBar().setTitle("E-Commerce");
        //getAllprodcuts();
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Loading the products");
        progressDialog.show();
        getAllproducts();
    }

    public void setcount(View view)
    {
        h++;
        setNotifCount(h);
    }

   public void  getAllproducts()
    {
        String result="";

            for (int i = 0; i < genderList.size(); i++) {
                result += genderList.get(i) + ",";
            }
        result=result.substring(0,result.length()-1);

        String apipath= Urls.BASE_URL_SHOPIFY+"products.json?product_type="+ result;
        //String apipath= "https://eb3136427dd12c9358a65b99ea2fdbb6:22f18e380dc760fd3797e1cee82f235c@gokids-shop.myshopify.com/admin/products.json?product_type=Chocolate,%20Ice%20Cream";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apipath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response != null) {
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                if (json instanceof JSONObject) {

                                    Log.e(TAG, "shopify response" + json.toString());
                                    progressDialog.dismiss();

                                    JSONArray arrey = ((JSONObject) json).getJSONArray("products");
                                    if (arrey.length() > 0) {
                                        progressDialog.dismiss();
                                        for (int i = 0; i < arrey.length(); i++) {
                                            JSONObject obj = arrey.getJSONObject(i);
                                            String id = obj.getString("id");
                                            String title = obj.getString("title");
                                           // String ImageURL = obj.getString("ImageURL");
                                            String body_html = obj.getString("body_html");
                                            String tags = obj.getString("tags");
                                            JSONArray variants = obj.getJSONArray("variants");
                                            String price = variants.getJSONObject(0).getString("price");
                                            JSONArray images = obj.getJSONArray("images");
                                            String imageurl = images.getJSONObject(0).getString("src");
                                            imageslist.add(imageurl);
                                            ArrayList<String> taglist = new ArrayList<String>(Arrays.asList(tags.split(" , ")));

                                            //for (int j = 0; j < images.length(); j++) {


                                            //}

                                               ShopifyProductBean bean = new ShopifyProductBean();
                                               bean.setBody_html(body_html);
                                               bean.setImgeslist(imageslist);
                                               bean.setTags(taglist);
                                               bean.setProduct_id(id);
                                               bean.setPrice(price);
                                               Log.e("all products", "price" + bean.getPrice());
                                               bean.setTitle(title);
                                               bean.setType("data");
                                               BeanArrayList.add(bean);
                                           }

                                        /*if(!ageList.isEmpty()) {
                                            for (int i = 0; i < BeanArrayList.size(); i++) {

                                                if (compareList(ageList, BeanArrayList.get(i).getTags()) == true) {
                                                    filteredList.add(BeanArrayList.get(i));
                                                }
                                            }
                                        }
                                        else
                                        {
                                            filteredList.addAll(BeanArrayList);
                                        }
                                        Log.e("","filteredlist"+filteredList.size());*/
                                        adapter.notifyDataSetChanged();

                                    }
                                    else {
                                        Toast.makeText(EcommercAllClothesListActivity.this, "No Products", Toast.LENGTH_SHORT).show();
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

        }){
            @Override
            public Map<String, String> getHeaders() {
                //--- Add headers
                Map<String, String> headers = new HashMap<>();
                String loginEncoded = new String(Base64.encode(("eb3136427dd12c9358a65b99ea2fdbb6" + ":" + "22f18e380dc760fd3797e1cee82f235c").getBytes(), Base64.NO_WRAP));
                headers.put("Authorization", " Basic " + loginEncoded);
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ecommerce_listcart_menu, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        View count = menu.findItem(R.id.badge).getActionView();
        notifCount = count.findViewById(R.id.notif_count);
        int finalcount= MySharedPrefrence.getPrefrence(EcommercAllClothesListActivity.this).getInt("cartCount",0);
        notifCount.setText(String.valueOf(mNotifCount));
        RLcart = count.findViewById(R.id.RL_cart);

        RLcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EcommercAllClothesListActivity.this,ChackoutActivity.class);
                startActivity(intent);

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setNotifCount(int count){
        int  cartCount=Integer.parseInt(notifCount.getText().toString());

        mNotifCount = count+cartCount;
        MySharedPrefrence.getPrefrence(EcommercAllClothesListActivity.this).edit().putInt("cartCount",mNotifCount);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.house)
        {
            Intent intent = new Intent(EcommercAllClothesListActivity.this, GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
        }
        else if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }
    public static boolean compareList(ArrayList<String> ls1,ArrayList<String> ls2){
        return ls1.toString().contentEquals(ls2.toString());
    }
}
