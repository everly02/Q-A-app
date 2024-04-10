package com.example.problem_solvingapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.problem_solvingapp.ApiServiceSingleton;
import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AskQuestionActivity extends AppCompatActivity {

    private EditText editTextQuestionTitle, editTextQuestionContent;
    private Button buttonSubmitQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        editTextQuestionTitle = findViewById(R.id.editTextQuestionTitle);
        editTextQuestionContent = findViewById(R.id.editTextQuestionContent);
        buttonSubmitQuestion = findViewById(R.id.buttonSubmitQuestion);

        buttonSubmitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuestion();
            }
        });
    }

    private void submitQuestion() {
        String title = editTextQuestionTitle.getText().toString().trim();
        String content = editTextQuestionContent.getText().toString().trim();

        if(title.isEmpty() || content.isEmpty()){
            Toast.makeText(this, "Title and content cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        AskQuestion askQuestion = new AskQuestion(title, content, 1); // 假设askerID = 1
        QuestionApiService service = ApiServiceSingleton.getApiService();
        Question ques = new Question( title ,content, 1);
        service.postQuestion(ques).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(AskQuestionActivity.this, "Question submitted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AskQuestionActivity.this, "Failed to submit question.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

