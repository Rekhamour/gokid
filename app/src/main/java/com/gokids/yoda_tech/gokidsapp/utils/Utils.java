package com.gokids.yoda_tech.gokidsapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.gokids.yoda_tech.gokidsapp.R;
import com.gokids.yoda_tech.gokidsapp.learn.Util.AllTopics;
import com.gokids.yoda_tech.gokidsapp.learn.Util.Classes;
import com.gokids.yoda_tech.gokidsapp.learn.Util.Contacts;
import com.gokids.yoda_tech.gokidsapp.learn.Util.Images;
import com.gokids.yoda_tech.gokidsapp.learn.Util.ProviderDetails;
import com.gokids.yoda_tech.gokidsapp.learn.Util.Reviews;
import com.gokids.yoda_tech.gokidsapp.learn.Util.SubTopic;
import com.gokids.yoda_tech.gokidsapp.learn.Util.Topics;
import com.gokids.yoda_tech.gokidsapp.learn.activity.DialogActivity;
import com.gokids.yoda_tech.gokidsapp.signup.activity.SignUpActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by manoj2prabhakar on 18/04/17.
 */

public class Utils {

    private static String TAG = "Utils";
    private static String Bucket_name = "kisimages/User";
    private static String flagupload="";
    private static Context context;
    public static String currentloc;
    private static LocationManager mLocationManager;
    private static long LOCATION_REFRESH_TIME=5000;
    private static float LOCATION_REFRESH_DISTANCE=500;

    public static boolean checkStatus(Context context, String jSONString){

        try {
            JSONObject jsonObject = new JSONObject(jSONString);
            String status = jsonObject.getString("status");
            if(status.equals("200")){
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static void openfullimage(Context context, int position, ArrayList<String> images)
    {
        Intent intent= new Intent(context, FullImageActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("Allimageslist",images);
        context.startActivity(intent);
    }
    public static Bitmap getGalleryBitmap(String userImage) {
        int targetW = 1024;
        int targetH = 600;
        Log.d(TAG, "picture path  " + userImage);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(userImage, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = 1;
        if ((targetW > photoW) || (targetH > photoH)) { //>1024
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            Log.e(TAG, "scaleFactor " + scaleFactor);
        }
        bmOptions.inJustDecodeBounds = false;
        if (photoW > 3500 || photoH > 3200)
            bmOptions.inSampleSize = 8;
        else if (photoW > 2500 || photoH > 2200)
            bmOptions.inSampleSize = 6;
        else if (photoW > 1500 || photoH > 1200)
            bmOptions.inSampleSize = 4;
        else if (photoW > 800 || photoH > 720)
            bmOptions.inSampleSize = 2;
        bmOptions.inPurgeable = true;        //***********************************
        Bitmap bitmap = BitmapFactory.decodeFile(userImage, bmOptions);
        ExifInterface ei = null;
        int rotate = 0;
        try {
            ei = new ExifInterface(userImage);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }
            Log.e(TAG, "rotate " + rotate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //***********************************
        return rotateImage(bitmap, rotate);
    }
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    public static String getAbsolutePath(Activity context, Uri uri) {
        Log.e(TAG, "Uri path " + uri);
        String[] projection = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return "";
    }



    public static AllTopics getJSONtoShared(Context context, String jSONString){

        AllTopics everything = null;

        try {
            JSONObject jsonObject = new JSONObject(jSONString);

            JSONArray topics = jsonObject.getJSONArray("result");
            JSONObject topic;
            String topicID,topicName,subTopID,subTopName;
            JSONArray subtopics;
            JSONObject subtopic;

            ArrayList<String> subTopicIDs;
            ArrayList<String> subTopicNames;
            ArrayList<Topics> allTopics = new ArrayList<>();



            Topics topics1;

            for (int i=0; i<topics.length();i++){
                topic = topics.getJSONObject(i);

                subTopicIDs = new ArrayList<>();
                subTopicNames = new ArrayList<>();

                topicID = topic.getString("TopicID");
                topicName = topic.getString("Topic");
                subtopics = topic.getJSONArray("SubTopics");

                for(int j =0;j<subtopics.length();j++){
                    subtopic = subtopics.getJSONObject(j);
                    subTopID = subtopic.getString("SubTopicID");
                    subTopName = subtopic.getString("SubTopic");

                    subTopicIDs.add(subTopID);
                    subTopicNames.add(subTopName);


                }

                topics1 = new Topics(topicID,topicName,subTopicIDs,subTopicNames);
                allTopics.add(topics1);

            }
            if(allTopics.size()!=0){
                //topics1 = allTopics;
                SubTopic subTopic;
                ArrayList<SubTopic> academics = new ArrayList<>();
                ArrayList<SubTopic> arts = new ArrayList<>();
                ArrayList<SubTopic> sports = new ArrayList<>();
                for(Topics topics2: allTopics){
                    //subTopic = new SubTopic(topics2.getTopicID(),topics2.getTopicName(),false);
                    //subTopics.add(subTopic);
                    if(topics2.getTopicName().equals("Academics")){
                        for(int i=0;i<topics2.getSubTopicIDs().size();i++){
                            subTopic = new SubTopic(topics2.getSubTopicIDs().get(i),topics2.getSubTopicNames().get(i),false);
                            academics.add(subTopic);

                        }
                    }
                    else if(topics2.getTopicName().equals("Arts & Crafts")){
                        for(int i=0;i<topics2.getSubTopicIDs().size();i++){
                            subTopic = new SubTopic(topics2.getSubTopicIDs().get(i),topics2.getSubTopicNames().get(i),false);
                            arts.add(subTopic);
                        }
                    }
                    else if(topics2.getTopicName().equals("Sports")){
                        for(int i=0;i<topics2.getSubTopicIDs().size();i++){
                            subTopic = new SubTopic(topics2.getSubTopicIDs().get(i),topics2.getSubTopicNames().get(i),false);
                            sports.add(subTopic);
                        }
                    }
                }
                DialogActivity.academics = academics;
                DialogActivity.arts = arts;
                DialogActivity.sports = sports;

                everything = new AllTopics(academics,arts,sports);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return everything;

    }

    public static void setStringPreference(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }


    public static ArrayList<ProviderDetails> parseDetailsString(Context context, String jSonString){
        ArrayList<ProviderDetails> providerDetailses = new ArrayList<>();

        try {
            JSONObject detailses = new JSONObject(jSonString);
            JSONArray results = detailses.getJSONArray("result");
            JSONObject result;
            ProviderDetails details;
            Contacts contacts;
            Images images;
            Classes classes;
            String learnProviderID,name,address,postalCode,latlong,email,courseID,title,detailss,priceSummary,pricePrefix
                    ,ageGroup,kidsAffinityScore,status,distance;

            String conContactID,conOwnerID,conPhoneNumber;
            String imgCounter,imgId,imgOwnerId, imgURL;
            String classId, subTopicID, subTopic, price, competencyLevel, cLMessage, mySchedule;



            ArrayList<Contacts> contactsArray;
            ArrayList<Images> imagesArray;
            ArrayList<Classes> classesArray;


            for(int i=0;i<results.length();i++){
                result = results.getJSONObject(i);

                learnProviderID = result.getString("LearnProviderID");
                name = result.getString("Name");
                address = result.getString("Address");
                postalCode = result.getString("Postal");
                latlong = result.getString("LatLong");
                email = result.getString("Email");
                courseID = result.getString("CourseID");
                title = result.getString("Title");
                detailss = result.getString("Details");
                priceSummary = result.getString("PriceSummary");
                pricePrefix = result.getString("PricePrefix");
                ageGroup = result.getString("AgeGroup");
                kidsAffinityScore = result.getString("KidsfinityScore");
                status = result.getString("Status");
                distance = result.getString("Distance");

                JSONArray contactsJSONArray= result.getJSONArray("Contacts");
                contactsArray = new ArrayList<>();
                JSONObject contact;

                for (int j=0; j<contactsJSONArray.length(); j++){

                    contact = contactsJSONArray.getJSONObject(j);
                    conContactID = contact.getString("ContactID");
                    conOwnerID = contact.getString("OwnerID");
                    conPhoneNumber = contact.getString("PhoneNo");

                    contacts = new Contacts(conContactID,conOwnerID,conPhoneNumber);
                    contactsArray.add(contacts);
                }

                JSONArray imagesJSONArray = result.getJSONArray("Images");
                imagesArray = new ArrayList<>();
                JSONObject image;

                for (int k=0; k<imagesJSONArray.length();k++){
                    image = imagesJSONArray.getJSONObject(k);

                    imgCounter = image.getString("Counter");
                    imgId = image.getString("ImageID");
                    imgOwnerId = image.getString("OwnerID");
                    imgURL = image.getString("ImageURL");

                    images = new Images(imgCounter,imgId,imgOwnerId,imgURL);
                    imagesArray.add(images);

                }

                JSONArray classesJSONArray = result.getJSONArray("Classes");
                classesArray = new ArrayList<>();
                JSONObject classObj;

                for (int l=0;l<classesJSONArray.length();l++){
                    classObj = classesJSONArray.getJSONObject(l);

                    classId = classObj.getString("ClassID");
                    subTopicID = classObj.getString("SubTopicID");
                    subTopic = classObj.getString("SubTopic");
                    price = classObj.getString("Price");
                    competencyLevel = classObj.getString("CompetencyLevel");
                    cLMessage = classObj.getString("CLMessage");
                    mySchedule = classObj.getString("Schedule");

                    Classes classes1 = new Classes(classId,subTopicID,subTopic,price,competencyLevel,cLMessage,mySchedule);
                    classesArray.add(classes1);
                }

                ProviderDetails providerDetails = new ProviderDetails(learnProviderID,name,
                        address,postalCode,latlong,email,courseID,title,detailss,priceSummary,pricePrefix,
                        ageGroup,kidsAffinityScore,status,distance,contactsArray,imagesArray,classesArray);

                providerDetailses.add(providerDetails);

            }

            //DisplayClassActivity.details = providerDetailses;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return providerDetailses;

    }

    public static boolean isNetworkConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean checkCourseInBookMark(Context context, String jSonString, String courseToCheck){

        try {
            JSONObject jsonObject = new JSONObject(jSonString);
            ArrayList<String> courseID = new ArrayList<>();

            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for(int i=0;i<jsonArray.length();i++){
                courseID.add(jsonArray.getJSONObject(i).getString("CourseID"));
                Log.v(courseToCheck,jsonArray.getJSONObject(i).getString("CourseID"));
            }

            if(courseID.contains(courseToCheck)){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ArrayList<Reviews> getReviews(Context context, String jsonString){
        ArrayList<Reviews> reviewses = null;

        try {
            JSONArray array = new JSONArray(jsonString);

            if(array.length()!=0){
                reviewses = new ArrayList<>();
                for(int i=0; i<array.length();i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    reviewses.add(new Reviews(jsonObject.getString("Review")
                            ,jsonObject.getString("Username"),jsonObject.getString("Reviewer")));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviewses;

    }

    public static boolean checkLoginWithUserName(Context context,String jSonString) {

        try {

            JSONArray jsonArray = new JSONArray(jSonString);


        } catch (JSONException e) {
            try {
                JSONObject jsonObject = new JSONObject(jSonString);
                if(jsonObject.has("status")) {
                    Log.v("Util","Invalid Username/Password");
                    SharedPreferences prefs = context.getSharedPreferences("UserName",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    if(jsonObject.has("message")) {
                        editor.putString("message",jsonObject.getString("message"));
                        editor.apply();
                    }
                    return false;
                }


            } catch (JSONException e1) {
                e1.printStackTrace();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void  storeFacebookCredentials(Context context, String jsonString) {
        SharedPreferences prefs = context.getSharedPreferences("UserName",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Integer userId = 0;
        String emailID = null;
        String userName = null;
        String city = null;
        String phoneNo = null;
        String ImageURL = null;



        try {

           // JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject user = new JSONObject(jsonString);

           // for (int i=0; i<jsonArray.length(); i++) {
                //user = jsonArray.getJSONObject(i);
                if(user.has("id")) {
                    userId = user.getInt("id");
                }
                if(user.has("email")) {
                    emailID = user.getString("email");
                }
                if(user.has("name")) {
                    userName = user.getString("name");
                }
                if(user.has("City")) {
                    city = user.getString("City");
                }
                if(user.has("PhoneNo")) {
                    phoneNo = user.getString("PhoneNo");
                }
           // }

            JSONObject profile_pic_data = new JSONObject(user.get("picture").toString());
            JSONObject profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            if(profile_pic_url.has("url"))
            {
                ImageURL= profile_pic_url.getString("url");
            }


            editor.putInt("userId",userId);
            editor.putString("emailId",emailID);
            editor.putString("userName",userName);
            editor.putString("city",city);
            editor.putString("phoneNo",phoneNo);
            editor.putString("ImageURL",ImageURL);
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void storeCredentials(Context context, String jsonString) {
        SharedPreferences prefs = context.getSharedPreferences("UserName",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Integer userId = 0;
        String emailID = null;
        String userName = null;
        String city = null;
        String phoneNo = null;


        try {

            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject user;

            for (int i=0; i<jsonArray.length(); i++) {
                user = jsonArray.getJSONObject(i);
                if(user.has("UserID")) {
                    userId = user.getInt("UserID");
                }
                if(user.has("Email")) {
                    emailID = user.getString("Email");
                }
                if(user.has("UserName")) {
                    userName = user.getString("UserName");
                }
                if(user.has("City")) {
                    city = user.getString("City");
                }
                if(user.has("PhoneNo")) {
                    phoneNo = user.getString("PhoneNo");
                }
            }

            editor.putInt("userId",userId);
            editor.putString("emailId",emailID);
            editor.putString("userName",userName);
            editor.putString("city",city);

            editor.putString("phoneNo",phoneNo);
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void updateprefrences(Context context, String jsonString) {
        SharedPreferences prefs = context.getSharedPreferences("UserName",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Integer userId = 0;
        String emailID = null;
        String userName = null;
        String city = null;
        String phoneNo = null;
        String ImageURL = null;
        String CurrentLocation = null;
        String DeviceToken = null;
        String ResetPassword = null;
        String Role = null;
        String BadgeCount = null;


        try {

            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject user;

            for (int i=0; i<jsonArray.length(); i++) {
                user = jsonArray.getJSONObject(i);
                if(user.has("UserID")) {
                    userId = user.getInt("UserID");
                }
                if(user.has("Email")) {
                    emailID = user.getString("Email");
                }
                if(user.has("UserName")) {
                    userName = user.getString("UserName");
                }
                if(user.has("City")) {
                    city = user.getString("City");
                }
                if(user.has("PhoneNo")) {
                    phoneNo = user.getString("PhoneNo");
                }
                if(user.has("ImageURL"))
                {
                    ImageURL= user.getString("ImageURL");
                }
                if(user.has("CurrentLocation"))
                {
                    CurrentLocation=user.getString("CurrentLocation");
                }
                if (user.has("DeviceToken")) {
                    DeviceToken= user.getString("DeviceToken");
                }
                if(user.has("ResetPassword"))
                {
                    ResetPassword= user.getString("ResetPassword");
                }
                if(user.has("Role"))
                {
                    Role= user.getString("Role");
                }
                if(user.has("BadgeCount"))
                {
                    BadgeCount= user.getString("BadgeCount");
                }

            }

            editor.putInt("userId",userId);
            editor.putString("ResetPassword",ResetPassword);
            editor.putString("Role",Role);
            editor.putString("emailId",emailID);
            editor.putString("userName",userName);
            editor.putString("city",city);
            editor.putString("phoneNo",phoneNo);
            editor.putString("ImageURL",ImageURL);
            editor.putString("CurrentLocation",CurrentLocation);
            editor.putString("DeviceToken",DeviceToken);
            editor.putString("ImageURL",ImageURL);
            editor.putString("BadgeCount",BadgeCount);
            Log.e(TAG,"curentlocation"+prefs.getString("CurrentLocation",""));
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void setSharedPreferenceEmpty(SharedPreferences.Editor editor) {
        editor.putInt("userId",0);
        editor.putString("emailId","");
        editor.putString("userName","");
        editor.putString("city","");
        editor.putString("phoneNo","");
        editor.putString("ImageURL","");
        editor.apply();
    }
    public static void setCurrentloc(Context context,String city) {

      //  SharedPreferences prefs  = PreferenceManager.getDefaultSharedPreferences(context);
      //  SharedPreferences prefs= context.getSharedPreferences("bydefault",Context.MODE_PRIVATE);
       // SharedPreferences.Editor editor = prefs.edit();
        //editor.putString("city",city);
        //editor.apply();
        currentloc= city;
        Log.e(TAG," set current city" +city);


    }
    public static String getCurrentloc(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_SIGNIN_NAME,Context.MODE_PRIVATE);
       String city= prefs.getString("newCurrentcity","");
        return city;

        //  SharedPreferences prefs  = PreferenceManager.getDefaultSharedPreferences(context);
        //  SharedPreferences prefs= context.getSharedPreferences("bydefault",Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = prefs.edit();
        //editor.putString("city",city);
        //editor.apply();


    }






  /*  public static double[] getLatLong(Context context,String locationname)

    {
        Log.e(TAG,"locationname" + locationname);
        Geocoder coder = new Geocoder(context);
        double lat,lng;
        double[] lanlon = new double[2];

        if(!locationname.isEmpty())
        {

        List<Address> addresses;
        try {

            addresses = coder.getFromLocationName(locationname, 5);
            if (addresses == null) {
            }
            Address location = addresses.get(0);
             lat = location.getLatitude();
             lng = location.getLongitude();
            Log.e("Lat in utils",""+lat);
            Log.e("Lng  in Utils",""+lng);
            lanlon= new double[]{lat,lng};
            LatLng latLng = new LatLng(lat,lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

        } catch (IOException e) {
            e.printStackTrace();
        }}
        else
        {
            lat = 1.358920;
            lng = 103.937346;
            lanlon= new double[]{lat,lng};

        }
        return lanlon;

    }*/

    public static Location[] location = {null};
    public static Location getLatLong(Context context) {
        Location l = new Location("");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        l.setLatitude(Double.parseDouble(prefs.getString("user_lat","1.23")));
        l.setLongitude(Double.parseDouble(prefs.getString("user_long","103.23")));

        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location loc) {
                //your code here
                //ed.putString("city",location.getLatitude()+","+location.getLongitude());
                System.out.println("-------------------"+loc.toString());
                location[0] = loc;
                editor.putString("user_lat",loc.getLatitude()+"");
                editor.putString("user_long",loc.getLongitude()+"");
                editor.commit();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.v("i am in util loc", "--"+location[0]);
            return location[0];
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
        //Log.v("i am outside util loc", location[0].toString());
        return location[0] == null?l:location[0];
    }

    public static ArrayList<String> setSelectedNeeds(ArrayList<String> selectedNeeds, String need) {
        selectedNeeds.add(need);
        return selectedNeeds;

    }
    public  static void credentialsProvider(Context contextstate, File file){

        context=contextstate;
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:24d2e6f9-e90e-d-ab44-b430c5fe146b", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        setAmazonS3Client(credentialsProvider,context,file);
    }

    /**
     *  Create a AmazonS3Client constructor and pass the credentialsProvider.
     * @param credentialsProvider
     * @param context
     * @param file
     */
    public static void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider, Context context, File file){

        // Create an S3 client
          AmazonS3Client s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        setTransferUtility(s3,context,file);

    }

    public static void setTransferUtility(AmazonS3Client s3, Context context, File file){

       TransferUtility  transferUtility = new TransferUtility(s3, context);
        if(flagupload.equalsIgnoreCase("y")) {
            setFileToUpload(transferUtility, file, context);
        }
        else if(flagupload.equalsIgnoreCase("n")) {
            setFileToDownload(transferUtility, file);
        }


    }

    /**
     * This method is used to upload the file to S3 by using TransferUtility class
     */
    public static void setFileToUpload(TransferUtility transferUtility,File fileToUpload,Context context){

        TransferObserver transferObserver = transferUtility.upload(
                Bucket_name,     /* The bucket to upload to */
                MySharedPrefrence.getPrefrence(context).getString("emailId","")+".jpg",    /* The key for the uploaded object */
                fileToUpload       /* The file where the data to upload exists */
        );

        transferObserverListener(transferObserver);
    }

    /**
     *  This method is used to Download the file to S3 by using transferUtility class
     **/
    public static void setFileToDownload(TransferUtility transferUtility,File fileToDownload){

        TransferObserver transferObserver = transferUtility.download(
                Bucket_name,     /* The bucket to download from */
                MySharedPrefrence.getPrefrence(context).getString("emailId","")+".jpg"    /* The key for the uploaded object */
                ,    /* The key for the object to download */
                fileToDownload        /* The file to download the object to */
        );

        transferObserverListener(transferObserver);

    }

    /**
     * This is listener method of the TransferObserver
     * Within this listener method, we got status of uploading and downloading file,
     * to diaplay percentage of the part of file to be uploaded or downloaded to S3
     * It display error, when there is problem to upload and download file to S3.
     * @param transferObserver
     */

    public static void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+"");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                try {
                    int percentage = (int) (bytesCurrent / bytesTotal * 100);
                    Log.e("percentage",percentage +"");

                }
                catch (ArithmeticException e)
                {
                    Log.e("exception"," exception");
                  e.getMessage();
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
            }

        });
    }
 public static void uploadImagetoserver(Context activity, String userImage,String flaguploaddownload)
 {
     flagupload= flaguploaddownload;
     File file = new File(userImage);

     credentialsProvider(activity,file);


     // callback method to call the setTransferUtility method
     //setTransferUtility();

 }
    public static String getCurrentdate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        System.out.println("Currrent Date Time : "+formattedDate);
        return formattedDate;

    }
    public static String getselecteddate(Date date)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(date);
        System.out.println("selected Date Time : "+formattedDate);

        return formattedDate;
    }
    public static String getEndnmonth()
    {
        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastday= sdf.format(lastDayOfMonth);
        System.out.println("Today            : " + sdf.format(today));
        System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
        return lastday;

    }
    public static String getMonth(){
        Date today = new Date();


        DateFormat sdf = new SimpleDateFormat("MMMM");
        String month= sdf.format(today);
        return month;
    }
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        Log.e(TAG," base 64 path  "+imageEncoded);
        return imageEncoded;
    }


    public static void getLoginContinue(final Context context) {
       // View itemView = LayoutInflater.from(getContext()).inflate(R.layout.entertainment_list_row, parent, false);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout = inflater.inflate(R.layout.login_continue_dialog, null);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        dialog.show();

        final Button loginclick =(Button) alertLayout.findViewById(R.id.loginclick);
        final Button continueclcik = (Button)alertLayout.findViewById(R.id.continueclick);
        loginclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "login clicked", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(context, SignUpActivity.class);
                context.startActivity(intent);
                dialog.dismiss();


            }
        });
        continueclcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });




    }



}
