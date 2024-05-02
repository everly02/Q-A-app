package com.example.qa_app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qa_app.ApiServiceSingleton;
import com.example.qa_app.QuestionApiService;
import com.example.qa_app.R;
import com.example.qa_app.adapters.AnswerAdapter;
import com.example.qa_app.adapters.Reviewadapter;
import com.example.qa_app.data.Answer;
import com.example.qa_app.data.NewReview;
import com.example.qa_app.data.QuestionDetail;
import com.example.qa_app.data.Review;
import com.example.qa_app.data.Tag;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class QuestionDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTitle, questionDetail;
    private Button addAnswerButton;
    int questionID ;
    QuestionDetail detail;
    List<Review> all_reviews = new ArrayList<>();
    Button add_review_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        addAnswerButton = findViewById(R.id.add_new_answer);
        questionID = getIntent().getIntExtra("questionID", -1);
        Log.d("ID TO QUERY",""+questionID);
        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        apiService.getQuestionById(questionID).enqueue(new Callback<QuestionDetail>() {
            @Override
            public void onResponse(@NonNull Call<QuestionDetail> call, @NonNull Response<QuestionDetail> response) {
                Log.d("if successful",""+response.isSuccessful());
                if(response.isSuccessful() && response.body() != null ) {

                    detail = response.body();
                    TextView questionTitle = findViewById(R.id.question_title);
                    WebView questionContent = findViewById(R.id.question_content);

                    Log.d("question content getted in detailed page",detail.getQuestionTitle());

                    questionTitle.setText(detail.getQuestionTitle());
                    questionContent.loadDataWithBaseURL(null, detail.getQuestionContent(), "text/html", "utf-8", null);

                    questionContent.setBackgroundColor(Color.TRANSPARENT);
                    questionContent.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null); // 为了防止滚动条等引起的渲染问题

                    add_review_btn = findViewById(R.id.btn_add_review);
                    add_review_btn.setOnClickListener(v -> {
                        showAddCommentDialog(QuestionDetailActivity.this);
                    });

                    //设定问题赞数
                    TextView ques_approves = findViewById(R.id.approvesTextView);
                    ques_approves.setText(String.valueOf(detail.getApproves()));
                    //设定问题浏览数
                    TextView ques_views = findViewById(R.id.viewsTextView);
                    ques_views.setText(String.valueOf(detail.getViews()));
                    //设评论数
                    all_reviews = detail.getReviews();
                    TextView ques_reviews = findViewById(R.id.reviewsTextView);
                    if (all_reviews != null) {
                        ques_reviews.setText(String.valueOf(all_reviews.size()));
                    } else {
                        ques_reviews.setText("0");
                    }
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

                    addAnswerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(QuestionDetailActivity.this, AddAnswerActivity.class);
                            intent.putExtra("questionID",questionID);
                            startActivity(intent);
                        }
                    });

                    // 标签
                    FlexboxLayout flexboxLayout = findViewById(R.id.tags_flexbox_layout);
                    List<Tag> flow_tags = detail.getTags();
                    if (flow_tags != null) {
                        for (Tag tag : flow_tags) {
                            View customView = LayoutInflater.from(QuestionDetailActivity.this).inflate(R.layout.tag_card, flexboxLayout, false);

                            TextView textView = customView.findViewById(R.id.viewsTextView);
                            textView.setText(tag.getName());

                            flexboxLayout.addView(customView);
                        }
                    }

// rv of reviews
                    RecyclerView recyclerViewReviews = findViewById(R.id.recyclerview_comments);
                    recyclerViewReviews.setLayoutManager(new LinearLayoutManager(QuestionDetailActivity.this));
                    DividerItemDecoration deco_reviews = new DividerItemDecoration(recyclerViewReviews.getContext(), DividerItemDecoration.VERTICAL);
                    recyclerViewReviews.addItemDecoration(deco_reviews);
                    List<Review> reviews = detail.getReviews();
                    if (reviews != null) {
                        Reviewadapter Radapter = new Reviewadapter(reviews);
                        recyclerViewReviews.setAdapter(Radapter);
                    } else {
                        recyclerViewReviews.setAdapter(new Reviewadapter(new ArrayList<Review>())); // 设置一个空的Review列表
                    }

// 回答rv
                    RecyclerView recyclerViewAnswers = findViewById(R.id.recyclerview_answers);
                    recyclerViewAnswers.setLayoutManager(new LinearLayoutManager(QuestionDetailActivity.this));
                    DividerItemDecoration decoration = new DividerItemDecoration(recyclerViewAnswers.getContext(), DividerItemDecoration.VERTICAL);
                    recyclerViewAnswers.addItemDecoration(decoration);
                    List<Answer> answers = detail.getAnswers();
                    if (answers != null) {
                        AnswerAdapter answerAdapter = new AnswerAdapter(QuestionDetailActivity.this,answers);
                        recyclerViewAnswers.setAdapter(answerAdapter);
                    } else {
                        recyclerViewAnswers.setAdapter(new AnswerAdapter(QuestionDetailActivity.this,new ArrayList<>()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionDetail> call, @NonNull Throwable t) {
                Log.d("FAIL","FAILURE");
            }
        });

    }
    private void showAddCommentDialog(Activity activity) {//显示添加评论dialog，包含提交评论逻辑
        // Inflate the custom layout.
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_comment, null);
        EditText editTextComment = view.findViewById(R.id.edittext_comment);

        // Create and show the dialog.
        new MaterialAlertDialogBuilder(activity)
                .setTitle("Add a Comment")
                .setView(view)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String comment = editTextComment.getText().toString();
                    if (!comment.isEmpty()) {
                        QuestionApiService apiservice = ApiServiceSingleton.getApiService();
                        apiservice.addCommentToQuestion(questionID,new NewReview(comment)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                Toast.makeText(activity, "succeeded", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable throwable) {
                                Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
    @Override
    public void onClick(View v) {

    }
}
