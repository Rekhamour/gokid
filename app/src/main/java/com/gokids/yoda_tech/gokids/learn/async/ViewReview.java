package com.gokids.yoda_tech.gokids.learn.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.learn.Util.Reviews;
import com.gokids.yoda_tech.gokids.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 21/04/17.
 */

public class ViewReview extends AsyncTask<String,Void,ArrayList<Reviews>> {
    private Context mContext;
    private ReturnReview returnReview;

    public interface ReturnReview{
        void onComplete(ArrayList<Reviews> reviews);
    }

    public ViewReview(Context c, ReturnReview r){
        mContext = c;
        returnReview = r;
    }

    @Override
    protected ArrayList<Reviews> doInBackground(String... strings) {

        String courseID = strings[0];
        ArrayList<Reviews> reviewses = null;

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String class6 = "CLS6/";

        final String URL = mContext.getString(R.string.base_url) + mContext.getString(R.string.viewReviewPerReviewee)
                + mContext.getString(R.string.reviewee) + courseID;

        Log.v("url", URL);

        Uri uri = Uri.parse(URL).buildUpon().build();

        try {
            java.net.URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            }
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            String jSonString = buffer.toString();

            //boolean status = Utils.checkStatus(mContext, jSonString);


            reviewses = Utils.getReviews(mContext,jSonString);
            Log.v("asd",reviewses.get(0).getReview());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return reviewses;
    }

    @Override
    protected void onPostExecute(ArrayList<Reviews> reviewses) {
        if(reviewses!=null && reviewses.size()!=0){
            returnReview.onComplete(reviewses);
        }
    }
}
