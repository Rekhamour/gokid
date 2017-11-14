package com.gokids.yoda_tech.gokidsapp.learn.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.learn.async.AddReview;


/**
 * Created by manoj2prabhakar on 22/04/17.
 */

public class AddReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_add_review);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button button = (Button) findViewById(R.id.postReview);
        button.setText("Post Review");
        final String courseId = getIntent().getStringExtra("course");
        final TextInputEditText text = (TextInputEditText) findViewById(R.id.inputText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReview addReview = new AddReview(getApplicationContext(), new AddReview.AddReviewComplete() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(),"Review Added Successfully",Toast.LENGTH_LONG);
                    }
                });

                addReview.execute(courseId,text.getText()+"");
                finish();
            }
        });

    }
}
