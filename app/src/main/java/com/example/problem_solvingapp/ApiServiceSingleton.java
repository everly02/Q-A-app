package com.example.problem_solvingapp;


public class ApiServiceSingleton {
    private static QuestionApiService apiService = null;

    private ApiServiceSingleton() { }

    public static synchronized QuestionApiService getApiService() {
        if (apiService == null) {
            apiService = RetrofitClientInstance.getRetrofitInstance().create(QuestionApiService.class);
        }
        return apiService;
    }
}