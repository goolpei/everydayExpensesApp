package com.example.everydayexpenses03.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everydayexpenses03.R;
import com.example.everydayexpenses03.adapter.ExpenseAdapter;
import com.example.everydayexpenses03.viewmodels.ExpenseViewModel;

import java.util.Calendar;

public class HistoryFragment extends Fragment {
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    private ExpenseViewModel mViewModel;
    private ExpenseAdapter adapter;
    private TextView tvHistoryDayTotal, tvSelectedDate;
    private CalendarView calendarView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Views
        tvHistoryDayTotal = view.findViewById(R.id.tvHistoryDayTotal);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        calendarView = view.findViewById(R.id.historyCalendar);
        RecyclerView recyclerView = view.findViewById(R.id.rvHistoryExpenses);

        // 2. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);

        // 3. Setup ViewModel (using requireActivity for sharing)
        mViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // 4. Handle Calendar Clicks
        calendarView.setOnDateChangeListener((cv, year, month, dayOfMonth) -> {
            updateHistoryUI(year, month, dayOfMonth);
        });

        // 5. Set Initial State (Show today's data by default)
        Calendar today = Calendar.getInstance();
        updateHistoryUI(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }
    private void updateHistoryUI(int year, int month, int day) {
        // Update the date label
        String dateString = String.format("%d/%d/%d", month + 1, day, year);
        tvSelectedDate.setText(dateString);

        // Remove old observers to prevent multiple "streams" of data
        mViewModel.getExpensesByDate(year, month, day).removeObservers(getViewLifecycleOwner());
        mViewModel.getTotalForSpecificDay(year, month, day).removeObservers(getViewLifecycleOwner());

        // Observe the list for the selected day
        mViewModel.getExpensesByDate(year, month, day).observe(getViewLifecycleOwner(), expenses -> {
            adapter.setExpenses(expenses);
        });

        // Observe the total for the selected day
        mViewModel.getTotalForSpecificDay(year, month, day).observe(getViewLifecycleOwner(), total -> {
            double value = (total != null) ? total : 0.0;
            tvHistoryDayTotal.setText(String.format("₱%.2f", value));
        });
    }
}
