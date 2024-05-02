package com.example.qa_app.adapters;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qa_app.adapters.OnItemClickListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.qa_app.R;
import com.example.qa_app.data.Answer;
import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private List<Answer> answerList;
    private Context context;

    private static OnItemClickListener listener;

    public AnswerAdapter(Context context, List<Answer> answerList) {
        this.context = context;
        this.answerList = answerList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Answer answer = answerList.get(position);
        holder.usernameTextView.setText(answer.getUsername());

        // Configuring WebView to display HTML content
        holder.contentWebView.setWebViewClient(new WebViewClient()); // Ensure redirects stay within the WebView
        holder.contentWebView.loadData(answer.getContent(), "text/html", "UTF-8");
        holder.approvesTextView.setText(String.valueOf(answer.getApproves()));
        holder.contentWebView.setBackgroundColor(Color.TRANSPARENT);
        holder.contentWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        holder.approve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:点赞回答
            }
        });
    }

    @Override
    public int getItemCount() {
        return answerList != null ? answerList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        WebView contentWebView;
        TextView approvesTextView;

        Button approve_button;
        public ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.answerer);
            contentWebView = itemView.findViewById(R.id.webView);
            approvesTextView = itemView.findViewById(R.id.answer_approves);
            approve_button = itemView.findViewById(R.id.approve_answer_btn);

        }
    }
}
