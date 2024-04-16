package com.example.problem_solvingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.problem_solvingapp.ApiServiceSingleton;
import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.adapters.Reviewadapter;
import com.example.problem_solvingapp.data.QuestionDetail;
import com.example.problem_solvingapp.data.Tag;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class QuestionDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTitle, questionDetail;
    private Button addAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        int questionID = getIntent().getIntExtra("questionID", -1);

        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        apiService.getQuestionById(questionID).enqueue(new Callback<QuestionDetail>() {
            @Override
            public void onResponse(@NonNull Call<QuestionDetail> call, @NonNull Response<QuestionDetail> response) {
                if(response.isSuccessful() && response.body() != null ) {

                    QuestionDetail detail = response.body();
                    TextView questionTitle = findViewById(R.id.question_title);
                    WebView questionContent = findViewById(R.id.question_content);
                    questionTitle.setText(detail.getQuestion().getTitle());
                    questionContent.loadDataWithBaseURL(null, detail.getQuestion().getContent(), "text/html", "utf-8", null);
                    //设定问题赞数
                    TextView ques_approves = findViewById(R.id.approvesTextView);
                    ques_approves.setText(detail.getQuestion().getApproves());
                    //设定问题浏览数
                    TextView ques_views = findViewById(R.id.viewsTextView);
                    ques_views.setText(detail.getQuestion().getViews());
                    //设评论数
                    TextView ques_reviews = findViewById(R.id.reviewsTextView);
                    ques_reviews.setText(detail.getReviews().size());
                    //设定赞同问题
                    Button act_approve = findViewById(R.id.action_approve);
                    act_approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            apiService.modifyQuestionApproves(questionID,"increment");
                        }
                    });
                    Button act_disapprove = findViewById(R.id.action_disapprove);
                    act_disapprove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            apiService.modifyQuestionApproves(questionID,"decrement");
                        }
                    });
                    //设定标签flowboxlayout
                    FlexboxLayout flexboxLayout = findViewById(R.id.tags_flexbox_layout);
                    List<Tag> flow_tags = detail.getTags();
                    for (Tag tag : flow_tags) {
                        View customView = LayoutInflater.from(QuestionDetailActivity.this).inflate(R.layout.tag_card, flexboxLayout, false);

                        TextView textView = customView.findViewById(R.id.viewsTextView);
                        textView.setText(tag.getName());

                        flexboxLayout.addView(customView);

                    }
                    //设置reviews recyclview
                    RecyclerView recyclerViewReviews = findViewById(R.id.recyclerview_comments);
                    recyclerViewReviews.setLayoutManager(new LinearLayoutManager(QuestionDetailActivity.this));

                    DividerItemDecoration deco_reviews = new DividerItemDecoration(recyclerViewReviews.getContext(), DividerItemDecoration.VERTICAL);

                    Reviewadapter Radapter = new Reviewadapter(detail.getReviews());
                    recyclerViewReviews.setAdapter(Radapter);
                    //设置回答recyclview
                    RecyclerView recyclerViewAnswers = findViewById(R.id.recyclerview_answers);
                    recyclerViewAnswers.setLayoutManager(new LinearLayoutManager(QuestionDetailActivity.this));
                    DividerItemDecoration decoration = new DividerItemDecoration(recyclerViewAnswers.getContext(), DividerItemDecoration.VERTICAL);

                    recyclerViewAnswers.addItemDecoration(decoration);

                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionDetail> call, @NonNull Throwable t) {
                Log.d("FAIL","FAILURE");
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
