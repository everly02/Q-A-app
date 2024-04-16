package com.example.problem_solvingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.problem_solvingapp.AskaiFragment;
import com.example.problem_solvingapp.LoginManager;
import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.fragments.ProfileFragment;
import com.example.problem_solvingapp.fragments.QAFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    public int LOGIN_CODE = -99;
    SharedPreferences sharedPreferences ;
    String param_username;
    int param_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        param_username = sharedPreferences.getString("username", null);
        param_id = sharedPreferences.getInt("id", -99);
        LoginManager loginManager = new LoginManager(this);

        if (!loginManager.tryAutoLogin()) {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivityForResult(intent, LOGIN_CODE);
        }
        else{
            setupBottomNavigation();
        }

    }
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.navigation_qa) {
                    selectedFragment = new QAFragment();
                } else if (item.getItemId() == R.id.navigation_ask_ai) {
                    selectedFragment = new AskaiFragment();
                } else if (item.getItemId() == R.id.navigation_profile) {
                    selectedFragment = ProfileFragment.newInstance(param_id,param_username);
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }

                return true;
            }

        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QAFragment()).commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_CODE) {
            if (resultCode == RESULT_OK) {
                setupBottomNavigation();
            } else {
                finish();
            }
        }
    }

}
