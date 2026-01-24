package com.example.everydayexpenses03.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.everydayexpenses03.R;
import com.example.everydayexpenses03.data.Expense;
import com.example.everydayexpenses03.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder> {

    private List<Expense> expenses = new ArrayList<>();

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
        Expense current = expenses.get(position);
        holder.tvCategory.setText(current.getCategory());
        holder.tvAmount.setText(String.format("₱%.2f", current.getAmount()));
        holder.tvNote.setText(current.getNote());

        // Using your DateUtils here!
        holder.tvDate.setText(DateUtils.formatDate(current.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<Expense> newExpenses) {
        // Calculate the difference between the old list and the new list
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return expenses.size(); }

            @Override
            public int getNewListSize() { return newExpenses.size(); }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                // Check if it's the same entry in the database
                return expenses.get(oldItemPosition).getId() ==
                        newExpenses.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                // Check if the amount or note changed
                Expense oldExp = expenses.get(oldItemPosition);
                Expense newExp = newExpenses.get(newItemPosition);

                return oldExp.getAmount() == newExp.getAmount() &&
                        oldExp.getCategory().equals(newExp.getCategory()) &&
                        oldExp.getTimestamp() == newExp.getTimestamp() &&
                        oldExp.getNote().equals(newExp.getNote());
            }
        });

        this.expenses = newExpenses;
        result.dispatchUpdatesTo(this); // This animates the change smoothly!
    }

    class ExpenseHolder extends RecyclerView.ViewHolder {
        private TextView tvCategory, tvAmount, tvDate, tvNote;

        public ExpenseHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNote = itemView.findViewById(R.id.tvNote);
        }
    }
    public Expense getExpenseAt(int position) {
        return expenses.get(position);
    }
}