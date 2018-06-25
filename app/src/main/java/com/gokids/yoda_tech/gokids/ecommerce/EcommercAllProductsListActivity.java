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
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceListAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.model.EcommercProductBean;
import com.gokids.yoda_tech.gokids.ecommerce.model.ShopifyProductBean;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.shop.activity.ShopDetailActivity;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shopify.buy3.Storefront;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class EcommercAllProductsListActivity extends AppCompatActivity {

    private EcommercProductListAdapter adapter;
    private RecyclerView listView;
    private LinearLayoutManager layoutmanager;
    private ArrayList<ShopifyProductBean> BeanArrayList= new ArrayList<>();
    private String TAG="ecommerce";
    final String jsonString="{\n" +
            "  \"status\": \"200\",\n" +
            "  \"message\": \"Product Category: Success!\",\n" +
            "  \"result\": [\n" +
            "    {\n" +
            "      \"ProductCategoryID\": \"PCAT1\",\n" +
            "      \"ProductCategory\": \"Milk and Milk Powder\",\n" +
            "      \"ImageURL\": \"https://s3-ap-southeast-1.amazonaws.com/kisimages/ProductCategory/Milk\",\n" +
            "      \"ProductClassID\": \"PCLS1\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"ProductCategoryID\": \"PCAT2\",\n" +
            "      \"ProductCategory\": \"Butter, Cheese and Yogurt\",\n" +
            "      \"ImageURL\": \"https://s3-ap-southeast-1.amazonaws.com/kisimages/ProductCategory/BCY\",\n" +
            "      \"ProductClassID\": \"PCLS1\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"ProductCategoryID\": \"PCAT3\",\n" +
            "      \"ProductCategory\": \"Chocolate, Ice Cream\",\n" +
            "      \"ImageURL\": \"https://s3-ap-southeast-1.amazonaws.com/kisimages/ProductCategory/Sweets\",\n" +
            "      \"ProductClassID\": \"PCLS1\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"ProductCategoryID\": \"PCAT4\",\n" +
            "      \"ProductCategory\": \"Beverage\",\n" +
            "      \"ImageURL\": \"https://s3-ap-southeast-1.amazonaws.com/kisimages/ProductCategory/Beverage\",\n" +
            "      \"ProductClassID\": \"PCLS1\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"ProductCategoryID\": \"PCAT5\",\n" +
            "      \"ProductCategory\": \"Snacks\",\n" +
            "      \"ImageURL\": \"https://s3-ap-southeast-1.amazonaws.com/kisimages/ProductCategory/Snacks\",\n" +
            "      \"ProductClassID\": \"PCLS1\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"ProductCategoryID\": \"PCAT6\",\n" +
            "      \"ProductCategory\": \"Baby - Boy\",\n" +
            "      \"ImageURL\": \"\",\n" +
            "      \"ProductClassID\": \"PCLS2\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"ProductCategoryID\": \"PCAT7\",\n" +
            "      \"ProductCategory\": \"Baby - Girl\",\n" +
            "      \"ImageURL\": \"\",\n" +
            "      \"ProductClassID\": \"PCLS2\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"ProductCategoryID\": \"PCAT8\",\n" +
            "      \"ProductCategory\": \"Kid - Boy\",\n" +
            "      \"ImageURL\": \"\",\n" +
            "      \"ProductClassID\": \"PCLS2\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"ProductCategoryID\": \"PCAT9\",\n" +
            "      \"ProductCategory\": \"Kid - Girl\",\n" +
            "      \"ImageURL\": \"\",\n" +
            "      \"ProductClassID\": \"\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    private int mNotifCount;
    private Button notifCount;
    private int h=1;
    private String classId;
    private String productCategory;
    private ArrayList<String> imageslist= new ArrayList<>();
    private ProgressDialog progressDialog;
    private RelativeLayout RLcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecommerce_allproduct_list_activity);

        initView();

    }

    private void initView() {
        productCategory=getIntent().getStringExtra("productcategory");
        Log.e(TAG,"productcategory"+getIntent().getStringExtra("productcategory"));

        listView= findViewById(R.id.ecommerce_allproduct_list);
        layoutmanager= new LinearLayoutManager(EcommercAllProductsListActivity.this);
        BeanArrayList.clear();
        adapter= new EcommercProductListAdapter(EcommercAllProductsListActivity.this,BeanArrayList);
        listView.setLayoutManager(layoutmanager);
        listView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading products");
        progressDialog.show();


        getSupportActionBar().setTitle(productCategory);
        //getAllprodcuts();
        getAllproducts();
    }

    public void setcount(View view)
    {
        h++;
        setNotifCount(h);
    }


    public void  getAllproducts()
    {
        String apipath= Urls.BASE_URL_SHOPIFY+"products.json?product_type="+ Uri.encode(productCategory);
        Log.e(TAG,"apipath shopify"+apipath);
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
                                            //ArrayList<String> taglist = new ArrayList<String>(Arrays.asList(tags.split(",")));

                                            //List taglist = new ArrayList();
                                            //taglist = Arrays.asList(tags.trim().split(" , "));
                                            //List<String> taglist = Arrays.asList(tags.split("[," +
                                                 //   "" +
                                                //    "]+"));
                                            List<String> taglist = Arrays.asList(tags.split(", "));
                                            //for (int j = 0; j < images.length(); j++) {


                                            //}

                                            ShopifyProductBean bean = new ShopifyProductBean();
                                            bean.setBody_html(body_html);
                                            bean.setImgeslist(imageslist);
                                            bean.setTags(taglist);
                                            bean.setProduct_id(id);
                                            bean.setPrice(price);
                                            //Log.e("all products","price"+taglist.get(0)+" "+taglist.get(1));
                                            bean.setTitle(title);
                                            bean.setType("data");
                                            BeanArrayList.add(bean);
                                        }
                                        adapter.notifyDataSetChanged();
                                        //adapter.notifyDataChanged();

                                    }
                                    else {
                                        Toast.makeText(EcommercAllProductsListActivity.this, "No Products", Toast.LENGTH_SHORT).show();
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
       int finalcount= MySharedPrefrence.getPrefrence(EcommercAllProductsListActivity.this).getInt("cartCount",0);

        notifCount.setText(String.valueOf(mNotifCount));
        RLcart = count.findViewById(R.id.RL_cart);

        RLcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EcommercAllProductsListActivity.this,ChackoutActivity.class);
                startActivity(intent);

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setNotifCount(int count){
        int  cartCount=Integer.parseInt(notifCount.getText().toString());

        mNotifCount = count+cartCount;
        MySharedPrefrence.getPrefrence(EcommercAllProductsListActivity.this).edit().putInt("cartCount",mNotifCount);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.house)
        {
            Intent intent = new Intent(EcommercAllProductsListActivity.this, GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);
        }
        else if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }
}
