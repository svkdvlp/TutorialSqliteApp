package com.svk.tutorialapplication.utils;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 05-05-2018.
 */

public class AppUtils {

    public static boolean isTimeValid(long timeInMillis) {
        Calendar currentTimeCal = Calendar.getInstance();
        return timeInMillis > currentTimeCal.getTimeInMillis();
    }

    public static void showToast(Context ctx, String str) {
        Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
    }

    public static String getformattedTimeString(long timeInMillis) {
        Date date = new Date(timeInMillis);
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateformat.format(date);
    }
}
