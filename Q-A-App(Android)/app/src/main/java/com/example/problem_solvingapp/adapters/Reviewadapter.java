package com.example.problem_solvingapp.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.data.Review;

import java.util.List;

public class Reviewadapter extends RecyclerView.Adapter<Reviewadapter.ReviewViewHolder>{
    private List<Review> ReviewList;

    public Reviewadapter(List<Review> ReviewList) {
        this.ReviewList = ReviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewcontent, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review comment = ReviewList.get(position);
        holder.textViewComment.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return ReviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView textViewComment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(R.id.review_content);
        }
    }
}
