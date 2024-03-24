package com.example.problem_solvingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {
    private final List<li_item> questions;
    private final Context context;

    public QuestionsAdapter(Context context, List<li_item> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
        public void onBindViewHolder(@NonNull QuestionsAdapter.QuestionViewHolder holder, int position) {
            li_item question = questions.get(position);
            holder.title.setText(question.getTitle());
            // 设置点击监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 创建Intent并启动QuestionDetailActivity，传递问题ID
                    Intent intent = new Intent(context, QuestionDetailActivity.class);
                    intent.putExtra("questionID", question.getQuestionID()); // 使用getQuestionID()方法获取问题ID
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }

        public static class QuestionViewHolder extends RecyclerView.ViewHolder {
            TextView title;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.questionTitle);
        }
    }
}