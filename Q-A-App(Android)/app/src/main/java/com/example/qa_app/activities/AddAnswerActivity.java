package com.example.qa_app.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qa_app.ApiServiceSingleton;
import com.example.qa_app.QuestionApiService;
import com.example.qa_app.R;
import com.example.qa_app.data.NewAnswer;
import com.example.qa_app.utils.RichEditorBase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAnswerActivity extends RichEditorBase {

    private int questionID;

    private EditText editText;

    public AddAnswerActivity() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questionID = getIntent().getIntExtra("questionID", -1);

        editText = findViewById(R.id.edittext_rich_text);
        Button submitAnswerButton = findViewById(R.id.submitAnswerButton);
        submitAnswerButton.setOnClickListener(view -> {
                postAnswer(questionID);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_answer;
    }


    private void postAnswer(int questionID) {
        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        Context context=this;
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        String content = editText.getText().toString().trim();
        if(content.isEmpty()){
            Toast.makeText(this, "content is empty!", Toast.LENGTH_SHORT).show();
        }
        
        NewAnswer new_ans = new NewAnswer(content, sharedPreferences.getInt("ID",-999));
        if(sharedPreferences.getInt("ID",-90)==-90){
            Toast.makeText(this, "id null", Toast.LENGTH_SHORT).show();
        }
        apiService.addAnswer(questionID, new_ans).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(AddAnswerActivity.this, "successfully added answer", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddAnswerActivity.this, "we met some problem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(AddAnswerActivity.this, "we met some problem", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
