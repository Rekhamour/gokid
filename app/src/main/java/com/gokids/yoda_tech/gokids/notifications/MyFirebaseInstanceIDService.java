package com.gokids.yoda_tech.gokids.notifications;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Lenovo on 4/16/2018.
 */

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    private SharedPreferences prefrence;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // If you want to send messages to this application instance or // manage this apps subscriptions on the server side, send the // Instance ID token to your app server.
       // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        MySharedPrefrence.getPrefrence(getApplicationContext()).edit().putString("FIREBASE_TOKEN",refreshedToken).apply();
        prefrence = getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);

       /*String url= Urls.BASE_URL+"api/registerDeviceToken/deviceToken/"+refreshedToken+"/email/"+MySharedPrefrence.getPrefrence(getApplicationContext()).getString("emailId","");
       Log.e("url","url"+url);*/

    }
}
