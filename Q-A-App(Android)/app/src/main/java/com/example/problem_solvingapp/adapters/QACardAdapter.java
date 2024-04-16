package com.example.problem_solvingapp.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.activities.QuestionDetailActivity;
import com.example.problem_solvingapp.data.Question;

import java.util.List;

public class QACardAdapter extends RecyclerView.Adapter<QACardAdapter.ViewHolder> {

    private List<Question> itemList;
    private OnItemClickListener listener;
    public QACardAdapter(List<Question> itemList,OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question item = itemList.get(position);
        holder.titleTextView.setText(item.title);
        holder.usernameTextView.setText(item.username);
        holder.likesTextView.setText(String.valueOf(item.approves));
        holder.viewsTextView.setText(String.valueOf(item.views));
        holder.commentsTextView.setText(String.valueOf(item.reviews));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setItemList(List<Question> newItemList) {
        this.itemList = newItemList;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView usernameTextView;
        TextView likesTextView;
        TextView viewsTextView;
        TextView commentsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            likesTextView = itemView.findViewById(R.id.likesLayout).findViewById(R.id.approvesTextView);
            viewsTextView = itemView.findViewById(R.id.viewsLayout).findViewById(R.id.viewsLayout);
            commentsTextView = itemView.findViewById(R.id.commentsLayout).findViewById(R.id.reviewsTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // Position is valid
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
