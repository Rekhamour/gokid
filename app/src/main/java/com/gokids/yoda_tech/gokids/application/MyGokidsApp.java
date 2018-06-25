package com.gokids.yoda_tech.gokids.application;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class MyGokidsApp extends Application {

	/**
     * Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
	 */
	public void onCreate() {
		super.onCreate();

	/*	if ( isExternalStorageWritable() ) {

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
				Process process = Runtime.getRuntime().exec( "logcat -e");
				process = Runtime.getRuntime().exec( "logcat -e " + logFile );
			} catch ( IOException e ) {
				e.printStackTrace();
			}
 
		} else if ( isExternalStorageReadable() ) {
			// only readable
		} else {
			// not accessible
		}*/
	}
 
	/* Checks if external storage is available for read and write */
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
}
 