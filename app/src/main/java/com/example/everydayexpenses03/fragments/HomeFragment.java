package com.example.everydayexpenses03.fragments;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.everydayexpenses03.R;
import com.example.everydayexpenses03.adapter.ExpenseAdapter;
import com.example.everydayexpenses03.data.Expense;
import com.example.everydayexpenses03.utils.DateUtils;
import com.example.everydayexpenses03.viewmodels.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

        TextView tvToday = view.findViewById(R.id.tvTodaysTotal);
        TextView tvDate = view.findViewById(R.id.tvDate);

        RecyclerView recyclerView = view.findViewById(R.id.rvTodayExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);

        tvDate.setText(DateUtils.getFormattedToday());

        mViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        mViewModel.getTodaysTotal().observe(getViewLifecycleOwner(), total -> {
            tvToday.setText(String.format("₱%.2f", total != null ? total : 0.0));
        });

        // 3. OBSERVE the data - This is where the magic happens!
        mViewModel.getRecentExpenses().observe(getViewLifecycleOwner(), expenses -> {
            adapter.setExpenses(expenses);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // CORRECTED: Use getBindingAdapterPosition()
                int position = viewHolder.getBindingAdapterPosition();

                // Safety Check: Sometimes animation delays make position return NO_POSITION (-1)
                if (position != RecyclerView.NO_POSITION) {
                    Expense expenseToDelete = adapter.getExpenseAt(position);
                    mViewModel.delete(expenseToDelete);

                    Snackbar.make(recyclerView, "Expense deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", v -> mViewModel.insert(expenseToDelete))
                            .show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        FloatingActionButton fab = view.findViewById(R.id.fabAddExpense);

        fab.setOnClickListener(v -> {
            AddExpenseFragment addExpenseSheet = new AddExpenseFragment();
            addExpenseSheet.show(getParentFragmentManager(), "add_expense");
        });



    }

}