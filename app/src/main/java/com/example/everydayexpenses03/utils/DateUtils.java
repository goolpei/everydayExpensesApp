package com.example.everydayexpenses03.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    // =========================================================================
    // 1. FORMATTING (For the UI)
    // =========================================================================

    public static String getFormattedToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d", Locale.getDefault());
        return sdf.format(new Date());
    }
    public static String formatDate(long timestamp) {
        // This format shows: Jan 24, 8:30 AM
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, h:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    // =========================================================================
    // 2. DAILY LOGIC (For Home Screen)
    // =========================================================================

    public static long getStartOfDay() {
        Calendar cal = Calendar.getInstance();
        resetTimeToStartOfDay(cal);
        return cal.getTimeInMillis();
    }

    public static long getTwoDaysAgoStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1); // Go back to "Yesterday"
        resetTimeToStartOfDay(cal);
        return cal.getTimeInMillis();
    }

    // =========================================================================
    // 3. PERIODIC LOGIC (For Summary Screen)
    // =========================================================================

    public static long getStartOfWeek() {
        Calendar cal = Calendar.getInstance();
        // Set to the first day of the current week (usually Sunday or Monday)
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        resetTimeToStartOfDay(cal);
        return cal.getTimeInMillis();
    }

    public static long getStartOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        resetTimeToStartOfDay(cal);
        return cal.getTimeInMillis();
    }

    // =========================================================================
    // 4. PRIVATE HELPERS (To reduce clutter)
    // =========================================================================

    private static void resetTimeToStartOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
}