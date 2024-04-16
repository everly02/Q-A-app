package com.example.problem_solvingapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.problem_solvingapp.ApiServiceSingleton;
import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.data.NewAnswer;
import com.example.problem_solvingapp.data.NewQuestion;
import com.example.problem_solvingapp.utils.RichEditorBase;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAnswerActivity extends RichEditorBase {

    private WebView webView;
    private int questionID;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);

        questionID = getIntent().getIntExtra("questionID", -1);


        Button submitAnswerButton = findViewById(R.id.submitAnswerButton);
        submitAnswerButton.setOnClickListener(view -> {
            webView.evaluateJavascript("getEditorContent();", htmlContent -> {

                postAnswer(questionID, htmlContent);
            });
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_answer;
    }

    private void postAnswer(int questionID, String answerContent) {
        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        Context context=this;
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        NewAnswer answer = new NewAnswer(answerContent,sharedPreferences.getInt("ID",-999));
        apiService.addAnswer(questionID, answer).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}
