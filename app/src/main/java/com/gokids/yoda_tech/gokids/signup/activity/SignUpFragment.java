package com.gokids.yoda_tech.gokids.signup.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gokids.yoda_tech.gokids.R;
import com.gokids.yoda_tech.gokids.settings.activity.AddKidsActivity;
import com.gokids.yoda_tech.gokids.utils.ImageFilePath;
import com.gokids.yoda_tech.gokids.utils.Urls;
import com.gokids.yoda_tech.gokids.utils.Utils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
   // public String signupurl= Urls.BASE_URL+ "api/signupByMail/email/:email/userName/:userName/password/:password/city/:city/phoneNo/:phoneNo";
    private EditText signup_city;
    private EditText signup_password;
    private EditText signup_phoneno;
    private EditText signup_username;
    private EditText signup_email;
    private Button btn_signup;
    private String userName;
    private String email;
    private String city;
    private String password;
    private String phoneNo;
    private String TAG = getClass().getName();
    private ImageView profile_img;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;
    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 102;
    private String mCurrentPhotoPath;
    private Uri mCurrentPhotoUri;
    private String userImage;
    private String filePath;
    private String flagupload="n";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        SetupSignup(rootView);
        return rootView;
    }

    private void SetupSignup(View rootView) {
        signup_city = rootView.findViewById(R.id.signup_city);
        profile_img = rootView.findViewById(R.id.profile_img);
        signup_email = rootView.findViewById(R.id.signup_email);
        signup_password = rootView.findViewById(R.id.signup_password);
        signup_phoneno = rootView.findViewById(R.id.signup_phoneno);
        signup_username = rootView.findViewById(R.id.signup_username);
        btn_signup = rootView.findViewById(R.id.btn_signup);
        city= String.valueOf(signup_city.getText());
        email= String.valueOf(signup_email.getText());
        password= String.valueOf(signup_password.getText());
        phoneNo= String.valueOf(signup_phoneno.getText());
        userName= String.valueOf(signup_username.getText());
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(signup_city.getText().toString().isEmpty()&& signup_email.getText().toString().isEmpty()&& signup_password.getText().toString().isEmpty()&& signup_phoneno.getText().toString().isEmpty()&& signup_username.getText().toString().isEmpty() )
                {
                    Log.e(TAG,"i m in if");
                    Toast.makeText(getActivity(), "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
                else {
                   Log.e(TAG,"i in else");

                   if(signup_password.getText().toString().length()>=8 )
                   {
                       if(Utils.isEmailValid(signup_email.getText().toString()))
                       {
                           apiCall(signup_city.getText().toString(), signup_email.getText().toString(), signup_password.getText().toString(), signup_phoneno.getText().toString(), signup_username.getText().toString());

                       }
                       else
                       {
                           Toast.makeText(getActivity(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
                       }
                   }
                   else {
                       Toast.makeText(getActivity(), "oops!!passowrd should have atleast 8 characters", Toast.LENGTH_SHORT).show();
                   }
                }


            }
        });

    }

    private void updateImage() {
        AlertforOptions();

    }

    private void AlertforOptions() {
        final CharSequence[] items = {
                "Gellery", "Camera"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        //ChooseGalleryOrCamera galleryOrCamera = new ChooseGalleryOrCamera();
       // galleryOrCamera.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.myCustomDialog);
       // galleryOrCamera.show(getActivity().getSupportFragmentManager(), "gorc");
       // AlertDialog ad= new AlertDialog()
    }

    private void apiCall(final String city, final String email, String password, final String phoneNo, final String userName) {
         final String signupurl= Urls.BASE_URL+ "api/signupByMail/email/" + email+"/userName/"+userName+"/password/"+password+"/city/"+city+"/phoneNo/"+phoneNo;
        Log.e(TAG," url " + signupurl);
        Ion.with(getActivity())
                .load(signupurl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null)
                        {
                            Log.e(TAG,"I m in api"+result.toString());
                            String status = result.get("status").toString().replaceAll("\"","");
                            String message = result.get("message").toString().replaceAll("\"","");
                            if(status.equalsIgnoreCase("200")) {
                                Utils.storeCredentials(getActivity(), userName, 0, email, city, phoneNo);

                                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                Utils.getEmailareadyexist(getActivity(),message);
                                Intent intent = new Intent(getActivity(), AddKidsActivity.class);
                                intent.putExtra("flag","0");
                                startActivity(intent);
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                getActivity().finish();


                            }
                           else
                           {
                               Utils.getEmailareadyexist(getContext(),"Email already exists");
                               //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                           }
                        }

                    }
                });


    }

    private void startGallery() {
        openGallery();
    }
    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // No explanation needed, we can request the permission.
                    AlertDialog.Builder builderok = new AlertDialog.Builder(getActivity());
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
        Uri uri = Uri.parse("package:" + getContext().getPackageName());
        intent.setData(uri);
        startActivity(intent);
    }
    private void startCamera() {
        openCamera();
    }

    private void openCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
                } else {
                    // No explanation needed, we can request the permission.
                    AlertDialog.Builder builderok = new AlertDialog.Builder(getActivity());
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
            Toast.makeText(getActivity(), "Camera is not installed", Toast.LENGTH_SHORT).show();
        }
    }
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
    }

  //  @Override
  /*  public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }*/



   public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.e(TAG, "requestCode " + requestCode + "resultCode " + resultCode + "data " + data);
        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
            Log.d(TAG, "picture userImage in camera " + userImage);
            flagupload="y";
            Utils.uploadImagetoserver(getActivity(),userImage,flagupload);

            if (!mCurrentPhotoPath.isEmpty()) {
                new ImageCompressionAsyncTask(false).execute(mCurrentPhotoPath);
                setPic(profile_img, mCurrentPhotoPath);
            }
        } else if (resultCode ==getActivity(). RESULT_CANCELED) {
            Log.e(TAG, "canceled");
        } else if (data.getAction() != null && data.getAction().equalsIgnoreCase("android.intent.action.VIEW") && requestCode == RESULT_LOAD_IMAGE && resultCode ==getActivity(). RESULT_OK && null != data) {
            Uri content = data.getData();
            userImage = ImageFilePath.getPath(getContext(), content);
            Utils.uploadImagetoserver(getActivity(),userImage,flagupload);

            Log.i(TAG, "Image File Path" + userImage);
            if (!userImage.isEmpty()) {
                Bitmap bitmap = Utils.getGalleryBitmap(userImage);
                profile_img.setImageBitmap(bitmap);
            }
            Log.e(TAG, "i m here");

        } else if (data.getFlags() == 0 && data.getAction() == null && requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Log.d(TAG, "data"+ data+"gallery  here" + data.getAction() + "flag " + data.getFlags());
            Uri content = data.getData();
            userImage = ImageFilePath.getPath(getContext(), content);
            flagupload="y";
            Utils.uploadImagetoserver(getActivity(),userImage,flagupload);

            Log.i(TAG, "Image File Path" + userImage);
            if (!userImage.isEmpty()) {
                Bitmap bitmap = Utils.getGalleryBitmap(userImage);
                profile_img.setImageBitmap(bitmap);
            }
        } else if (data.getAction() == null && requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Log.d(TAG, "gallery  here" + data.getAction() + "flag " + data.getFlags());
            userImage = Utils.getAbsolutePath(getActivity(), data.getData());
            flagupload="y";
            Utils.uploadImagetoserver(getActivity(),userImage,flagupload);
            Log.e(TAG, "selectedImagePath  " + userImage);
            if (!userImage.isEmpty()) {
                Bitmap bitmap = Utils.getGalleryBitmap(userImage);
                profile_img.setImageBitmap(bitmap);

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
            Cursor cursor = getContext().getContentResolver().query(contentUri, null, null, null, null);
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





    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @Override
    public void onResume() {
        super.onResume();
        if ( isExternalStorageWritable() ) {

            File appDirectory = new File( Environment.getExternalStorageDirectory() + "/MyGokidsAppFolder" );
            File logDirectory = new File( appDirectory + "/log" );
            File logFile = new File( logDirectory, "logcat" + System.currentTimeMillis() + ".txt" );

            // create app folder
            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }

            // create log folder
            if ( !logDirectory.exists() ) {
                logDirectory.mkdir();
            }

            // clear the previous logcat and then write the new one to the file
            try {
                Process process = Runtime.getRuntime().exec( "logcat -d");
                process = Runtime.getRuntime().exec( "logcat -f " + logFile );
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        } else if ( isExternalStorageReadable() ) {
            // only readable
        } else {
            // not accessible
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
