package com.example.qa_app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qa_app.ApiServiceSingleton;
import com.example.qa_app.QuestionApiService;
import com.example.qa_app.R;
import com.example.qa_app.activities.QuestionDetailActivity;
import com.example.qa_app.adapters.QACardAdapter;

import com.example.qa_app.data.Question;
import com.example.qa_app.data.QuestionResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class userQuestionFragment extends Fragment {

    public userQuestionFragment() {
    }
    private List<Question> itemlist = new ArrayList<>();

    private static String arg_user_id;

    int instance_id;

    private QACardAdapter adapter;

    RecyclerView recyclerView;
    List<Question> my_questions = new ArrayList<>();
    public static userQuestionFragment newInstance(int id) {
        userQuestionFragment fragment = new userQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(arg_user_id,id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        menu.clear();
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
        recyclerView = view.findViewById(R.id.user_content_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        QuestionApiService service = ApiServiceSingleton.getApiService();
        Call<QuestionResponse> call = service.getMyQuestions(instance_id);
        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(@NonNull Call<QuestionResponse> call, @NonNull Response<QuestionResponse> response) {
                assert response.body() != null;
                itemlist = response.body().getQuestions();
                updateRecyclerView(itemlist);
            }

            @Override
            public void onFailure(@NonNull Call<QuestionResponse> call, @NonNull Throwable throwable) {
                Log.d("reponse result failure","failed");
            }
        });
        adapter = new QACardAdapter(itemlist,position -> {
            Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra("questionID", itemlist.get(position).getQuestionId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerView(List<Question> questions) {
        
        itemlist = questions;
        if (adapter == null) {
            Log.d("QAFragment", "Adapter was null, creating new adapter.");
            adapter = new QACardAdapter(itemlist, position -> {
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                intent.putExtra("questionID", itemlist.get(position).getQuestionId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setItemList(itemlist);
            adapter.notifyDataSetChanged();
        }
    }
}