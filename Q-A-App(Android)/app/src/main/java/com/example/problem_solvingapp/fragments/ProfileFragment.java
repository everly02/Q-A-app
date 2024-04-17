package com.example.problem_solvingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.problem_solvingapp.ApiServiceSingleton;
import com.example.problem_solvingapp.QuestionApiService;
import com.example.problem_solvingapp.R;
import com.example.problem_solvingapp.activities.LoginActivity;
import com.example.problem_solvingapp.activities.QuestionDetailActivity;
import com.example.problem_solvingapp.adapters.QACardAdapter;
import com.example.problem_solvingapp.adapters.UserContentAdapter;
import com.example.problem_solvingapp.data.Answer;
import com.example.problem_solvingapp.data.Question;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final String  ARG_USER_ID = "user id";
    private static final String ARG_USER_NAME = "user name";

    private int instance_user_id;
    private String instance_username;
    public ProfileFragment() {
        //empty
    }
    int LOGIN_CODE = -999;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            instance_user_id = getArguments().getInt(ARG_USER_ID);
            instance_username = getArguments().getString(ARG_USER_NAME);
        }
    }
    public static ProfileFragment newInstance(int userId,String username){
        ProfileFragment ProfileFragement_instance = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID,userId);
        args.putString(ARG_USER_NAME,username);
        ProfileFragement_instance.setArguments(args);
        return ProfileFragement_instance;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Context context = getActivity();

        assert context != null;

        TextView userNameView = view.findViewById(R.id.user_name);

        SharedPreferences sp = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        if(sp.getInt("ID",-999)==instance_user_id){
            Button logoutButton = view.findViewById(R.id.logout_button);
            SharedPreferences.Editor editor = sp.edit();
            logoutButton.setOnClickListener(v -> {
                editor.remove("ID");
                editor.remove("username");
                editor.remove("password");
                editor.apply();
                Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                startActivityForResult(intent, LOGIN_CODE);
            });
        }
        TabLayout UserTabLayout = view.findViewById(R.id.profile_tabLayout);
        ViewPager2 viewpager= view.findViewById(R.id.profile_viewPager);
        viewpager.setAdapter(new UserContentAdapter(this,instance_user_id));

        new TabLayoutMediator(UserTabLayout, viewpager,
                (tab, position) -> tab.setText(position == 0 ? "问题" : "解答")).attach();



        userNameView.setText("Username: " + instance_username);

        return view;
    }

}
