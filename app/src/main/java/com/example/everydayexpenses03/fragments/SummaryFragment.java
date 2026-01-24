package com.example.everydayexpenses03.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.everydayexpenses03.R;
import com.example.everydayexpenses03.viewmodels.ExpenseViewModel;

public class SummaryFragment extends Fragment {


    private ExpenseViewModel mViewModel;
    private TextView tvWeeklyTotal, tvMonthlyTotal, tvDailyAverage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvWeeklyTotal = view.findViewById(R.id.tvWeeklyTotal);
        tvMonthlyTotal = view.findViewById(R.id.tvMonthlyTotal);
        tvDailyAverage = view.findViewById(R.id.tvDailyAverage);

        mViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        setupObservers();

    }
    private void setupObservers() {
        // Observe Weekly Total
        mViewModel.getWeeklyTotal().observe(getViewLifecycleOwner(), total -> {
            updateText(tvWeeklyTotal, total);
        });

        // Observe Monthly Total (We need to add this to ViewModel - see below)
        mViewModel.getMonthlyTotal().observe(getViewLifecycleOwner(), total -> {
            updateText(tvMonthlyTotal, total);
        });

        // Observe Daily Average
        mViewModel.getDailyAverage().observe(getViewLifecycleOwner(), average -> {
            updateText(tvDailyAverage, average);
        });
    }
    private void updateText(TextView textView, Double value) {
        if (textView == null) return;
        double displayValue = (value != null) ? value : 0.0;
        textView.setText(String.format("₱%.2f", displayValue));
    }
}
