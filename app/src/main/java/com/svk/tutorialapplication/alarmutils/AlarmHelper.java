package com.svk.tutorialapplication.alarmutils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.ALARM_SERVICE;
import static com.svk.tutorialapplication.utils.AppUtils.showToast;

public class AlarmHelper {

    public static final String KEY_JOB_TITLE = "jobTitle";
    public static final String KEY_JOB_id = "jobId";

    public static void setJobAlarm(Context ctx, long selectedTimeMillis, int reqCode, String titleStr) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);

        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, selectedTimeMillis,  getAlarmPendingIntent(ctx,reqCode, titleStr));
        }catch (Exception e){
            e.printStackTrace();
            showToast(ctx, "Alarm set error");
        }
    }

    public static void deleteJobAlarm(Context ctx, int reqCode, String titleStr) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        try {
            alarmManager.cancel(getAlarmPendingIntent(ctx, reqCode, titleStr));
        }catch (Exception e){
            e.printStackTrace();
            showToast(ctx, "Alarm deletion error");
        }
    }

    private static PendingIntent getAlarmPendingIntent(Context ctx, int reqCode, String titleStr) {
        Intent myIntent = new Intent(ctx, AlarmReceiver.class);
        myIntent.putExtra(KEY_JOB_TITLE,titleStr);
        myIntent.putExtra(KEY_JOB_id,reqCode);

        return  PendingIntent.getBroadcast(ctx, reqCode, myIntent, 0);
    }
}
