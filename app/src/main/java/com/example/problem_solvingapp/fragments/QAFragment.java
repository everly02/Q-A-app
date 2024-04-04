package com.example.problem_solvingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.problem_solvingapp.ApiServiceSingleton;
import com.example.problem_solvingapp.activities.AskQuestionActivity;
import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.adapters.QuestionsAdapter;
import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.datas.li_item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QAFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qa, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_ask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AskQuestionActivity.class);
                startActivity(intent);
            }
        });
        fetchQuestions();

        return view;
    }
    private void showQuestions(List<li_item> questions) {
        RecyclerView recyclerView = requireView().findViewById(R.id.questionsRecyclerView);
        QuestionsAdapter adapter = new QuestionsAdapter(getContext(), questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }
    public void fetchQuestions() {
        QuestionApiService apiService = ApiServiceSingleton.getApiService();
        apiService.getQuestions().enqueue(new Callback<List<li_item>>() {
            @Override
            public void onResponse(@NonNull Call<List<li_item>> call, @NonNull Response<List<li_item>> response) {
                if (response.isSuccessful()) {

                    List<li_item> questions = new ArrayList<>();

                    assert response.body() != null;
                    for (li_item item : response.body()) {
                        questions.add(item);
                        Log.d("QuestionTitle", "Title: " + item.getTitle()); // 同时打印标题到日志
                    }
                    showQuestions(response.body());
                    for (li_item question : questions) {
                        Log.d("id", "id:"+question.getQuestionID());
                        Log.d("QuestionTitle", "Title: " + question.getTitle());
                    }
                } else {
                    Log.e("QuestionFetchError", "Failed to fetch questions");

                }
            }


            @Override
            public void onFailure(Call<List<li_item>> call, Throwable t) {
                // 网络错误处理
                Log.e("QuestionList", "Network error", t);
            }
        });
    }

}
