package com.example.problem_solvingapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.RetrofitClientInstance;
import com.example.problem_solvingapp.adapters.QACardAdapter;
import com.example.problem_solvingapp.data.Question;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QAFragment extends Fragment {
    private RecyclerView recyclerView;
    private QACardAdapter adapter;
    private List<Question> itemList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qa, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemList = new ArrayList<>();
        // TODO:
        adapter = new QACardAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.top_app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_filt) {
            // Handle the favorite action
            return true;
        } else if (id == R.id.action_sort) {
            // Handle the search action
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void fetchQuestions() {
        QuestionApiService service = RetrofitClientInstance.getRetrofitInstance().create(QuestionApiService.class);
        Call<List<Question>> call = service.getAllQuestions();
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateRecyclerView(response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {

            }
        });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerView(List<Question> questions) {

        QACardAdapter adapter = (QACardAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItemList(questions);
            adapter.notifyDataSetChanged();
        }
    }
}



