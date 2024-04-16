package com.example.problem_solvingapp;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginManager {
    private static final String PREFERENCES_FILE = "LoginPrefs";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";

    private static final String PREF_ID = "ID";
    private Context context;

    public LoginManager(Context context) {
        this.context = context;
    }

     public void saveLoginCredentials(String username, String password,int id) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_ID,id);
        editor.putString(PREF_USERNAME, username);
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

    public boolean tryAutoLogin() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        String username = prefs.getString(PREF_USERNAME, null);
        String password = prefs.getString(PREF_PASSWORD, null);


        if (username != null && password != null) {

            return "correct_username".equals(username) && "correct_password".equals(password);
        }

        return false;
    }
}
