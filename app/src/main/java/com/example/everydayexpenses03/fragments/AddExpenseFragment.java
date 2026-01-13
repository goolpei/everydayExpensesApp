package com.example.everydayexpenses03.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.everydayexpenses03.R;
import com.example.everydayexpenses03.data.Expense;
import com.example.everydayexpenses03.viewmodels.ExpenseViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddExpenseFragment extends BottomSheetDialogFragment {

    private ExpenseViewModel mViewModel;
    private EditText etAmount, etNote;
    private AutoCompleteTextView actvCategory;
    private Button btnSave;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // 1. Initialize the SHARED ViewModel
        mViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // 2. Bind the UI elements from your fragment_add_expense.xml
        etAmount = view.findViewById(R.id.etAmount); // Ensure these IDs match your XML
        etNote = view.findViewById(R.id.etNotes);
        btnSave = view.findViewById(R.id.btnSaveExpense);

        actvCategory = view.findViewById(R.id.actvCategory);

        // Setup the adapter
        String[] categories = getResources().getStringArray(R.array.expense_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, categories);
        actvCategory.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String amountStr = etAmount.getText().toString().trim();
        String category = actvCategory.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        // Simple Validation
        if (amountStr.isEmpty() || category.isEmpty()) {
            Toast.makeText(getContext(), "Please enter amount and category", Toast.LENGTH_SHORT).show();
            return;
        }
        category = category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase();

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid amount format", Toast.LENGTH_SHORT).show();
            return;
        }
        long currentTime = System.currentTimeMillis();

        // Create the Expense object (matching our lean Entity)
        Expense expense = new Expense(category, amount, note, currentTime);

        // Save to Database via ViewModel
        mViewModel.insert(expense);

        // Close the Bottom Sheet
        dismiss();

    }
}