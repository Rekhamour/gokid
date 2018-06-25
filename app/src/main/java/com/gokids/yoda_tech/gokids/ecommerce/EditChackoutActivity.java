package com.gokids.yoda_tech.gokids.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommercCartListAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommercEditCartListAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.model.ShopifyProductBean;

import java.util.ArrayList;

public class EditChackoutActivity extends AppCompatActivity {

    private RecyclerView itemsList;
    private LinearLayoutManager lm;
    private EcommercEditCartListAdapter adapter;
    public static ArrayList<ShopifyProductBean> addedItemList= new ArrayList<>();
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart);
        initView();
    }

    private void initView() {
        itemsList= findViewById(R.id.ecommerce_Edititems_list);
        lm= new LinearLayoutManager(this);
        adapter= new EcommercEditCartListAdapter(EditChackoutActivity.this,ChackoutActivity.addedItemList);
        itemsList.setLayoutManager(lm);
        itemsList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      if(item.getItemId()==android.R.id.home)
        {
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
        editButton.setText("Done");
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=  new Intent(EditChackoutActivity.this,ChackoutActivity.class);
                startActivity(intent);
            }
        });
        //notifCount.setText(String.valueOf(mNotifCount));
        return super.onCreateOptionsMenu(menu);
    }
}
