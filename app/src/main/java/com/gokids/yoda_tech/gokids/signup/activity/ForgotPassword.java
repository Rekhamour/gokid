package com.gokids.yoda_tech.gokids.signup.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Lenovo on 7/17/2017.
 */
public class ForgotPassword extends AppCompatActivity {
    private EditText emailId;
    private String TAG= getClass().getName();
    private Button forgot_password_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        gettingkeyhash();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SetUpforgetPassword();


    }
    private void gettingkeyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.gokids.yoda_tech.gokids",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    private void SetUpforgetPassword() {
        emailId= (EditText)findViewById(R.id.email_id);
        forgot_password_btn= (Button)findViewById(R.id.forgot_password_btn);
        final String email= emailId.getText().toString();
        forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCall(email);

            }
        });

    }

    private void apiCall(String email) {
        Log.e(TAG,"i m in clcik n function"+email);
       String url=  Urls.BASE_URL+"api/forgotPassword/email/"+emailId.getText().toString();
        Log.e(TAG,"i m in clcik n function"+url);

        Ion.with(ForgotPassword.this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.e(TAG,"result"+result.toString());
                    if(e==null)
                        {
                            String status= result.get("status").toString();
                            String message = result.get("message").toString().replaceAll("\"","");
                            Log.e(TAG,"message"+message);

                            Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPassword.this,SignUpActivity.class);
                            startActivity(intent);

                        }
                        else
                        {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            startActivity(new Intent(ForgotPassword.this,SignUpActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
