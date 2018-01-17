package com.gokids.yoda_tech.gokids.utils;

import android.content.Context;
import android.util.Log;

import com.amazonaws.http.HttpResponse;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpParams;

import static java.net.Proxy.Type.HTTP;

/**
 * Created by Lenovo on 7/15/2017.
 */
public class Urls {
    public static String BASE_URL = "http://52.77.82.210/";

    public static String addKisdetails = "api/addDeleteKidDetail/email/:email/age/:age/gender/:gender/specialNeed/:specialNeed/kidID/:kidID";
//    public static String getDataWithMessage(String URL, Context mContext) {
//        DefaultHttpClient httpClient = null;
//
//
//        try {
//            // Setup a custom SSL Factory object which simply ignore the certificates validation and accept all type of self signed certificates
//            SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
//            sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//            // Enable HTTP parameters
//            HttpParams params = new BasicHttpParams();
//            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//            // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
//            SchemeRegistry registry = new SchemeRegistry();
//            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//            registry.register(new Scheme("https", sslFactory, 443));
//
//            // Create a new connection manager using the newly created registry and then create a new HTTP client using this connection manager
//            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//            httpClient = new DefaultHttpClient(ccm, params);
//            HttpPost httpPost = new HttpPost(URL);
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                return EntityUtils.toString(httpResponse.getEntity());
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            Log.e("Error", TAG + " " + e.getMessage());
//            httpClient.getConnectionManager().shutdown();
//            return null;
//        }
//    }


}
