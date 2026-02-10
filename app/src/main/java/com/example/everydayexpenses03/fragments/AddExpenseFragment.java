package com.example.everydayexpenses03.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddExpenseFragment extends BottomSheetDialogFragment {

    private ExpenseViewModel mViewModel;
    private EditText etNote;
    private TextInputEditText etAmount;
    private AutoCompleteTextView actvCategory;
    private Button btnSave;


    private TextInputLayout amountLayout;
    private TextInputLayout categoryLayout;

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

        amountLayout = view.findViewById(R.id.tilAmount);
        categoryLayout = view.findViewById(R.id.tilCategory);

        setupErrorWatcher(amountLayout, etAmount);
        setupErrorWatcher(categoryLayout, actvCategory);

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

        // 1. Validate Category first (it's a simple string check)
        if (category.isEmpty()) {
            categoryLayout.setError("Please select a category");
            return;
        }

        // 2. Validate Amount
        double amount = 0;
        try {
            if (amountStr.isEmpty()) {
                amountLayout.setError("Please enter amount");
                return;
            }
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) { // Handles zero and negative numbers in one go
                amountLayout.setError("Amount must be greater than zero");
                return;
            }
        } catch (NumberFormatException e) {
            amountLayout.setError("Invalid number format");
            return;
        }

        // 3. Normalization (Only happens if validation passes)
        category = category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase();

        // 4. Save
        mViewModel.insert(new Expense(category, amount, note, System.currentTimeMillis()));
        dismiss();
    }

    private void setupErrorWatcher(TextInputLayout layout, EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // As soon as the user types something, clear the error
                if (s.length() > 0) {
                    layout.setError(null);
                    layout.setErrorEnabled(false); // Optional: collapses the error space
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }
}