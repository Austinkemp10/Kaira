package com.example.android.portfolio.helpers;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               Reminder.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This is a helper class used to create
 *                                                  notification reminders after a given interval.
 * ===============================================================================================*/
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.android.portfolio.R;

public class Reminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyJob")
                .setSmallIcon(R.drawable.ic_baseline_work_24)
                .setContentTitle("Job Reminder")
                .setContentText("Check on your job application for " + intent.getStringExtra("COMPANY_NAME"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }

}
