package com.gokids.yoda_tech.gokids.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokids.BuildConfig;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommercCartListAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.model.ShopifyProductBean;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.Storefront;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChackoutActivity extends AppCompatActivity {

    private RecyclerView itemsList;
    List<Storefront.Product> products = new ArrayList<>();
    List<Storefront.Collection> collections= new ArrayList<>();
    private LinearLayoutManager lm;
    private EcommercCartListAdapter adapter;
    public static ArrayList<ShopifyProductBean> addedItemList = new ArrayList<>();
    private Button editButton;
    private Button editButton2;
    private String TAG = getClass().getName();
    private Button checkoutBtn;
    private String weburl;
    private SharedPreferences prefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chackout);
        initView();
    }

    private void initView() {
        itemsList = findViewById(R.id.ecommerce_addeditems_list);
        checkoutBtn = findViewById(R.id.checkoutBtn);
        prefrences= getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hendl action here eg
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //checkoutFinal();
                checkFinalout();

            }
        });
        lm = new LinearLayoutManager(this);
        adapter = new EcommercCartListAdapter(ChackoutActivity.this, addedItemList);
        itemsList.setLayoutManager(lm);
        itemsList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_edit_menu, menu);
        MenuItem item = menu.findItem(R.id.edit);

        MenuItemCompat.setActionView(item, R.layout.action_layout_edit);
        View count = menu.findItem(R.id.edit).getActionView();
        editButton = count.findViewById(R.id.edit_cart);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChackoutActivity.this, EditChackoutActivity.class);
                startActivity(intent);
                finish();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkoutFinal() {

        GraphClient graphClient = GraphClient.builder(this)
                .shopDomain(BuildConfig.domain)
                .accessToken("673b6c2afb795a11f3f3a5c045d1051a")
                .httpCache(new File(getApplicationContext().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .shop(shopQuery -> shopQuery
                        .collections(args -> args.first(10), collectionConnectionQuery -> collectionConnectionQuery
                                .edges(collectionEdgeQuery -> collectionEdgeQuery
                                        .node(collectionQuery -> collectionQuery
                                                .title()
                                                .products(args -> args.first(10), productConnectionQuery -> productConnectionQuery
                                                        .edges(produtEdgeQuery -> produtEdgeQuery
                                                                .node(new Storefront.ProductQueryDefinition() {
                                                                    @Override
                                                                    public void define(Storefront.ProductQuery productQuery) {
                                                                        productQuery.title()
                                                                                .productType()
                                                                                .description();
                                                                    }
                                                                })

                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        graphClient.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

            @Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                Log.e("response","response"+response.toString());
                collections = new ArrayList<>();
                for (Storefront.CollectionEdge collectionEdge : response.data().getShop().getCollections().getEdges()) {
                    collections.add(collectionEdge.getNode());
                   // Log.e("response","list size"+response.toString());

                     products = new ArrayList<>();
                    for (Storefront.ProductEdge productEdge : collectionEdge.getNode().getProducts().getEdges()) {
                        products.add(productEdge.getNode());

                    }
                }
                Log.e("response","list size"+products.size());
                Log.e("response","collection list size"+collections.size());

            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                Log.e("error","error"+error.toString());
            }

        });
   /*     Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput()
                .setLineItemsInput(Input.value(Arrays.asList(

                        new Storefront.CheckoutLineItemInput (5,new ID("ODcwMDMwOTczMzQ2NQ==")),
                        new Storefront.CheckoutLineItemInput( 3,new ID("ODcwMDMyNTQyOTMzNw=="))
                )));

        Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
                .checkoutCreate(input, createPayloadQuery -> createPayloadQuery
                        .checkout(checkoutQuery -> checkoutQuery
                                .webUrl()
                        )
                        .userErrors(userErrorQuery -> userErrorQuery
                                .field()
                                .message()
                        )
                )
        );

        graphClient.mutateGraph(query).enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {
                Log.e(TAG,"response"+response.errors().toString());
                if (!response.data().getCheckoutCreate().getUserErrors().isEmpty()) {
                    // handle user friendly errors
                } else {
                    String checkoutId = response.data().getCheckoutCreate().getCheckout().getId().toString();
                    String checkoutWebUrl = response.data().getCheckoutCreate().getCheckout().getWebUrl();
                    Log.e("checkout","checkoutid"+checkoutId);
                    Log.e("checkout","checkoutWebUrl"+checkoutWebUrl);
                }
            }

            @Override public void onFailure(@NonNull GraphError error) {
                Log.e("checkout","error"+error.toString());

                // handle errors
            }
        });*/

    }
    public void checkFinalout1()
    {
        String createCheckOutUrl= Urls.BASE_URL_SHOPIFY+"oauth/access_token";
       // https://{shop}.myshopify.com/admin/oauth/access_token
       /*// getJsonobjecttopost();
        Log.e("createCheckOutUrl","createCheckOutUrl"+createCheckOutUrl);
        Ion.with(ChackoutActivity.this)
                .load(createCheckOutUrl)
                .setJsonObjectBody(getJsonobjecttopost())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null)
                        {
                            Log.e("checkout","result" +result.toString());
                        }

                    }
                });*/

    }
    public void  checkFinalout()
    {
        JsonObjectRequest jsonObjectRequest=null;
        try {
            JSONObject objTopass= new JSONObject(getJsonobjecttopost().toString());
            Log.e("objTopass","objTopass"+objTopass.toString());

            String createCheckOutUrl= Urls.BASE_URL_SHOPIFY+"checkouts.json";
            // getJsonobjecttopost();
            Log.e("createCheckOutUrl","createCheckOutUrl"+createCheckOutUrl);
             jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    createCheckOutUrl,
                    objTopass,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG,"response"+response.toString());
                           /// try {
                              // String weburl="https:\/\/checkout.shopify.com\/10890068\/checkouts\/f9604c3f13b35c7ba36932436310ef09";
                             ///  String weburl= "\"https:\\/\\/checkout.shopify.com\\/10890068\\/checkouts\\/f9604c3f13b35c7ba36932436310ef09\""  ;
                            // response.getString("web_url");
                               // Log.e(TAG,"weburl"+weburl);

                               // Intent intent= new Intent(Intent.ACTION_VIEW);
                               // intent.setData(Uri.parse("\"https:\\/\\/checkout.shopify.com\\/10890068\\/checkouts\\/f9604c3f13b35c7ba36932436310ef09\""));
                                //startActivity(intent)
                            //String newurl="https://checkout.shopify.com/10890068/checkouts/f9604c3f13b35c7ba36932436310ef09";
                           // newurl.replaceAll("[\\-\\+\\.\\^:,]","");
                          String h=  weburl.replaceAll("\\\\+","");
                          String newlink=  h.replaceAll("\\\\+","");
                          String newnewlink=  newlink.replaceAll("\\\\+","");
                          String finallink=  newnewlink.replaceAll("\"","");
                            Log.e(TAG,"weburl"+finallink);

                            //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(finallink));
                           // startActivity(i);
                            Intent i= new Intent(ChackoutActivity.this,ShopifyCheckoutActivity.class);
                            i.putExtra("weburl",finallink);
                            startActivity(i);

                           /* } catch (JSONException e) {
                                e.printStackTrace();
                            }*/

                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.e("error","error"+error.getMessage());

                        }

                    }


            )
            {
                @Override
                public Map<String, String> getHeaders() {
                    //--- Add headers
                    Map<String, String> headers = new HashMap<>();
                    String loginEncoded = new String(Base64.encode(("eb3136427dd12c9358a65b99ea2fdbb6" + ":" + "22f18e380dc760fd3797e1cee82f235c").getBytes(), Base64.NO_WRAP));
                    Log.e("loginEncoded","loginEncoded"+loginEncoded);

                    headers.put("Authorization", " Basic " + loginEncoded);
                    headers.put("Content-Type", "application/json");
                   // headers.put("X-Shopify-Access-Token", "673b6c2afb795a11f3f3a5c045d1051a");
                    return headers;
                }

            };

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private JsonObject getJsonobjecttopost() {

        JsonObject checkoutobj= new JsonObject();
        JsonArray productArray= new JsonArray();
        JsonObject addressobj= new JsonObject();

        for(int i=0;i<addedItemList.size();i++) {
            JsonObject productobj = new JsonObject();
             ShopifyProductBean bean= addedItemList.get(i);
            productobj.addProperty("variant_id",bean.getProduct_id() );
            productobj.addProperty("quantity", bean.getProductQuantity());
            productArray.add(productobj);
        }
        //checkoutobj.addProperty("email", MySharedPrefrence.getPrefrence(ChackoutActivity.this).getString("emailId",""));
        checkoutobj.add("line_items",productArray);
       // checkoutobj.add("shipping_address",addressobj);

        addressobj.addProperty("first_name",prefrences.getString("userName",""));
        addressobj.addProperty("last_name","ravula");
        addressobj.addProperty("address1","h.no-137/5 ");
        addressobj.addProperty("city","gurgaon");
        addressobj.addProperty("province_code","IN");
        addressobj.addProperty("country_code","IN");
        addressobj.addProperty("phone","9996633927");
        addressobj.addProperty("zip","122001");
        JsonObject parentObj= new JsonObject();
        parentObj.add("checkout",checkoutobj);
        return parentObj;


    }
}
