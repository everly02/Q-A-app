package com.example.problem_solvingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionDetailActivity extends AppCompatActivity {

    private TextView questionTitle, questionDetail;
    private Button addAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        questionTitle = findViewById(R.id.questionTitle);
        questionDetail = findViewById(R.id.questionDetail);
        addAnswerButton = findViewById(R.id.addAnswerButton);

        // 获取传递过来的问题ID
        int questionID = getIntent().getIntExtra("questionID", -1);

        // 使用Retrofit获取问题详情
        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        apiService.getQuestionDetail(questionID).enqueue(new Callback<List<QuestionDetail>>() {
        @Override
        public void onResponse(@NonNull Call<List<QuestionDetail>> call, @NonNull Response<List<QuestionDetail>> response) {
            if(response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                // 假设你只对第一个对象感兴趣
                QuestionDetail detail = response.body().get(0);
                Log.d("QuestionDetailActivity", "Title: " + detail.getTitle() + ", Content: " + detail.getContent());

                questionTitle.setText(detail.getTitle());
                questionDetail.setText(detail.getContent());
            } else {
                Log.d("NOT_TRUE","IT IS NOT TRUE");
            }
        }

        @Override
        public void onFailure(@NonNull Call<List<QuestionDetail>> call, @NonNull Throwable t) {
            Log.d("FAIL","FAILURE");
        }
    });

        // 为“添加回答”按钮设置点击事件
        addAnswerButton.setOnClickListener(view -> {
            Intent intent = new Intent(QuestionDetailActivity.this, AddAnswerActivity.class);
            intent.putExtra("questionID", questionID); // 传递问题ID
            startActivity(intent);
        });
    }
}
