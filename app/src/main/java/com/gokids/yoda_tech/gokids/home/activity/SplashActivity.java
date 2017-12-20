package com.gokids.yoda_tech.gokids.home.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.signup.activity.SignUpActivity;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.Utils;


public class SplashActivity extends AppCompatActivity {

    private Location latlon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        progressBar.setVisibility(View.INVISIBLE);
        latlon= Utils.getLatLong(SplashActivity.this);




        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageResource(R.drawable.splash1);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });

                    sleep(2000);

                    SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME, MODE_PRIVATE);

                    if(prefs.contains("userId") && prefs.getInt("userId",0)!=0) {
                        Intent i = new Intent(getApplicationContext(),GoKidsHome.class);
                        i.putExtra("flag","0");
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(getApplicationContext(),SignUpActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }

                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });

        thread.start();
    }


}
