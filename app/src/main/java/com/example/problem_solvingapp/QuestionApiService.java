package com.example.problem_solvingapp;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuestionApiService {
    @GET("/questions")
    Call<List<li_item>> getQuestions();

    @POST("/ask")
    Call<ResponseBody> postQuestion(@Body Question question);

    @GET("/question/{questionID}")
    Call<List<QuestionDetail>> getQuestionDetail(@Path("questionID") int questionID);

    @POST("/answer/{questionID}")
    Call<ResponseBody> postAnswer(@Path("questionID") int questionID, @Body Answer answer);
}


