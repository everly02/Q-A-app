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
import com.example.problem_solvingapp.adapters.UserAllAnswerAdapter;
import com.example.problem_solvingapp.data.Answer;
import com.example.problem_solvingapp.data.Question;
import com.example.problem_solvingapp.data.answer_of_user;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class userAnswerFragment extends Fragment {

    public userAnswerFragment(){}
    private List<answer_of_user> itemlist;

    private static String arg_user_id;
    int instance_id;

    public static userAnswerFragment newInstance(int id){
        userAnswerFragment fragment = new userAnswerFragment();
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
        View view= inflater.inflate(R.layout.user_content_recycler,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.user_content_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        QuestionApiService service = ApiServiceSingleton.getApiService();
        Call<List<answer_of_user>> call = service.getMyAnswers(instance_id);
        call.enqueue(new Callback<List<answer_of_user>>() {
            @Override
            public void onResponse(Call<List<answer_of_user>> call, Response<List<answer_of_user>> response) {
                itemlist = response.body();
            }

            @Override
            public void onFailure(Call<List<answer_of_user>> call, Throwable throwable) {

            }
        });
        recyclerView.setAdapter(new UserAllAnswerAdapter(itemlist,position -> {
            Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra("questionID", itemlist.get(position).getqid());
            startActivity(intent);
        } ));
        return view;
    }
}