package com.digital.dhanbadbasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.digital.dhanbadbasket.fragments.HomeFragment;
import com.digital.dhanbadbasket.fragments.OrdersFragment;
import com.digital.dhanbadbasket.fragments.ProfileFragment;
import com.digital.dhanbadbasket.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this, WelcomeScreen.class);
            startActivity(intent);
            finish();
        }
        fragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment.newInstance()).commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = HomeFragment.newInstance();
            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = HomeFragment.newInstance();
                    break;
                case R.id.grocery:
                    selectedFragment = SearchFragment.newInstance();
                    break;
                case R.id.orders:
                    selectedFragment = OrdersFragment.newInstance();
                    break;
                case R.id.account:
                    selectedFragment = ProfileFragment.newInstance();
                    break;
            }
            MainActivity.this.fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_content, selectedFragment)
                    .commit();
            return true;
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
           return;
        });
    }
}