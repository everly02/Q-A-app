package com.example.problem_solvingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.problem_solvingapp.ApiServiceSingleton;
import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.datas.QuestionDetail;

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

        int questionID = getIntent().getIntExtra("questionID", -1);

        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        apiService.getQuestionDetail(questionID).enqueue(new Callback<QuestionDetail>() {
            @Override
            public void onResponse(@NonNull Call<QuestionDetail> call, @NonNull Response<QuestionDetail> response) {
                if(response.isSuccessful() && response.body() != null ) {

                    QuestionDetail detail = response.body();
                    Log.d("QuestionDetailActivity", "Title: " + detail.getTitle() + ", Content: " + detail.getContent());

                    questionTitle.setText(detail.getTitle());
                    questionDetail.setText(detail.getContent());
                } else {
                    Log.d("NOT_TRUE","IT IS NOT TRUE");
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionDetail> call, @NonNull Throwable t) {
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
