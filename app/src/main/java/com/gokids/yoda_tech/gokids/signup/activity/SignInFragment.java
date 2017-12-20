package com.gokids.yoda_tech.gokids.signup.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.home.activity.GoKidsHome;
import com.gokids.yoda_tech.gokids.signup.async.SignInTask;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private String emailId;
    private String pass;
    private LoginButton mLoginButton;
    private SignInButton mSignInButton;
    EditText mailId;
    EditText password;
    Button signIn;
    CallbackManager callbackManager;


    private TextView forgot_password;
    private static final int RC_SIGN_IN = 007;

    private String TAG = getClass().getName();
    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FacebookSdk.sdkInitialize(getContext()); // ####### Facebook Sign In Coding
      //  AppEventsLogger.activateApp(getContext());
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        gettingkeyhash();

        mailId = (EditText) rootView.findViewById(R.id.email_signin);
        password  = (EditText) rootView.findViewById(R.id.pass_signin);
        signIn = (Button) rootView.findViewById(R.id.signInButton);
        forgot_password = (TextView) rootView.findViewById(R.id.forgot_password);
        callbackManager = CallbackManager.Factory.create();


        signInSetup(rootView);
        setupLoginButton(rootView);
        setupSignInButton(rootView);
        setupFogotPassword(rootView);


        return rootView;
    }

    private void gettingkeyhash() {
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.facebook.samples.loginhowto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void setupFogotPassword(View rootView) {
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public void signInSetup(View rootView) {


        final SharedPreferences prefs = getContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME, Context.MODE_PRIVATE);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailId = mailId.getText().toString();
                pass = password.getText().toString();
               /* if (pass.length() < 8) {
                    Toast.makeText(getActivity(), "Password should be atLeast 8 characters ", Toast.LENGTH_SHORT).show();

                } else {*/

                    SignInTask signInTask = new SignInTask(getContext(), emailId, pass, new SignInTask.SignInComplete() {
                        @Override
                        public void onSignInComplete(boolean isSignedIn) {
                            if (isSignedIn) {
                                Toast.makeText(getContext(), "Signed In", Toast.LENGTH_SHORT).show();

                                Intent home = new Intent(getContext(), GoKidsHome.class);
                                home.putExtra("flag","0");
                                home.putExtra("userProfile","singedup");
                                startActivity(home);

                            } else {
                                Toast.makeText(getContext(), prefs.getString("message", "Invalid Password"), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                    signInTask.execute();

                }
           // }
        });
    }

    public void setupLoginButton(View view) {

        mLoginButton = (LoginButton) view.findViewById(R.id.login_button);
      Button  LoginButtonfb = (Button) view.findViewById(R.id.LoginButtonfb);
        mLoginButton.setFragment(this);
        //mLoginButton.performClick();

        mLoginButton.setReadPermissions(Arrays.asList("public_profile, email")); // ####### Facebook Sign In Coding
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG,"loginresult"+loginResult);

                getUserDetails(loginResult);
               // Log.e(TAG,"loginresult"+loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG,"exception"+exception);
                // App code
            }
        });
        LoginButtonfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(),Arrays.asList("public_profile, email"));

                // mLoginButton.performClick();
            }
        });

    }




    public void setupSignInButton(View view) {

        mSignInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }


    private void signIn() {
          setgoogleOptions();
       // Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        //startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void buildClient(GoogleSignInOptions gso) {
       if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() , this )
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        /*if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity() *//* FragmentActivity *//*, this *//* OnConnectionFailedListener *//*)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
       // }
    }


    private void setgoogleOptions() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        buildClient(gso);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if( callbackManager.onActivityResult(requestCode, resultCode, data))
       {
           return;
       }
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        Log.e(TAG,"response"+json_object.toString());
                        Intent intent = new Intent(getActivity(), GoKidsHome.class);
                        intent.putExtra("flag","1");
                        intent.putExtra("userProfile", json_object.toString());
                        startActivity(intent);
                        Utils.storeFacebookCredentials(getActivity(),json_object.toString());

                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }



    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
           // mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
           // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
           // updateUI(false);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mGoogleApiClient.stopAutoManage(getActivity());
        //mGoogleApiClient.disconnect();
    }

    public void onStart() {
        super.onStart();

     /*   OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage((getActivity()) );
            mGoogleApiClient.disconnect();
        }
    }
}
