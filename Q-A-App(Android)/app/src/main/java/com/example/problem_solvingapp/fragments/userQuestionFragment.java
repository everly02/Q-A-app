package com.example.problem_solvingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.problem_solvingapp.ApiServiceSingleton;
import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.activities.QuestionDetailActivity;
import com.example.problem_solvingapp.adapters.QACardAdapter;

import com.example.problem_solvingapp.data.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class userQuestionFragment extends Fragment {

    public userQuestionFragment() {
    }
    private List<Question> itemlist;

    private static String arg_user_id;

    int instance_id;

    public static userQuestionFragment newInstance(int id) {
        userQuestionFragment fragment = new userQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(arg_user_id,id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            instance_id = getArguments().getInt(arg_user_id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.user_content_recycler,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.user_content_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        QuestionApiService service = ApiServiceSingleton.getApiService();
        Call<List<Question>> call = service.getMyQuestions(instance_id);
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                itemlist = response.body();
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable throwable) {

            }
        });

        recyclerView.setAdapter(new QACardAdapter(itemlist,position -> {
            Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra("questionID", itemlist.get(position).getQuestionId());
            startActivity(intent);
        }));

        return view;
    }
}