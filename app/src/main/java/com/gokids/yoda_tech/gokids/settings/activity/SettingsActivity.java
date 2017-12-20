package com.gokids.yoda_tech.gokids.settings.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.settings.adapter.KidsDetailsAdapter;
import com.gokids.yoda_tech.gokids.settings.model.KidsDetailBean;
import com.gokids.yoda_tech.gokids.settings.model.NeedBean;
import com.gokids.yoda_tech.gokids.utils.Constants;
import com.gokids.yoda_tech.gokids.utils.ImageFilePath;
import com.gokids.yoda_tech.gokids.utils.MySharedPrefrence;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SettingsActivity extends AppCompatActivity {


    ImageView mEditKids;
    private Toolbar toolbar;
    private EditText settins_username;
    private EditText settins_city;
    private EditText settings_mail;
    private EditText settings_currentpass;
    private EditText settings_newpass;
    private EditText settins_phone;
    public ArrayList<KidsDetailBean> kidslist = new ArrayList<>();
    public ArrayList<NeedBean> needslist = new ArrayList<>();
    private RecyclerView rv_child;
    private String TAG= getClass().getName();
    private LinearLayoutManager layoutmanager;
    private KidsDetailsAdapter adapter;
    private String flag;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;
    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 102;
    private String mCurrentPhotoPath;
    private Uri mCurrentPhotoUri;
    private String userImage;
    private String filePath;
    private ImageView profile_img;
    private String flagupload="n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        handleintent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupUi();
        setupKidsClick();
        //setupKidsDetails();
    }

    private void handleintent() {
        flag=getIntent().getStringExtra("flag");
        if(flag.equalsIgnoreCase("0"))
        {
            Intent addKidsActivity = new Intent(SettingsActivity.this, AddKidsActivity.class);
            addKidsActivity.putExtra("flag","0");
            startActivity(addKidsActivity);
            finish();
        }
        else {

        }
    }

    private void setupKidsDetails() {
        kidslist.clear();
        layoutmanager= new LinearLayoutManager(this);
        rv_child.setLayoutManager(layoutmanager);
        adapter = new KidsDetailsAdapter(SettingsActivity.this,kidslist);
        rv_child.setAdapter(adapter);
        String url= Urls.BASE_URL+"api/viewAllKids/email/"+ MySharedPrefrence.getPrefrence(SettingsActivity.this).getString("emailId","");
        Ion.with(SettingsActivity.this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null)
                        {
                            Log.e(TAG,"result"+result.toString());
                            String status = result.get("status").toString();
                            String message = result.get("message").toString();
                            JsonArray resultarray = result.getAsJsonArray("result");
                           // Log.e(TAG,"message"+message);
                            if(resultarray.size()>0)
                            {
                                try {
                                    JSONArray arrey= new JSONArray(resultarray.toString());
                                    for(int i= 0;i<arrey.length();i++)
                                    {
                                        KidsDetailBean bean = new KidsDetailBean();
                                        JSONObject obj= arrey.getJSONObject(i);
                                        String KidID= obj.getString("KidID");
                                        String Age= obj.getString("Age");
                                        String Gender= obj.getString("Gender");

                                        bean.setAge(Age);
                                        bean.setKidID(KidID);
                                        bean.setGender(Gender);
                                        bean.setNeedsbean(needslist);
                                        kidslist.add(bean);

                                    }
                                    adapter.notifyDataSetChanged();


                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }

                        }

                    }
                });

        
    }

    private void itemclick() {


    }

    private void setupUi() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_SIGNIN_NAME,MODE_PRIVATE);
        settins_username = (EditText) findViewById(R.id.settins_username);
        rv_child = (RecyclerView) findViewById(R.id.rv_child_details);
        mEditKids = (ImageView) findViewById(R.id.setttings_add_kid);
        settins_city = (EditText) findViewById(R.id.settins_city);
        settings_mail = (EditText) findViewById(R.id.settings_mail);
        settings_currentpass = (EditText) findViewById(R.id.settings_currentpass);
        settings_newpass = (EditText) findViewById(R.id.settings_newpass);
        settins_phone = (EditText) findViewById(R.id.settins_phone);
        profile_img = (ImageView) findViewById(R.id.settings_user_image);
        final Button update_profile= (Button)findViewById(R.id.update_profile);
        TextView change_profile_pic= (TextView)findViewById(R.id.change_profile_pic);
        change_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });

        settings_mail.setEnabled(false);
        settins_username.setEnabled(false);
        settins_city.setEnabled(false);
        settins_phone.setEnabled(false);
        settings_newpass.setEnabled(false);
        settings_currentpass.setEnabled(false);
        settins_username.setText(prefs.getString("userName",""));
        settins_city.setText(prefs.getString("city",""));
        settings_mail.setText(prefs.getString("emailId",""));
        settins_phone.setText(prefs.getString("phoneNo",""));
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(update_profile.getText().toString().equalsIgnoreCase("Change Profile"))
                {

                    settins_phone.setFocusable(true);
                    settins_city.setEnabled(true);
                    settins_city.setFocusable(true);

                    settins_phone.setEnabled(true);
                    settings_newpass.setEnabled(true);
                    settings_newpass.setFocusable(true);
                    settings_currentpass.setEnabled(true);
                    settings_currentpass.setFocusable(true);
                    update_profile.setText("Update Profile");
                }
                else if(update_profile.getText().toString().equalsIgnoreCase("Update Profile")) {
                        Log.e(TAG,"userid"+String.valueOf(MySharedPrefrence.getPrefrence(SettingsActivity.this).getInt("userId",0)));
                        if(!settings_currentpass.getText().toString().trim().isEmpty()) {
                            setUpUpdateProfile(settins_username.getText().toString(), settins_city.getText().toString(), settings_mail.getText().toString(), settings_currentpass.getText().toString(), settings_newpass.getText().toString(), settins_phone.getText().toString());
                            update_profile.setText("Change Profile");
                        }
                       else
                        {
                            Utils.getEnterPasswordAlert(SettingsActivity.this);
                           // Toast.makeText(SettingsActivity.this, "Please Enter Current password to update your profile", Toast.LENGTH_SHORT).show();
                        }



                }

            }
        });

    }

    private void setUpUpdateProfile(String username, String city, String mail, String currentpassword, String newpassword, String phone) {
        String urlupdateprofile;
        if(!newpassword.isEmpty())
        urlupdateprofile= Urls.BASE_URL+ "api/updateProfile/email/"+mail+"/userName/"+username+"/password/"+currentpassword+"/newPassword/"+newpassword+"/city/"+city+"/phoneNo/"+phone;
        else
            urlupdateprofile= Urls.BASE_URL+ "api/updateProfile/email/"+mail+"/userName/"+username+"/password/"+currentpassword+"/newPassword/-/city/"+city+"/phoneNo/"+phone;

        Log.e(TAG,"url to update"+urlupdateprofile);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, urlupdateprofile, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {

                            Log.e(TAG," i m there  in response "+ response.toString());
                            String status= response.get("status").toString();
                            String message= response.get("message").toString();
                            JSONArray res = response.getJSONArray("result");

                            if(res!=null) {
                                Utils.updateprefrences(SettingsActivity.this,res.toString());
                            }

                            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);





    }

    public void setupKidsClick() {

        mEditKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MySharedPrefrence.getPrefrence(SettingsActivity.this).getString("emailId","").trim().isEmpty()) {
                    Intent addKidsActivity = new Intent(SettingsActivity.this, AddKidsActivity.class);
                    addKidsActivity.putExtra("flag", "0");
                    startActivity(addKidsActivity);
                }
                else
                {
                    Toast.makeText(SettingsActivity.this, "Please Login or Continue", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void startGallery() {
        openGallery();
    }
    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    ActivityCompat.requestPermissions(SettingsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // No explanation needed, we can request the permission.
                    AlertDialog.Builder builderok = new AlertDialog.Builder(SettingsActivity.this);
                    builderok.setCancelable(false)
                            .setTitle("Allow " + getResources().getString(R.string.app_name) + " to access your gallery ? ")
                            .setMessage("Without this permission the app is unable to open Phone Gallery. Are you sure you want to deny this permission ?")
                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    goToSettings();
                                }
                            });
                    AlertDialog alertDialog = builderok.create();
                    alertDialog.show();
                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
        }


    }
    private void goToSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getPackageName());
        intent.setData(uri);
        startActivity(intent);
    }
    private void startCamera() {
        openCamera();
    }

    private void openCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this,
                        Manifest.permission.CAMERA)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    ActivityCompat.requestPermissions(SettingsActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
                } else {
                    // No explanation needed, we can request the permission.
                    AlertDialog.Builder builderok = new AlertDialog.Builder(SettingsActivity.this);
                    builderok.setCancelable(false)
                            .setTitle("Allow " + getResources().getString(R.string.app_name) + " to access your camera ")
                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    goToSettings();
                                }
                            });
                    AlertDialog alertDialog = builderok.create();
                    alertDialog.show();
                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                /*Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                intentForCapturingImage();
            }
        } else {
            intentForCapturingImage();
        }
    }
    public void intentForCapturingImage() {
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photoFile = null;
        if (takePictureIntent.resolveActivity(SettingsActivity.this.getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                mCurrentPhotoUri = Uri.fromFile(photoFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        } else {
            Toast.makeText(SettingsActivity.this, "Camera is not installed", Toast.LENGTH_SHORT).show();
        }
    }
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    public void setPic(ImageView mImageView, String mCurrentPhoto) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int targetW = displayMetrics.widthPixels;
        int targetH = displayMetrics.heightPixels;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhoto, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhoto, bmOptions);
        mImageView.setImageBitmap(bitmap);
        String path64= Utils.encodeTobase64(bitmap);
        flagupload="y";
        Utils.uploadImagetoserver(SettingsActivity.this,userImage,flagupload);
    }

    //  @Override
  /*  public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }*/



    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.e(TAG, "requestCode " + requestCode + "resultCode " + resultCode + "data " + data);
        if (requestCode == CAMERA_REQUEST && resultCode == SettingsActivity.this.RESULT_OK) {
            Log.d(TAG, "picture userImage in camera " + userImage);

            //Utils.uploadImagetoserver(SettingsActivity.this,userImage);

            if (!mCurrentPhotoPath.isEmpty()) {
                new ImageCompressionAsyncTask(false).execute(mCurrentPhotoPath);
                setPic(profile_img, mCurrentPhotoPath);
            }
        } else if (resultCode ==SettingsActivity.this. RESULT_CANCELED) {
            Log.e(TAG, "canceled");
        } else if (data.getAction() != null && data.getAction().equalsIgnoreCase("android.intent.action.VIEW") && requestCode == RESULT_LOAD_IMAGE && resultCode ==SettingsActivity.this. RESULT_OK && null != data) {
            Uri content = data.getData();

            userImage = ImageFilePath.getPath(SettingsActivity.this, content);


            Log.i(TAG, "Image File Path" + userImage);
            if (!userImage.isEmpty()) {
                Bitmap bitmap = Utils.getGalleryBitmap(userImage);
                profile_img.setImageBitmap(bitmap);
                String path64= Utils.encodeTobase64(bitmap);
                flagupload = "y";
                Utils.uploadImagetoserver(SettingsActivity.this,userImage,flagupload);
            }
            Log.e(TAG, "i m here");

        } else if (data.getFlags() == 0 && data.getAction() == null && requestCode == RESULT_LOAD_IMAGE && resultCode == SettingsActivity.this.RESULT_OK && null != data) {
            Log.d(TAG, "data"+ data+"gallery  here" + data.getAction() + "flag " + data.getFlags());
            Uri content = data.getData();
            userImage = ImageFilePath.getPath(SettingsActivity.this, content);
            //Utils.uploadImagetoserver(SettingsActivity.this,userImage);

            Log.i(TAG, "Image File Path" + userImage);
            if (!userImage.isEmpty()) {
                Bitmap bitmap = Utils.getGalleryBitmap(userImage);
                profile_img.setImageBitmap(bitmap);
                String path64= Utils.encodeTobase64(bitmap);
                flagupload="y";
                Utils.uploadImagetoserver(SettingsActivity.this,userImage,flagupload);
            }
        } else if (data.getAction() == null && requestCode == RESULT_LOAD_IMAGE && resultCode == SettingsActivity.this.RESULT_OK && null != data) {
            Log.d(TAG, "gallery  here" + data.getAction() + "flag " + data.getFlags());
            userImage = Utils.getAbsolutePath(SettingsActivity.this, data.getData());
           // Utils.uploadImagetoserver(SettingsActivity.this,userImage);
            Log.e(TAG, "selectedImagePath  " + userImage);
            if (!userImage.isEmpty()) {
                Bitmap bitmap = Utils.getGalleryBitmap(userImage);
                profile_img.setImageBitmap(bitmap);
                String path64= Utils.encodeTobase64(bitmap);
                flagupload="y";
                Utils.uploadImagetoserver(SettingsActivity.this,userImage,flagupload);

            }
        }

    }


    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery) {
            this.fromGallery = fromGallery;
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = compressImage(params[0]);
            return filePath;
        }

        public String compressImage(String imageUri) {
            String filePath = getRealPathFromURI(imageUri);
            Bitmap scaledBitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];
            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filename = getFilename();
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return filename;
        }
        public  int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
            return inSampleSize;
        }


        private String getRealPathFromURI(String contentURI) {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }
        }

        public String getFilename() {
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
            if (!file.exists()) {
                file.mkdirs();
            }
            String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
            return uriSting;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (fromGallery) {
                Bundle bundle = new Bundle();
                bundle.putString("FILE_PATH", result);
                // showDialog(1, bundle);
            } else {
                Log.e(TAG, "sending image ....");
                filePath = result;
            }
        }
    }
    private void updateImage() {
        AlertforOptions();

    }

    private void AlertforOptions() {
        final CharSequence[] items = {
                "Gellery", "Camera"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                // mDoneButton.setText(items[item]);
                if(item==0)
                {
                    startGallery();
                }
                else if(item==1)
                {
                    startCamera();

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        setupKidsDetails();

    }
}


