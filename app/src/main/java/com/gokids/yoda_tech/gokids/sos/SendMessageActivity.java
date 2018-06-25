package com.gokids.yoda_tech.gokids.sos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;

public class SendMessageActivity extends AppCompatActivity {

    private EditText edtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        initView();
    }

    private void initView() {
        edtMessage= findViewById(R.id.message);
        //MySharedPrefrence.getPrefrence(SendMessageActivity.this).edit().putString("SOS_message",edtMessage.getText().toString()).commit();
    }

    public void redirectback(View view)
    {
        MySharedPrefrence.getPrefrence(SendMessageActivity.this).edit().putString("SOS_message",edtMessage.getText().toString()).commit();
        finish();
    }
}
