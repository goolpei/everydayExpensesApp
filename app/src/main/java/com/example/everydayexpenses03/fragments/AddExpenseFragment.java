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
import com.example.everydayexpenses03.viewmodels.AddExpenseViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddExpenseFragment extends BottomSheetDialogFragment {

    private AddExpenseViewModel mViewModel;

    public static AddExpenseFragment newInstance() {
        return new AddExpenseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the ViewModel here
        mViewModel = new ViewModelProvider(this).get(AddExpenseViewModel.class);

        // This is also the best place to find your buttons/text fields
        // Example: Button saveBtn = view.findViewById(R.id.save_button);
    }

}