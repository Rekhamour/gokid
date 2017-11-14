package com.gokids.yoda_tech.gokidsapp.learn.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.learn.Util.ProviderDetails;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 21/04/17.
 */

public class AddReview extends AsyncTask<String,Void,Void> {

    Context mContext;
    AddReviewComplete addReviewComplete;

    public AddReview(Context c, AddReviewComplete addReviewComplete1){
        mContext = c;
        addReviewComplete = addReviewComplete1;
    }

    public interface AddReviewComplete{
        void onComplete();
    }


    @Override
    protected Void doInBackground(String... strings) {

        String courseID = strings[0]+"/";
        String string1 = strings[1].replaceAll(" ","%20");
        String review = string1+"/";

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        ArrayList<ProviderDetails> detailses = null;



        final String URL = mContext.getString(R.string.base_url)+ mContext.getString(R.string.addDeleteReview)
                + mContext.getString(R.string.reviewee) + courseID + mContext.getString(R.string.review)
                + review + mContext.getString(R.string.email) + mContext.getString(R.string.emailID)
                + mContext.getString(R.string.reviewID);

        Log.v("url" ,URL);

        Uri uri = Uri.parse(URL).buildUpon().build();

        try {
            java.net.URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream !=null){
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            }
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine())!=null){
                buffer.append(line + "\n");
            }

            String jSonString = buffer.toString();

            boolean status = Utils.checkStatus(mContext,jSonString);

            if(status){
                Toast.makeText(mContext,"Review Added Successfully",Toast.LENGTH_LONG);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            urlConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        addReviewComplete.onComplete();
    }
}

