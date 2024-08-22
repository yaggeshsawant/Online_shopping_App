package com.madss.grocery;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
    
    private static final String BASE_URL = "https://madsssoftwaresolution.com/Online_Shopping_Website_Php/";



    public static String getBaseUrl() {
        return BASE_URL;
    }
    private static Retrofit retrofit = null;
    public static Retrofit getApiClient(Context context) {

        if (!isNetworkAvailable(context)) {
            // Show a toast indicating no internet connection
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();

            // Start NoInternetActivity
            Intent i = new Intent(context, NoInternetActivity.class);
            context.startActivity(i);
    
        }


        // Create a cache object
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);



        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // .header("Madss-KEY", "madss123");

        httpClient.addInterceptor(chain -> {
            Request originalRequest = chain.request();
            Request compressedRequest = originalRequest.newBuilder()
                    .header("Madss-KEY", "madss123")
                    .build();
            return chain.proceed(compressedRequest);
        });

        OkHttpClient client = httpClient.cache(cache).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }



    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
