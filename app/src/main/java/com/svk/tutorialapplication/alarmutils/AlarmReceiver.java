package com.svk.tutorialapplication.alarmutils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.svk.tutorialapplication.AddUpdateTodoActivity;
import com.svk.tutorialapplication.MainActivity;
import com.svk.tutorialapplication.R;
import com.svk.tutorialapplication.databaseutils.ToDoDatabaseHelper;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by SvK on 05-05-2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = AlarmReceiver.class.getSimpleName();

    private ToDoDatabaseHelper mToDoDatabaseHelper;

    private String CHANNEL_ID = "simpleId";

    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String jobTitle = intent.getStringExtra(AlarmHelper.KEY_JOB_TITLE);
        int jobId = intent.getIntExtra(AlarmHelper.KEY_JOB_id,-11);


        if (jobTitle != null && jobId !=-11) {

            //Pending Intent
            Intent mainintent = new Intent(context, MainActivity.class);
            mainintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, mainintent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Notifiction
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentText(jobTitle)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis());

            // Set the Channel ID for Android O.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID); // Channel ID
            }

            //notify
            notificationManager.notify(jobId,builder.build());

            //delete record from db
            deleteJobRecord(context,jobId);
        }else{
            Log.e(TAG, "onReceive: " + "Insufficient values for firing a alarm");
        }
    }

    private void deleteJobRecord(Context ctx, int jobId) {
        mToDoDatabaseHelper = ToDoDatabaseHelper.getInstance(ctx.getApplicationContext());
        int rows_effected = mToDoDatabaseHelper.deleteJobItem(jobId);
        if(rows_effected > 0){
            Toast.makeText(ctx, "Job Completed", Toast.LENGTH_SHORT).show();
        }
    }
}
