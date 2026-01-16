package com.example.everydayexpenses03.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.everydayexpenses03.R;
import com.example.everydayexpenses03.fragments.HomeFragment;
import com.example.everydayexpenses03.fragments.SummaryFragment;
import com.example.everydayexpenses03.fragments.HistoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    // We keep these instances so they aren't destroyed
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment summaryFragment = new SummaryFragment();
    private final Fragment historyFragment = new HistoryFragment();
    private Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 1. MUST set the content view first!
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);

        // 2. Initialize Views
        bottomNav = findViewById(R.id.bottomNav);

        // 3. Setup Fragments ONLY on the first launch
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, historyFragment, "3").hide(historyFragment)
                    .add(R.id.fragment_container, summaryFragment, "2").hide(summaryFragment)
                    .add(R.id.fragment_container, homeFragment, "1") // Home is visible by default
                    .setReorderingAllowed(true)
                    .commit();
        }

        // 4. Handle Edge-to-Edge Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // 5. Navigation Listener
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showFragment(homeFragment);
                return true;
            } else if (id == R.id.nav_summary) {
                showFragment(summaryFragment);
                return true;
            } else if (id == R.id.nav_history) {
                showFragment(historyFragment);
                return true;
            }
            return false;
        });
    }

    private void showFragment(Fragment fragment) {
        if (fragment != activeFragment) {
            getSupportFragmentManager().beginTransaction()
                    .hide(activeFragment)
                    .show(fragment)
                    .setReorderingAllowed(true)
                    .commit();
            activeFragment = fragment;
        }
    }
}