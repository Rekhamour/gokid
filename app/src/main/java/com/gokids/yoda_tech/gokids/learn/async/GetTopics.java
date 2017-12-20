package com.gokids.yoda_tech.gokids.learn.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.learn.Util.AllTopics;
import com.gokids.yoda_tech.gokids.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by manoj2prabhakar on 18/04/17.
 */

public class GetTopics extends AsyncTask<String,Void,AllTopics>{
    private Context mContext;
    AsyncCompleteTopics asyncCompleteTopics;

    public interface AsyncCompleteTopics{
        void processCompleted(Object obj);
    }

    public GetTopics(Context c, AsyncCompleteTopics asyncCompleteTopics1){
        mContext = c;
        asyncCompleteTopics = asyncCompleteTopics1;
    }
    @Override
    protected AllTopics doInBackground(String... strings) {

        final String LOG_TAG = mContext.getClass().getSimpleName();

        HttpURLConnection urlConnection=null;
        BufferedReader bufferedReader=null;

        final String URL = mContext.getString(R.string.base_url) + mContext.getString(R.string.viewTopics);
        Uri uri = Uri.parse(URL).buildUpon().build();
        AllTopics allTopics = null;

        try{
            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream!=null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            }
            StringBuffer stringBuffer=new StringBuffer();

            String line;
            while ((line = bufferedReader.readLine())!=null){
                stringBuffer.append(line + "\n");
            }


            String jSonString = stringBuffer.toString();

            boolean status = Utils.checkStatus(mContext,jSonString);

            if(status){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("topics",jSonString);

                allTopics = Utils.getJSONtoShared(mContext,jSonString);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        return allTopics;
    }

    @Override
    protected void onPostExecute(AllTopics allTopics) {
        if(allTopics!=null && allTopics.getAcademics()!=null && allTopics.getAcademics().size()!=0){
            asyncCompleteTopics.processCompleted(allTopics);
        }
    }
}
