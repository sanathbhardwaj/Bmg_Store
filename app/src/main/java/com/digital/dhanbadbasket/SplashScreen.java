package com.digital.dhanbadbasket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private Handler mhandler;
    private Runnable mrunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mrunnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }
        };
        mhandler = new Handler();
        mhandler.postDelayed(mrunnable,4000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mhandler!=null && mhandler!=null)
        {
            mhandler.removeCallbacks(mrunnable);
        }
    }
}
