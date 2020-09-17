package com.digital.dhanbadbasket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.digital.dhanbadbasket.R;
import com.digital.dhanbadbasket.Adapters.SliderAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeScreen extends AppCompatActivity {

    List<Integer> icon;
    List<String> iconName;
    ViewPager viewPager;
    TabLayout indicator;
    Button location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_screen);
        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        location = findViewById(R.id.location);

        //hello

        icon = new ArrayList<>();

        icon.add(R.drawable.slider1);
        icon.add(R.drawable.slider2);
        icon.add(R.drawable.slider3);

        iconName = new ArrayList<>();
        iconName.add("Safe and quick delivery");
        iconName.add("Order groceries online.");
        iconName.add("Fresh vegetables in your city.");

        viewPager.setAdapter(new SliderAdapter(WelcomeScreen.this, icon, iconName));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
                intent.putExtra("start","start");
                startActivity(intent);
            }
        });

//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                Intent intent = new Intent(WelcomeScreen.this, Login.class);
////                startActivity(intent);
//
//            }
//        });
    }
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            WelcomeScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < icon.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}


