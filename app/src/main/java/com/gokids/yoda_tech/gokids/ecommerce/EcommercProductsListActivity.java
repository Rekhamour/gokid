package com.gokids.yoda_tech.gokids.ecommerce;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceListAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.model.EcommercProductBean;
import com.gokids.yoda_tech.gokids.entertainment.activity.EntertainmentCategoryBean;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.settings.model.Allergy;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class EcommercProductsListActivity extends AppCompatActivity {

    private EcommerceListAdapter adapter;
    private RecyclerView listView;
    private LinearLayoutManager layoutmanager;
    private ArrayList<EcommercProductBean> BeanArrayList= new ArrayList<>();
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
    private ArrayList<Allergy> list;
    private RelativeLayout RLcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecommerc_products_list);
        list=(ArrayList<Allergy>) getIntent().getSerializableExtra("selectedAllergi");

        initView();
    }

    private void initView() {
        classId=getIntent().getStringExtra("classid");
        Log.e(TAG,"classid"+getIntent().getStringExtra("classid"));

        listView= findViewById(R.id.ecommerce_product_list);
        layoutmanager= new LinearLayoutManager(EcommercProductsListActivity.this);
        BeanArrayList.clear();
        adapter= new EcommerceListAdapter(EcommercProductsListActivity.this,BeanArrayList);
        listView.setLayoutManager(layoutmanager);
        listView.setAdapter(adapter);
        getAllprodcuts();
        //getAllproducts();
    }

    public void setcount(View view)
    {
        h++;
        setNotifCount(h);
    }
    private void getAllprodcuts() {
        Log.e(TAG,"classid 5 "+classId);

        //String All_sucategories= Urls.BASE_URL+"api/viewAllProductCategoryPerClass/prodClass/PCLS1/prodType/-";
        String All_sucategories= Urls.BASE_URL+"api/viewAllProductCategory";

        Log.e(TAG," url" +All_sucategories);
        Ion.with(EcommercProductsListActivity.this)
                .load(All_sucategories)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null) {
                            try {
                                Object json = new JSONTokener(result.toString()).nextValue();
                                if (json instanceof JSONObject) {

                                    Log.e(TAG, "obj out if" + json.toString());

                                    JSONArray arrey = ((JSONObject) json).getJSONArray("result");
                                    if (arrey.length() > 0) {
                                        for (int i = 0; i < arrey.length(); i++) {
                                            JSONObject obj = arrey.getJSONObject(i);
                                            String ProductCategoryID = obj.getString("ProductCategoryID");
                                            String ProductCategory = obj.getString("ProductCategory");
                                            String ImageURL = obj.getString("ImageURL");
                                            String ProductClassID = obj.getString("ProductClassID");
                                            EcommercProductBean bean = new EcommercProductBean();
                                            bean.setProductCategory(ProductCategory);
                                            bean.setProductCategoryID(ProductCategoryID);
                                            bean.setImageURL(ImageURL);
                                            bean.setProductClassID(ProductClassID);
                                            BeanArrayList.add(bean);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }

                                }


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        else
                        {
                            e.printStackTrace();
                        }

                    }
                });
    }
   public void  getAllproducts()
    {
        String apipath= "http://52.77.82.210/api/viewAllProductCategory";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apipath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response != null) {
                            try {
                                Object json = new JSONTokener(jsonString).nextValue();
                                if (json instanceof JSONObject)
                                {

                                    Log.e(TAG,"obj out if"+json.toString());

                                    JSONArray arrey=((JSONObject) json).getJSONArray("result");
                                    if(arrey.length()>0) {
                                        for (int i = 0; i < arrey.length(); i++) {
                                            JSONObject obj = arrey.getJSONObject(i);
                                            String ProductCategoryID = obj.getString("ProductCategoryID");
                                            String ProductCategory = obj.getString("ProductCategory");
                                            String ImageURL = obj.getString("ImageURL");
                                            String ProductClassID = obj.getString("ProductClassID");
                                            EcommercProductBean bean= new EcommercProductBean();
                                            bean.setProductCategory(ProductCategory);
                                            bean.setProductCategoryID(ProductCategoryID);
                                            bean.setImageURL(ImageURL);
                                            bean.setProductClassID(ProductClassID);
                                            BeanArrayList.add(bean);
                                        }
                                        adapter.notifyDataSetChanged();
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ecommerce_listcart_menu, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        View count = menu.findItem(R.id.badge).getActionView();
        notifCount = count.findViewById(R.id.notif_count);
        int finalcount= MySharedPrefrence.getPrefrence(EcommercProductsListActivity.this).getInt("cartCount",0);
        notifCount.setText(String.valueOf(mNotifCount));
        RLcart = count.findViewById(R.id.RL_cart);

        RLcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EcommercProductsListActivity.this,ChackoutActivity.class);
                startActivity(intent);

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setNotifCount(int count){
        int  cartCount=Integer.parseInt(notifCount.getText().toString());

        mNotifCount = count+cartCount;
        MySharedPrefrence.getPrefrence(EcommercProductsListActivity.this).edit().putInt("cartCount",mNotifCount);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.house)
        {
            Intent intent = new Intent(EcommercProductsListActivity.this, GoKidsHome.class);
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
