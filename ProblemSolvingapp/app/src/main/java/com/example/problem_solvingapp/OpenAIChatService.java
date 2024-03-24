package com.example.problem_solvingapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OpenAIChatService {
    @POST("/process-string")
    Call<AIResponse> sendMessage(@Body UserMessage message);
}

class UserMessage {
    String inputString;

    UserMessage(String inputString) {
        this.inputString = inputString;
    }
}

class AIResponse {
    String answer;
}
