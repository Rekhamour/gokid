package com.gokids.yoda_tech.gokidsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lenovo on 7/26/2017.
 */
public class MySharedPrefrence {
   static Context context;
  static   SharedPreferences preferences;

    public static SharedPreferences getPrefrence(Context context)
    {

         preferences= context.getSharedPreferences(Constants.SHARED_SIGNIN_NAME,Context.MODE_PRIVATE);
        return preferences;

    }


}
