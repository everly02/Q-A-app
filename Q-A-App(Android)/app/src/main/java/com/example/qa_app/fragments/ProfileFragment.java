package com.example.qa_app.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qa_app.R;
import com.example.qa_app.activities.LoginActivity;
import com.example.qa_app.adapters.UserContentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Context context = getActivity();
        Log.d("ProfileFragment", "Instance User ID: " + instance_user_id);
        assert context != null;

        TextView userNameView = view.findViewById(R.id.user_name);

        SharedPreferences sp = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(sp.getInt("ID",-777)==instance_user_id){
            Log.d("if equal","yes");
            Button logoutButton = view.findViewById(R.id.logout_button);

            logoutButton.setOnClickListener( view1-> {
                editor.remove("ID");
                editor.remove("username");
                editor.remove("password");
                editor.apply();
                Log.d("if erased","yes");
                Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                startActivity(intent);
                Log.d("go to login page","yes");
                getActivity().finish();
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
