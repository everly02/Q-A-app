package com.example.qa_app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qa_app.ApiServiceSingleton;
import com.example.qa_app.QuestionApiService;
import com.example.qa_app.R;
import com.example.qa_app.activities.AskQuestionActivity;
import com.example.qa_app.activities.QuestionDetailActivity;
import com.example.qa_app.adapters.QACardAdapter;
import com.example.qa_app.data.Question;
import com.example.qa_app.data.QuestionResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QAFragment extends Fragment {
    private RecyclerView recyclerView;
    private QACardAdapter adapter;
    private List<Question> itemList=new ArrayList<>();

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new QACardAdapter(new ArrayList<>(), position -> {
            Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra("questionID", itemList.get(position).getQuestionId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        fetchQuestions();
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
            return true;
        } else if (id == R.id.action_sort) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void fetchQuestions() {
        QuestionApiService service = ApiServiceSingleton.getApiService();
        Call<QuestionResponse> call = service.getAllQuestions();
        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(@NonNull Call<QuestionResponse> call, @NonNull Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Question> questions = response.body().getQuestions();
                    updateRecyclerView(questions);

                }else{
                    Log.d("QAFragment", "Response is successful but the body is null or empty.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuestionResponse> call, @NonNull Throwable t) {
                Log.e("QAFragment", "Failed to fetch questions", t);
            }
        });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerView(List<Question> questions) {

        itemList = questions;
        if (adapter == null) {
            Log.d("QAFragment", "Adapter was null, creating new adapter.");
            adapter = new QACardAdapter(itemList, position -> {
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                intent.putExtra("questionID", itemList.get(position).getQuestionId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setItemList(itemList);
            adapter.notifyDataSetChanged();
        }
    }
}



