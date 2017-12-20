package com.gokids.yoda_tech.gokids.signup.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by manoj2prabhakar on 25/05/17.
 */

public class SignInTask extends AsyncTask<Void,Void,Void> {

    private Context mContext;
    private String emailId;
    private String password;
    private SignInComplete mSignInComplete;
    boolean loggedIn = false;

    public interface SignInComplete {
        void onSignInComplete(boolean isSignedIn);
    }

    public SignInTask(Context c, String email, String password, SignInComplete signInComplete) {
        this.mContext = c;
        this.emailId = email;
        this.password = password;
        this.mSignInComplete = signInComplete;
    }


    @Override
    protected Void doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        final String URL = mContext.getResources().getString(R.string.base_url)
                + mContext.getResources().getString(R.string.api_login)
                + mContext.getResources().getString(R.string.email)
                + emailId + "/"
                + mContext.getResources().getString(R.string.api_password)
                + password + "/"
                + mContext.getResources().getString(R.string.api_fb)
                + "-/"
                + mContext.getResources().getString(R.string.api_google)
                + "-/";

        Uri uri = Uri.parse(URL).buildUpon().build();

        try {
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
            }

            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine())!=null ) {
                buffer.append(line + "\n");
            }

            String jsonString = buffer.toString();
            loggedIn = Utils.checkLoginWithUserName(mContext,jsonString);

            if(loggedIn) {
                Utils.storeCredentials(mContext,jsonString);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mSignInComplete.onSignInComplete(loggedIn);
    }
}
