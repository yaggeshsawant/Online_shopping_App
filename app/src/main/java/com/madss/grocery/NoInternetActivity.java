package com.madss.grocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NoInternetActivity extends AppCompatActivity {
    ProgressBar pgsBar;
    Button restBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        pgsBar = findViewById(R.id.pBar);
        restBtn = findViewById(R.id.retry_btn_id);

        restBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgsBar.setVisibility(v.VISIBLE);
                restBtn.setVisibility(v.GONE);
                checkInternetConnection(v);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pgsBar.setVisibility(v.GONE);
                        restBtn.setVisibility(v.VISIBLE);
                    }
                },2000);


            }
        });
    }

    public void checkInternetConnection(View view){

        new NoInternetActivity.CheckInternetConnectivityTask().execute();
    }

    public class CheckInternetConnectivityTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return isNetworkConnected();
        }

        @Override
        protected void onPostExecute(Boolean isNetworkConnected) {
            if (!isNetworkConnected) {
                //showNoInternetActivity();
            }
            else{
                finish();
            }
        }
    }

    private void showNoInternetActivity() {
        Toast.makeText(this, "NO INTERNET", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, NoInternetActivity.class);
        startActivity(i);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }




}