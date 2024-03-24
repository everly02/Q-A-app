package com.example.problem_solvingapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


public class AskaiFragment extends Fragment {

    private ChatAdapter chatAdapter;
    private final List<String> messages = new ArrayList<>();
    private EditText messageInput;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_askai, container, false);
        RecyclerView chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        messageInput = view.findViewById(R.id.message_input);
        Button sendButton = view.findViewById(R.id.send_button);

        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sendButton.setOnClickListener(v -> {
            String userInput = messageInput.getText().toString().trim();
            if (!userInput.isEmpty()) {
                messages.add("User: " + userInput);
                chatAdapter.notifyItemInserted(messages.size() - 1);
                messageInput.setText("");

                // 发送消息到后端并获取AI响应
                sendMessageToBackend(userInput);
            }
        });

        return view;
    }

    private void sendMessageToBackend(String message) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://20.39.192.103:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenAIChatService service = retrofit.create(OpenAIChatService.class);
        Call<AIResponse> call = service.sendMessage(new UserMessage(message));
        call.enqueue(new Callback<AIResponse>() {
            @Override
            public void onResponse(@NonNull Call<AIResponse> call, @NonNull Response<AIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("response",response.body().answer);
                    requireActivity().runOnUiThread(() -> {
                        messages.add("AI: " + response.body().answer);
                        chatAdapter.notifyItemInserted(messages.size() - 1);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<AIResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
