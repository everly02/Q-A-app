package com.example.qa_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Log.d("about to store id = ",""+id);
        editor.putInt(PREF_ID,id);

        editor.putString(PREF_USERNAME, username);
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
         Log.d("stored id:", ""+prefs.getInt(PREF_ID,-4));
    }

    public void tryAutoLogin(final LoginCallback callback) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        String username = prefs.getString(PREF_USERNAME, null);
        String password = prefs.getString(PREF_PASSWORD, null);
        if (username == null || password == null) {
            Log.d("LoginManager", "No saved credentials, triggering login failure.");
            callback.onLoginFailure();
            return;
        }

        QuestionApiService.LoginRequest loginRequest = new QuestionApiService.LoginRequest(username, password);
        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        apiService.login(loginRequest).enqueue(new Callback<QuestionApiService.LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<QuestionApiService.LoginResponse> call, Response<QuestionApiService.LoginResponse> response) {
                assert response.body() != null;
                if(response.body().getid()>=0){
                    Log.d("if reached tryautologin onresponse","true");
                    callback.onLoginSuccess();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionApiService.LoginResponse> call, Throwable throwable) {
                    callback.onLoginFailure();
            }
        });
    }
}
