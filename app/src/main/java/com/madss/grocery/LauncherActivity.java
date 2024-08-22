package com.madss.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private static int AUDIO_TIME_OUT = 500;
    // Hooks
    View first, second, third, fourth, fifth, sixth;
    TextView slogan;
    ImageView a;
    // Animations
    Context context;
    Animation topAnimation, bottomAnimation, middleAnimation;
    SessionManager sessionManager = null;
    RetrofitApiInterface retrofitApiInterface;
    MediaPlayer mediaPlayer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        retrofitApiInterface = RetrofitApiClient.getApiClient(getApplicationContext()).create(RetrofitApiInterface.class);
        sessionManager = new SessionManager(LauncherActivity.this);

        // Hooks
//        first = findViewById(R.id.circle1);
//        second = findViewById(R.id.circle2);
//        third = findViewById(R.id.circle3);
/*        fourth = findViewById(R.id.circle4);
        fifth = findViewById(R.id.circle5);
        sixth = findViewById(R.id.circle6);*/
        a = findViewById(R.id.a);
        slogan = findViewById(R.id.tagLine);
        mediaPlayer = MediaPlayer.create(this, R.raw.splash_sound);
        // Animation Calls
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);

        // Setting Animations to the elements of Splash Screen
//        first.setAnimation(topAnimation);
//        second.setAnimation(topAnimation);
//        third.setAnimation(topAnimation);
      /*fourth.setAnimation(topAnimation);
        fifth.setAnimation(topAnimation);
        sixth.setAnimation(topAnimaation);*/
        a.setAnimation(middleAnimation);
        slogan.setAnimation(bottomAnimation);

        // Splash Screen Code to call new Activity after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                changeIntent(DashBoardActivity.class, null);
            }
        }, SPLASH_TIME_OUT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start playing audio
                mediaPlayer.start();


            }
        }, AUDIO_TIME_OUT);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void changeIntent(Class<? extends Activity> activityClass, Bundle bundle) {
        Intent intent = new Intent(LauncherActivity.this, activityClass);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        startActivity(intent);
        finish();
    }

}
