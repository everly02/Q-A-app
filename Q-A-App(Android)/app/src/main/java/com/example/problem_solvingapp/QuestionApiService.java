package com.example.problem_solvingapp;

import com.example.problem_solvingapp.data.NewQuestion;
import com.example.problem_solvingapp.data.NewReview;
import com.example.problem_solvingapp.data.Question;
import com.example.problem_solvingapp.data.QuestionDetail;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QuestionApiService {
    @GET("/api/questions")
    Call<List<Question>> getAllQuestions();

    // Get a specific question by ID
    @GET("/api/questions/{id}")
    Call<QuestionDetail> getQuestionById(@Path("id") int id);

    // Add a new question
    @POST("/api/questions")
    Call<Void> addQuestion(@Body NewQuestion question);

    // Add a comment to a question
    @POST("/api/questions/{id}/comments")
    Call<Void> addCommentToQuestion(@Path("id") int questionId, @Body NewReview comment);

    // Add an answer to a question
    @POST("/api/questions/{id}/answers")
    Call<Void> addAnswer(@Path("id") int questionId, @Body NewReview answer);

    // Modify approves for a question
    @PATCH("/api/questions/{id}/approves/{action}")
    Call<Void> modifyQuestionApproves(@Path("id") int questionId, @Path("action") String action);

    // Modify approves for an answer (increment or decrement)
    @PATCH("/api/answers/{id}/approves/{action}")
    Call<Void> modifyAnswerApproves(@Path("id") int answerId, @Path("action") String action);

    // Delete a question
    @DELETE("/api/questions/{id}")
    Call<Void> deleteQuestion(@Path("id") int questionId);

    // Delete an answer
    @DELETE("/api/answers/{id}")
    Call<Void> deleteAnswer(@Path("id") int answerId);

    // Fetch questions by tags
    @GET("/api/questions/by-tags")
    Call<List<Question>> getQuestionsByTags(@Query("tags") String tags);
}


