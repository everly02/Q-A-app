package com.example.problem_solvingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.problem_solvingapp.ApiServiceSingleton;
import com.example.problem_solvingapp.LoginManager;
import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;

    private final int register_result_code = -99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout usernameTextInputLayout = findViewById(R.id.usernameTextInputLayout);
        TextInputEditText usernameEditText = (TextInputEditText) usernameTextInputLayout.getEditText();

        TextInputLayout passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        TextInputEditText passwordEditText = (TextInputEditText) passwordTextInputLayout.getEditText();

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // 构造登录请求的body
            QuestionApiService.LoginRequest loginRequest = new QuestionApiService.LoginRequest(username, password);

            // 发起登录请求
            QuestionApiService apiService = ApiServiceSingleton.getApiService();
            apiService.login(loginRequest).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Integer myID = response.body();
                        LoginManager mng = new LoginManager(LoginActivity.this);
                        mng.saveLoginCredentials(username,password,myID);
                        setResult(RESULT_OK);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    // 处理请求失败的情况
                }
            });
        });
        MaterialButton registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent,register_code);
        });


    }
    int register_code;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == register_code){
            if(resultCode == RESULT_OK){
                //DO NOTHING
            }
        }
    }
}