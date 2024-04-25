package com.example.qa_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.qa_app.fragments.userAnswerFragment;
import com.example.qa_app.fragments.userQuestionFragment;

public class UserContentAdapter extends FragmentStateAdapter{
    private int param_id;
    public UserContentAdapter(@NonNull Fragment fragment, int user_id) {
        super(fragment);
        param_id = user_id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return userQuestionFragment.newInstance(param_id);
            case 1:
                return new userAnswerFragment();
            default:
                return  new userQuestionFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
