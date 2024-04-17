package com.example.problem_solvingapp.adapters;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.data.answer_of_user;

import java.util.ArrayList;
import java.util.List;

public class UserAllAnswerAdapter extends RecyclerView.Adapter<UserAllAnswerAdapter.ViewHolder> {
    private List<answer_of_user> itemlist;

    private OnItemClickListener listener;

    public UserAllAnswerAdapter(List<answer_of_user> itemlist,OnItemClickListener listener){
        this.itemlist = itemlist;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_answer_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        answer_of_user item = itemlist.get(position);
        holder.qtitle.setText(item.getQuestionTitle());
        String pre = item.getContent();
        holder.ans_content.setText(Html.fromHtml(pre));
        holder.ans_content.setMaxLines(1);
        holder.ans_content.setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView qtitle;
        TextView ans_content;

        public ViewHolder(View itemView){
            super(itemView);
            qtitle = itemView.findViewById(R.id.user_answer_question);
            ans_content = itemView.findViewById(R.id.user_answer);
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
