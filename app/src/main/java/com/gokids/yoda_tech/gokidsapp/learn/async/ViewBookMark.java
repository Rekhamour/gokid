package com.gokids.yoda_tech.gokidsapp.learn.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by manoj2prabhakar on 21/04/17.
 */

public class ViewBookMark extends AsyncTask<String,Void,Boolean> {
    private Context mContext;
    private IsBookMarked isBookMarked;

    public interface IsBookMarked{
        void onCompleted(Boolean isBooked);
    }

    public ViewBookMark(Context c, IsBookMarked isBookMarked) {
        mContext = c;
        this.isBookMarked = isBookMarked;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String courseID = strings[0];
        boolean returnBool =false;

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String class6 = "CLS6/";

        final String URL = mContext.getString(R.string.base_url) + mContext.getString(R.string.viewAllBookmarks)
                + mContext.getString(R.string.email) + mContext.getString(R.string.emailID)
                + mContext.getString(R.string.classURL) + class6;

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

            boolean status = Utils.checkStatus(mContext, jSonString);

            if (status) {
                returnBool = Utils.checkCourseInBookMark(mContext,jSonString,courseID);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return returnBool;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        isBookMarked.onCompleted(aBoolean);
    }
}
