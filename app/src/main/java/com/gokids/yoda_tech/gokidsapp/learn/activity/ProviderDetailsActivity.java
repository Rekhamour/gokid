package com.gokids.yoda_tech.gokidsapp.learn.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.learn.Util.Contacts;
import com.gokids.yoda_tech.gokidsapp.learn.Util.ProviderDetails;
import com.gokids.yoda_tech.gokidsapp.learn.Util.Reviews;
import com.gokids.yoda_tech.gokidsapp.learn.adapter.ReviewAdapter;
import com.gokids.yoda_tech.gokidsapp.learn.adapter.SlidingViewPageAdapter;
import com.gokids.yoda_tech.gokidsapp.learn.async.AddReview;
import com.gokids.yoda_tech.gokidsapp.learn.async.SetBookMark;
import com.gokids.yoda_tech.gokidsapp.learn.async.ViewBookMark;
import com.gokids.yoda_tech.gokidsapp.learn.async.ViewReview;

import java.util.ArrayList;

public class ProviderDetailsActivity extends AppCompatActivity {
    String[] imageURLs;
    ProviderDetails details;
    boolean isBookmarked = false;
    ListView listViewReview;
    ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        details = (ProviderDetails)getIntent().getSerializableExtra("details");

        getSupportActionBar().setTitle(details.getTitle());


        setTabAndViewPager();

        TextView amountText = (TextView) findViewById(R.id.amountText);
        TextView amount = (TextView) findViewById(R.id.amount);
        TextView kidsAffText = (TextView) findViewById(R.id.kidSAffText);
        RatingBar kidsScore = (RatingBar) findViewById(R.id.ratings);
        TextView ageRangeText = (TextView) findViewById(R.id.AgeRangeText);
        TextView ageRange = (TextView) findViewById(R.id.ageRange);
        TextView providerText = (TextView) findViewById(R.id.providerText);
        TextView provider = (TextView) findViewById(R.id.provider);
        TextView detailsText = (TextView) findViewById(R.id.detailsText);
        final TextView details1 = (TextView) findViewById(R.id.details);
        TextView addressText = (TextView) findViewById(R.id.addressText);
        TextView address = (TextView) findViewById(R.id.address);
        TextView contactText = (TextView) findViewById(R.id.contactText);
        TextView contactNumbers = (TextView) findViewById(R.id.contactNumbers);
        TextView emailText = (TextView) findViewById(R.id.emailText);
        TextView email = (TextView) findViewById(R.id.email);

        amountText.setText("Amount");
        amount.setText(details.getPriceSummary() + "/" + details.getPricePrefix());
        kidsAffText.setText("Kids Affinity");
        if (details.getKidsAffinityScore() != "") {
            kidsScore.setRating(Float.parseFloat(details.getKidsAffinityScore()));
        }
        ageRangeText.setText("Age Range");
        ageRange.setText(details.getAgeGroup());
        providerText.setText("Provider");
        provider.setText(details.getName());
        detailsText.setText("Details");
        details1.setText(details.getDetails());
        addressText.setText("Address");
        address.setText(details.getAddress());
        contactText.setText("Contact Numbers");

        String contactNumbers1 = "";

        for (Contacts contacts: details.getContacts()){
            contactNumbers1 += contacts.getPhoneNumber() + "\n";
        }

        contactNumbers.setText(contactNumbers1);


        emailText.setText("Email");
        email.setText(details.getEmail());

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + details.getEmail()));
                emailIntent.setType("message/rfc822");
                //startActivity(emailIntent);
                startActivity(Intent.createChooser(emailIntent, "Send Email Using: "));
            }
        });


        setDialog();
        setFab();
        setReview();

        final Button addReview = (Button) findViewById(R.id.buttonReview);
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddReviewActivity.class);
                intent.putExtra("course",details.getCourseID());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                showList();
                return true;
            case R.id.action_share:
                shareIntent();
                return true;
            case R.id.action_location:
                openMaps();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showList(){
        finish();
    }

    public void shareIntent(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this class: "+ details.getTitle() + "\n" + "Phone Number: "+details.getContacts().get(0).getPhoneNumber());
        startActivity(Intent.createChooser(sharingIntent, "Share content using"));
    }

    public void openMaps(){
        String uri = "http://maps.google.com/maps?saddr=" + "1.358920" + ","
                + "103.937346" + "&daddr=" + details.getLatlong();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    public void setReview(){
        final TextView numRating = (TextView) findViewById(R.id.numRating);
        numRating.setText("0");

        listViewReview = (ListView) findViewById(R.id.listViewReview);
        ArrayList<Reviews> reviewses = null;
        reviewAdapter =  new ReviewAdapter(getApplicationContext(),reviewses);
        listViewReview.setAdapter(reviewAdapter);


        ViewReview viewReview = new ViewReview(getApplicationContext(), new ViewReview.ReturnReview() {
            @Override
            public void onComplete(ArrayList<Reviews> reviews) {
                numRating.setText(reviews.size()+"");
                reviewAdapter.swapContent(reviews);
                setListView();
            }
        });

        viewReview.execute(details.getCourseID());


    }

    public void setFab(){
        final FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        NestedScrollView scroller = (NestedScrollView) findViewById(R.id.nestedScroll);

        if (scroller != null) {

            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(scrollY==0){
                        fab1.show();
                    }
                    else if(scrollY>0){
                        fab1.hide();
                    }
                }
            });
        }

        ViewBookMark viewBookMark = new ViewBookMark(getApplicationContext(), new ViewBookMark.IsBookMarked() {
            @Override
            public void onCompleted(Boolean isBooked) {
                isBookmarked = isBooked;
                if(isBookmarked){
                    fab1.setImageResource(R.drawable.btn_bookmark_y);
                }
            }
        });

        viewBookMark.execute(details.getCourseID());


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetBookMark setBookMark = new SetBookMark(ProviderDetailsActivity.this);
                if(!isBookmarked){
                    setBookMark.execute(details.getCourseID(),"1");
                    fab1.setImageResource(R.drawable.btn_bookmark_y);
                    isBookmarked = true;
                }
                else {
                    setBookMark.execute(details.getCourseID(),"0");
                    fab1.setImageResource(R.drawable.btn_bookmark_n);
                    isBookmarked = false;
                }
            }
        });


    }

    public void setDialog(){
        final Dialog dialog = new Dialog(ProviderDetailsActivity.this);
        dialog.setContentView(R.layout.dialog_call);

        ListView lv = (ListView ) dialog.findViewById(R.id.lv);
        dialog.setCancelable(true);
        dialog.setTitle("ListView");

        ArrayList<String> names = new ArrayList<>();

        for (Contacts contacts: details.getContacts()){
            names.add(contacts.getPhoneNumber());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.layout_text,names);
        lv.setAdapter(adapter);

        Button call = (Button) findViewById(R.id.fab);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + details.getContacts().get(i).getPhoneNumber()));
                startActivity(intent);
            }
        });
    }

    public void setTabAndViewPager(){
        imageURLs = new String[details.getImages().size()];
        for(int i = 0; i<details.getImages().size();i++){
            imageURLs[i] = details.getImages().get(i).getImageURL();
        }


        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SlidingViewPageAdapter(getApplicationContext(),imageURLs));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    public void addReview(){

        Dialog dialog = new Dialog(ProviderDetailsActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_review, null);

        dialog.setCancelable(true);
        dialog.setTitle("Add Review");

        Button button = (Button) dialogView.findViewById(R.id.postReview);
        final TextInputEditText text = (TextInputEditText) dialogView.findViewById(R.id.inputText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReview addReview = new AddReview(getApplicationContext(), new AddReview.AddReviewComplete() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(),"Review Added Successfully",Toast.LENGTH_LONG);
                    }
                });

                addReview.execute(text.getText()+"");
            }
        });

        dialog.setContentView(dialogView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setReview();
    }

    public void setListView() {

        ViewGroup vg = listViewReview;
        int totalHeight = 0;
        for (int i = 0; i < reviewAdapter.getCount(); i++) {
            View listItem = reviewAdapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listViewReview.getLayoutParams();
        par.height = totalHeight + (listViewReview.getDividerHeight() * (reviewAdapter.getCount() - 1));
        listViewReview.setLayoutParams(par);
        listViewReview.requestLayout();

    }}
