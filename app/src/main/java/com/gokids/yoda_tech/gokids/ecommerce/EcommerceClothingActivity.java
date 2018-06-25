package com.gokids.yoda_tech.gokids.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.bookmark.activity.AllBookmarksActivity;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceClothingAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceGridAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceSLiderAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.model.ClothingBean;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.home.util.RoundedImageView;
import com.gokids.yoda_tech.gokids.referfriend.activity.ReferFriend;
import com.gokids.yoda_tech.gokids.settings.activity.AddKidsActivity;
import com.gokids.yoda_tech.gokids.settings.activity.CityActivity;
import com.gokids.yoda_tech.gokids.settings.activity.SettingsActivity;
import com.gokids.yoda_tech.gokids.settings.adapter.AllergyAdapter;
import com.gokids.yoda_tech.gokids.settings.fragment.AllergyFragment;
import com.gokids.yoda_tech.gokids.settings.model.Allergy;
import com.gokids.yoda_tech.gokids.signup.activity.SignUpActivity;
import com.gokids.yoda_tech.gokids.utils.ClothingGridItem;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.GridItemView;
import com.gokids.yoda_tech.gokids.utils.MyGridView;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class EcommerceClothingActivity extends AppCompatActivity {



    private MyGridView gridView;
    private EcommerceClothingAdapter adapter;
    private ArrayList<ClothingBean> list= new ArrayList<>();
    private ArrayList<String> selectedAgelist= new ArrayList<>();
    private ArrayList<String> selectAge= new ArrayList<>();
    private ArrayList<String> GenderArray= new ArrayList<>();
    String[] genderarray= {"girl","boy"};
    boolean boyclick=false;
    boolean girlclick=false;

    private Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothing_activity);
        initView();

    }

    private void initView() {
        gridView = findViewById(R.id.clothing_age_gridview);
        continueBtn = findViewById(R.id.continue_clothing);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("","selected in click"+"selected Agegroup "+selectAge.size()+" "+"selected gender"+GenderArray.size());
                view.setBackground(getDrawable(R.drawable.bg_selected));
                Toast.makeText(EcommerceClothingActivity.this, "selected Agegroup "+selectAge.size()+" "+"selected gender"+GenderArray.size(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EcommerceClothingActivity.this,EcommercAllClothesListActivity.class);
                intent.putExtra(",genderList",GenderArray);
                intent.putExtra("ageList",selectAge);
                startActivity(intent);

            }
        });
         setData();
        gridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });
        gridView.setVerticalScrollBarEnabled(false);


        adapter = new EcommerceClothingAdapter(EcommerceClothingActivity.this, list);
        gridView.setAdapter(adapter);
        gridView.setMultiChoiceModeListener(new MultiChoiceModeListener());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(list.get(position).isSelected())
                {
                    list.get(position).setSelected(false);
                    ((ClothingGridItem) v).display(false);
                    selectAge.remove(list.get(position).getDispText());
                    Log.e("","selected"+selectAge.size());

                }
                else
                {
                    list.get(position).setSelected(true);
                    ((ClothingGridItem) v).display(true);
                    selectAge.add(list.get(position).getDispText());
                    Log.e("","selected"+selectAge.size());

                }




            }

        });



    }

    private void setData() {
        list.add(new ClothingBean(false,"0-1","0-1"));
        list.add(new ClothingBean(false,"1-2","1-2"));
        list.add(new ClothingBean(false,"2-3","2-3"));
        list.add(new ClothingBean(false,"3-4","3-4"));
        list.add(new ClothingBean(false,"4-5","4-5"));
        list.add(new ClothingBean(false,"5-6","5-6"));
        list.add(new ClothingBean(false,"6-7","6-7"));
        list.add(new ClothingBean(false,"7-8","7-8"));
        list.add(new ClothingBean(false,"8-9","8-9"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ecommerce_main_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.house)
        {
            Intent intent=  new Intent(EcommerceClothingActivity.this,GoKidsHome.class);
            intent.putExtra("flag","0");
            startActivity(intent);

        }
       else if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }


    public class MultiChoiceModeListener implements
            GridView.MultiChoiceModeListener {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Select Items");
            mode.setSubtitle("One item selected");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position,
                                              long id, boolean checked) {
            int selectCount = gridView.getCheckedItemCount();
            switch (selectCount) {
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + selectCount + " items selected");
                    break;
            }
        }

    }
    public void selectAll(View view)
    {
        view.setBackground(getResources().getDrawable(R.drawable.bg_selected));
        for(int i=0;i<list.size();i++)
        {
            list.get(i).setSelected(true);
            selectedAgelist.add(list.get(i).getDispText());

        }
        adapter.notifyDataSetChanged();
        selectAge.addAll(selectedAgelist);
        Log.e("click all","click all"+selectAge.size());

    }
    public void selectGirl(View view)
    {
        if(girlclick==true) {
            GenderArray.remove(genderarray[0]);
            view.setBackground(getDrawable(R.drawable.black_outline_reactangle_filled_grey));
            girlclick=false;

        }
        else
        {
            GenderArray.add(0,genderarray[0]);
            girlclick=true;
            view.setBackground(getDrawable(R.drawable.bg_selected));


        }

    }
    public void selectBoy(View view)
    {
        if(boyclick==true) {
            GenderArray.remove(genderarray[1]);
            boyclick=false;
            view.setBackground(getDrawable(R.drawable.black_outline_reactangle_filled_grey));
        }
        else
        {
            boyclick=true;
            GenderArray.add(genderarray[1]);
            view.setBackground(getDrawable(R.drawable.bg_selected));


        }


    }
}
