package com.example.everydayexpenses03.fragments;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.everydayexpenses03.R;
import com.example.everydayexpenses03.viewmodels.HomeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private HomeViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        FloatingActionButton fab = view.findViewById(R.id.fabAddExpense);


        fab.setOnClickListener(v -> {
            AddExpenseFragment addExpenseSheet = new AddExpenseFragment();
            // Use getParentFragmentManager() when calling from a fragment
            addExpenseSheet.show(getParentFragmentManager(), "add_expense");
        });



    }

}