package com.example.everydayexpenses03.fragments;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.everydayexpenses03.R;
import com.example.everydayexpenses03.adapter.ExpenseAdapter;
import com.example.everydayexpenses03.viewmodels.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private ExpenseViewModel mViewModel;
    private ExpenseAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = view.findViewById(R.id.rvTodayExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);

        mViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // 3. OBSERVE the data - This is where the magic happens!
        mViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            // Whenever the database changes, this code runs automatically
            adapter.setExpenses(expenses);
        });

        FloatingActionButton fab = view.findViewById(R.id.fabAddExpense);

        fab.setOnClickListener(v -> {
            AddExpenseFragment addExpenseSheet = new AddExpenseFragment();
            addExpenseSheet.show(getParentFragmentManager(), "add_expense");
        });



    }

}