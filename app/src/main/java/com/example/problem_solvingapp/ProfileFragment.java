package com.example.problem_solvingapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private static final String USER_ID = "1";
    private static final String USERNAME = "SampleUser";

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView userIdView = view.findViewById(R.id.user_id);
        TextView userNameView = view.findViewById(R.id.user_name);
        Button logoutButton = view.findViewById(R.id.logout_button);

        userIdView.setText("User ID: " + USER_ID);
        userNameView.setText("Username: " + USERNAME);

        logoutButton.setOnClickListener(v -> {

        });

        return view;
    }
}
