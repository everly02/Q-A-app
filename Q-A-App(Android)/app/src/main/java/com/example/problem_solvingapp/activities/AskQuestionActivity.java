package com.example.problem_solvingapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.problem_solvingapp.ApiServiceSingleton;
import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.data.NewQuestion;
import com.example.problem_solvingapp.utils.RichEditorBase;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AskQuestionActivity extends RichEditorBase {

    private TextInputEditText titleInput;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        titleInput = findViewById(R.id.title_input);
        editText = findViewById(R.id.edittext_rich_text);

        findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuestion();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    private void submitQuestion() {
        String title = Objects.requireNonNull(titleInput.getText()).toString().trim();
        String content = editText.getText().toString().trim();

        if(title.isEmpty() || content.isEmpty()){
            Toast.makeText(this, "Title and content cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        QuestionApiService service = ApiServiceSingleton.getApiService();
        Context context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        NewQuestion newques = new NewQuestion(sharedPreferences.getInt("ID",-999),title,content);
        Call<Void> call = service.addQuestion(newques);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(AskQuestionActivity.this, "Question submitted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AskQuestionActivity.this, "Failed to submit question.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

