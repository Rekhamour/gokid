package com.gokids.yoda_tech.gokids.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.bookmark.activity.AllBookmarksActivity;
import com.gokids.yoda_tech.gokids.eat.activity.FoodListActivity;
import com.gokids.yoda_tech.gokids.eat.adapter.ImageSLiderAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceGridAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceGridListAdapter;
import com.gokids.yoda_tech.gokids.ecommerce.adapter.EcommerceSLiderAdapter;
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
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.GridItemView;
import com.gokids.yoda_tech.gokids.utils.ItemClickListener;
import com.gokids.yoda_tech.gokids.utils.MyGridView;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.RecyclerItemClickListener;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class EcommerceMainActivity extends AppCompatActivity {

    private MyGridView listView;
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private ArrayList<Integer> imagesList;
    private GridLayoutManager lm;
    private ArrayList<Allergy> allergies;
    public static ArrayList<Allergy> selectedallergie= new ArrayList<>();
    private EcommerceGridListAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RoundedImageView image;
    private NavigationView navigation;
    private TextView name;
    private int currentpage;
    private String TAG=getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecommerce_main_parent_layout);
        initInstances();
        initView();

    }

    private void initView() {
        viewPager= findViewById(R.id.ecommerce__pager);
        listView= findViewById(R.id.ecommerce_list);
        setImages();
        setImagesforGrid();
        viewPager.setAdapter(new EcommerceSLiderAdapter(EcommerceMainActivity.this,imagesList));
        indicator = findViewById(R.id.image_indicator);
        indicator.setViewPager(viewPager);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                if (currentpage == imagesList.size()) {
                    currentpage = 0;
                }
                viewPager.setCurrentItem(currentpage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
        lm=  new GridLayoutManager(EcommerceMainActivity.this,3);
        adapter = new EcommerceGridListAdapter(EcommerceMainActivity.this,allergies);
        listView.setAdapter(adapter);
        listView.setMultiChoiceModeListener(new MultiChoiceModeListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(position==0||position==2||position==3||position==4) {
                    if (allergies.get(position).isSelected()) {
                        allergies.get(position).setSelected(false);
                        ((GridItemView) v).display(false,position);
                        selectedallergie.remove(allergies.get(position));
                    } else {
                        allergies.get(position).setSelected(true);
                        ((GridItemView) v).display(true,position);
                        selectedallergie.add(allergies.get(position));
                    }
                }
                for(int j=0;j<selectedallergie.size();j++)
                {
                    Log.e(TAG," selected strings" + selectedallergie.get(j));
                }




            }
        });



        Log.e("","selected"+selectedallergie.size());
    }

    private void setImagesforGrid() {
         allergies=  new ArrayList<>();
        allergies.add(new Allergy(R.drawable.allergy_dairyfree, false, "Dairy Free", "SN26"));
        allergies.add(new Allergy(R.drawable.allergy_eggfree, false, "Egg Free", "SN4"));
        allergies.add(new Allergy(R.drawable.allergy_gluttenfree, false, "Glutten Free", "SN6"));
        allergies.add(new Allergy(R.drawable.allergy_lactosefree, false, "Lactose Free", "SN33"));
        allergies.add(new Allergy(R.drawable.allergy_nuttfree, false, "Nut Free", "SN5"));
        allergies.add(new Allergy(R.drawable.allergy_sesame, false, "Sesame Free", "SN28"));
        allergies.add(new Allergy(R.drawable.allergy_shelfishfree, false, "Shelfish Free", "SN27"));
        allergies.add(new Allergy(R.drawable.allergy_soyfree, false, "Soy Free", "SN29"));
        allergies.add(new Allergy(R.drawable.allergy_wheatfree, false, "Wheat Free", "SN32"));
    }

    private void setImages() {
        imagesList= new ArrayList<>();
        imagesList.add(R.drawable.ad1);
        imagesList.add(R.drawable.ad2);
        imagesList.add(R.drawable.ad3);
        imagesList.add(R.drawable.ad4);
        imagesList.add(R.drawable.ad5);
        imagesList.add(R.drawable.ad6);
        imagesList.add(R.drawable.entad);


    }
    private void initInstances() {
        Toolbar toolbar = findViewById(R.id.ecommerce_main_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
        getSupportActionBar().setTitle("E-Commerce");


        drawerLayout = findViewById(R.id.drawerLayout_ecommerce);
        drawerToggle = new ActionBarDrawerToggle(EcommerceMainActivity.this, drawerLayout,toolbar, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerToggle.setHomeAsUpIndicator(R.drawable.img_hamburger_menu);
        drawerToggle.setDrawerIndicatorEnabled(false);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        final SharedPreferences.Editor editor= prefs.edit();
        navigation = findViewById(R.id.navigation_ecommerce);
        final View header = navigation.getHeaderView(0);

        image = header.findViewById(R.id.nav_userImage);
        name = header.findViewById(R.id.nav_username);


        Menu menu = navigation.getMenu();
        MenuItem sign_out = menu.getItem(4);
        if(prefs.contains("emailId") && !prefs.getString("emailId","").isEmpty() ) {
            Log.e("Signin fragment","userId "+  prefs.getString("emailId",""));
            name.setText(prefs.getString("userName","Guest"));

            if(prefs.contains("ImageURL") && !prefs.getString("ImageURL","").isEmpty()) {
                Picasso.with(EcommerceMainActivity.this).load(prefs.getString("ImageURL", "")).into(image);
            }
            if(!MySharedPrefrence.getPrefrence(EcommerceMainActivity.this).getString("emailId","").trim().isEmpty()) {
                String path = "https://s3-ap-southeast-1.amazonaws.com/kisimages/User/" + MySharedPrefrence.getPrefrence(EcommerceMainActivity.this).getString("emailId", "") + ".jpg";
                Picasso.with(EcommerceMainActivity.this).load(path).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
            }

        } else {
            sign_out.setTitle("Sign In");
        }


        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_sign_out:
                       /* if (flag.equalsIgnoreCase("1"))
                        {
                            Utils.setSharedPreferenceEmpty(editor);
                            disconnectFromFacebook();
                        }
                        else {*/
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
                        SharedPreferences.Editor editor= prefs.edit();
                        Utils.setSharedPreferenceEmpty(editor);
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(EcommerceMainActivity.this, SignUpActivity.class);
                        startActivity(intent);
                        // }
                        return true;

                    case R.id.nav_settings:
                        Intent settingsIntent = new Intent(EcommerceMainActivity.this, SettingsActivity.class);
                        settingsIntent.putExtra("flag","1");
                        startActivity(settingsIntent);
                        return true;
                    case R.id.nav_refer_friend:
                        Intent referfriendIntent = new Intent(EcommerceMainActivity.this, ReferFriend.class);
                        startActivity(referfriendIntent);
                        return true;
                    case R.id.nav_home_drawer:
                        Intent homeintent = new Intent(EcommerceMainActivity.this,GoKidsHome.class);
                        homeintent.putExtra("flag","3");
                        startActivity(homeintent);
                        return true;
                    case R.id.nav_selectcity:
                        Intent intents= new Intent(EcommerceMainActivity.this, CityActivity.class);
                        startActivity(intents);
                        return true;

                    case R.id.nav_bookmark:
                        Intent bookintent = new Intent(EcommerceMainActivity.this,AllBookmarksActivity.class);
                        startActivity(bookintent);
                        return true;

                    default:
                        return true;
                }
            }
        });



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
            Intent intent=  new Intent(EcommerceMainActivity.this,GoKidsHome.class);
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
            int selectCount = listView.getCheckedItemCount();
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
    public void clickContinue(View view)
    {
        Toast.makeText(this, "selected" +selectedallergie.size(), Toast.LENGTH_SHORT).show();
        view.setBackground(getResources().getDrawable(R.drawable.outline_with_radius_red_reactangle));
        Intent intent = new Intent(EcommerceMainActivity.this, EcommercProductsListActivity.class);
        intent.putExtra("selectedAllergi", selectedallergie);
        startActivity(intent);
    }
}
