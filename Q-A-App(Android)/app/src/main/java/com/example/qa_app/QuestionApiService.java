package com.example.qa_app;

import com.example.qa_app.data.NewAnswer;
import com.example.qa_app.data.NewQuestion;
import com.example.qa_app.data.NewReview;
import com.example.qa_app.data.Question;
import com.example.qa_app.data.QuestionDetail;
import com.example.qa_app.data.QuestionResponse;
import com.example.qa_app.data.Tag;
import com.example.qa_app.data.answer_of_user;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QuestionApiService {
    @GET("/questions")
    Call<QuestionResponse> getAllQuestions();
    @GET("/users/{id}/questions")
    Call<QuestionResponse> getMyQuestions(@Path("id") int userId);

    @GET("/users/{id}/answers")
    Call<List<answer_of_user>> getMyAnswers(@Path("id") int userId);

    @GET("/questions/{id}")
    Call<QuestionDetail> getQuestionById(@Path("id") int id);

    // Add a new question
    @POST("/questions")
    Call<Void> addQuestion(@Body NewQuestion question);

    // Add a comment to a question
    @POST("/questions/{id}/comments")
    Call<Void> addCommentToQuestion(@Path("id") int questionId, @Body NewReview comment);

    // Add an answer to a question
    @POST("/questions/{id}/answers")
    Call<Void> addAnswer(@Path("id") int questionId, @Body NewAnswer answer);

    // Modify approves for a question
    @PATCH("/questions/{id}/approves/{action}")
    Call<Void> modifyQuestionApproves(@Path("id") int questionId, @Path("action") String action);

    // Modify approves for an answer (increment or decrement)
    @PATCH("/answers/{id}/approves/{action}")
    Call<Void> modifyAnswerApproves(@Path("id") int answerId, @Path("action") String action);

    // Delete a question
    @DELETE("/questions/{id}")
    Call<Void> deleteQuestion(@Path("id") int questionId);

    // Delete an answer
    @DELETE("/answers/{id}")
    Call<Void> deleteAnswer(@Path("id") int answerId);

    // Fetch questions by tags
    @GET("/questions/by-tags")
    Call<QuestionResponse> getQuestionsByTags(@Query("tags") String tags);

    @GET("/tags")
    Call<List<Tag>> getAllTags();

    @POST("questiontags")
    @FormUrlEncoded
    Call<ResponseBody> addQuestionTag(@Field("QuestionID") int questionID, @Field("TagID") int tagID);

    class LoginRequest {
        final String username;
        final String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    class LoginResponse {
        final int id;

        public int getid(){
            return id;
        }
        public LoginResponse(int id) {
            this.id = id;
        }
    }
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest body);


    @Multipart
    @POST("/register")
    Call<ResponseBody> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part avatar
    );


}


