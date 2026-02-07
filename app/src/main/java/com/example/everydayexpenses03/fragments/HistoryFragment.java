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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everydayexpenses03.R;
import com.example.everydayexpenses03.adapter.ExpenseAdapter;
import com.example.everydayexpenses03.data.Expense;
import com.example.everydayexpenses03.utils.DateUtils;
import com.example.everydayexpenses03.viewmodels.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

        // Set up Observers ONCE.
        // We observe the "filtered" variables directly.
        mViewModel.filteredHistoryExpenses.observe(getViewLifecycleOwner(), expenses -> {
            // DiffUtil will now handle the updates perfectly!
            adapter.setExpenses(expenses);
        });

        mViewModel.filteredHistoryTotal.observe(getViewLifecycleOwner(), total -> {
            tvHistoryDayTotal.setText(String.format("₱%.2f", total != null ? total : 0.0));
        });

        // Calendar Event
        calendarView.setOnDateChangeListener((cv, year, month, dayOfMonth) -> {
            updateDate(year, month, dayOfMonth);
        });
        // Set Initial Date (Today)
        Calendar today = Calendar.getInstance();
        updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    Expense expenseToDelete = adapter.getExpenseAt(position);

                    // Delete from Database
                    mViewModel.delete(expenseToDelete);

                    // Show Snackbar on the Root View of the fragment
                    Snackbar.make(view, "Deleted from history", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", v -> mViewModel.insert(expenseToDelete))
                            .show();
                }
            }
        }).attachToRecyclerView(recyclerView);




        FloatingActionButton fabToday = view.findViewById(R.id.fabToday);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // dy > 0 means the user is scrolling DOWN
                if (dy > 0 && fabToday.isShown()) {
                    fabToday.hide();
                }
                // dy < 0 means the user is scrolling UP
                else if (dy < 0 && !fabToday.isShown()) {
                    fabToday.show();
                }
            }
        });

        view.findViewById(R.id.fabToday).setOnClickListener(v -> {
            long todayMillis = today.getTimeInMillis();

            // 1. Move the CalendarView visually
            calendarView.setDate(todayMillis, true, true);

            // 2. Trigger the data update
            updateDate(
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
            );
        });



    }

    private void updateDate(int year, int month, int day) {
        // Update the UI label
        tvSelectedDate.setText(String.format("%d/%d/%d", month + 1, day, year));

        // Convert date to timestamps using your DateUtils
        long start = DateUtils.getStartOfSpecificDay(year, month, day);
        long end = DateUtils.getEndOfSpecificDay(year, month, day);

        // Just push the new dates to the ViewModel
        mViewModel.setHistoryDate(start, end);
    }
}
