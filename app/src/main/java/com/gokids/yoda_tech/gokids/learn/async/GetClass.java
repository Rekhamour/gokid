package com.gokids.yoda_tech.gokids.learn.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.learn.Util.ProviderDetails;
import com.gokids.yoda_tech.gokids.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 19/04/17.
 */

public class GetClass extends AsyncTask<String,Void,ArrayList<ProviderDetails>> {

    private Context mContext;

    public AsyncComplete asyncComplete;

    public interface AsyncComplete{
        void processCompleted(Object obj);
    }

    public GetClass(Context c, AsyncComplete a){
        mContext = c;
        asyncComplete = a;

    }


    @Override
    protected ArrayList<ProviderDetails> doInBackground(String... strings) {

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        ArrayList<ProviderDetails> detailses = null;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Integer pref1 = prefs.getInt("level", 0);
        String selected = prefs.getString("selected","0");

        if(selected.equals("")){
            selected="0";
        }

        String latitude = "1.358920"+"/";
        String longitude = "103.937346"+"/";
        String level = pref1 +"/";
        String subTopic = selected +"/";
        String location = "-"+"/";
        String provider = "-"+"/";
        String limitStart = "0"+"/";
        String count = "200"+"/";


        final String URL = mContext.getString(R.string.base_url)+ mContext.getString(R.string.viewProvider)
                + mContext.getString(R.string.latitude) + latitude + mContext.getString(R.string.longitude)
                + longitude + mContext.getString(R.string.level) + level + mContext.getString(R.string.subTopic)
                + subTopic + mContext.getString(R.string.location) + location + mContext.getString(R.string.provider)
                + provider + mContext.getString(R.string.limitStart) + limitStart + mContext.getString(R.string.count)
                + count;

        Log.v("url" ,URL);

        Uri uri = Uri.parse(URL).buildUpon().build();

        try {
            URL url = new URL(uri.toString());
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

            if (status){
                detailses = Utils.parseDetailsString(mContext, jSonString);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            urlConnection.disconnect();
        }

        return detailses;
    }

    @Override
    protected void onPostExecute(ArrayList<ProviderDetails> detailses) {
        if(detailses!=null && !detailses.isEmpty()){
            Log.v("sizeAsync",detailses.size()+"");
            asyncComplete.processCompleted(detailses);
        }
    }
}
