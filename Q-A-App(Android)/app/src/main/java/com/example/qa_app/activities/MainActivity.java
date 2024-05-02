package com.example.qa_app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.qa_app.AskaiFragment;
import com.example.qa_app.LoginCallback;
import com.example.qa_app.LoginManager;
import com.example.qa_app.R;
import com.example.qa_app.fragments.ProfileFragment;
import com.example.qa_app.fragments.QAFragment;
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
        param_id = sharedPreferences.getInt("ID", -99);
        LoginManager loginManager = new LoginManager(this);

        loginManager.tryAutoLogin(new LoginCallback() {
            @Override
            public void onLoginSuccess() {
                Toast.makeText(MainActivity.this, "当前用户："+param_username, Toast.LENGTH_SHORT).show();
                setupBottomNavigation();
            }

            @Override
            public void onLoginFailure() {
                Toast.makeText(MainActivity.this, "您需要登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Log.d("if trying to login","yes");

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


}
