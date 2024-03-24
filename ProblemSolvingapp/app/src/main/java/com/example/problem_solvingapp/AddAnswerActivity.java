package com.example.problem_solvingapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAnswerActivity extends AppCompatActivity {

    private WebView webView;
    private int questionID;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);

        questionID = getIntent().getIntExtra("questionID", -1);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/rich_editor.html");

        Button submitAnswerButton = findViewById(R.id.submitAnswerButton);
        submitAnswerButton.setOnClickListener(view -> {
            webView.evaluateJavascript("getEditorContent();", htmlContent -> {

                postAnswer(questionID, htmlContent);
            });
        });
    }

    private void postAnswer(int questionID, String answerContent) {
        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        Answer answer = new Answer(questionID,answerContent);
        apiService.postAnswer(questionID, answer).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
