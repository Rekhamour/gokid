package com.gokids.yoda_tech.gokids.sos;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoActivity extends AppCompatActivity implements  Session.SessionListener,PublisherKit.PublisherListener{
    private static String API_KEY = "46143722";
    private static String SESSION_ID = "";//"1_MX40NjE0MzcyMn5-MTUyOTk1NjcwMTk4NX5QY2VMeE9mUWU2SmlybzE0S2tzTVNuQ3J-fg";
    private static String TOKEN = "";//"T1==cGFydG5lcl9pZD00NjE0MzcyMiZzaWc9ZDA5OWY1OTg5YTFhOTJiYmM1ODZmZGU0MTFhZjM4NzYwY2EwYWQ2NDpzZXNzaW9uX2lkPTFfTVg0ME5qRTBNemN5TW41LU1UVXlPVGsxTmpjd01UazROWDVRWTJWTWVFOW1VV1UyU21seWJ6RTBTMnR6VFZOdVEzSi1mZyZjcmVhdGVfdGltZT0xNTI5OTU2ODM0Jm5vbmNlPTAuMjg2NTI0NzIzMDQ3MTI2NiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTMyNTQ4ODMxJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = VideoActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;

    private Subscriber mSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vedio_call);
        getSessionId();
        //requestPermissions();
    }
    private void getSessionId() {
        String url= Urls.BASE_URL+"api/service/moderator/"+ MySharedPrefrence.getPrefrence(VideoActivity.this).getString("emailId","");
        Log.e("url result","url to session"+url);

        Ion.with(VideoActivity.this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                      if(e==null)
                      {
                      Log.e("url result","url to session"+url);
                          String status=result.get("status").getAsString();
                          SESSION_ID =result.get("result").getAsString();
                          getTokenId(SESSION_ID);

                          // SESSION_ID= sessionId;
                          Log.e("url result","url to session"+SESSION_ID);

                      }
                      else
                      {
                          Log.e("exception in session","exception"+ e.toString());
                      }
                    }
                });

    }

    private void getTokenId(String id) {
        String url= Urls.BASE_URL+"api/sessionToken/sessionID/"+id;
        Log.e("url result","url to token"+url);

        Ion.with(VideoActivity.this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null)
                        {
                            Log.e("url result","url to token"+url);

                            String status=result.get("status").getAsString();
                            String tokenId =result.get("result").getAsString();
                            TOKEN= tokenId;
                            requestPermissions();

                            Log.e("url result","url to toekn"+TOKEN);

                        }
                    }
                });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);


    }
    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);

            // initialize and connect to the session
            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(this);
            mSession.connect(TOKEN);

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }
    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        mPublisherViewContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }


    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }
    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }

}
