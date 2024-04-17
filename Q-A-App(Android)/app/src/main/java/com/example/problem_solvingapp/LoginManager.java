package com.example.problem_solvingapp;

import android.content.Context;
import android.content.SharedPreferences;

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
        editor.putInt(PREF_ID,id);
        editor.putString(PREF_USERNAME, username);
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

    public void tryAutoLogin(final LoginCallback callback) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        String username = prefs.getString(PREF_USERNAME, null);
        String password = prefs.getString(PREF_PASSWORD, null);
        QuestionApiService.LoginRequest loginRequest = new QuestionApiService.LoginRequest(username, password);
        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        apiService.login(loginRequest).enqueue(new Callback<QuestionApiService.LoginResponse>() {
            @Override
            public void onResponse(Call<QuestionApiService.LoginResponse> call, Response<QuestionApiService.LoginResponse> response) {
                if(response.body().getid()>=0){
                    callback.onLoginSuccess();
                }
            }

            @Override
            public void onFailure(Call<QuestionApiService.LoginResponse> call, Throwable throwable) {
                    callback.onLoginFailure();
            }
        });
    }
}
