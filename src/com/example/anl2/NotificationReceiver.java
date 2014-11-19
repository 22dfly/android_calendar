package com.example.anl2;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    @SuppressWarnings("deprecation")
    public void onReceive(Context context, Intent intent) {
        // Check have event in next hour
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Event eventInTime = databaseAdapter.getEventInTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY));
        if (eventInTime.id != 0 && eventInTime.dismiss == 0) {
            String title = "You have a event in " + Event.getTime(calendar.get(Calendar.HOUR_OF_DAY), false) + ":00";
            String content = eventInTime.event;

            // Create notification
            NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification note = new Notification(R.drawable.ic_launcher, "Calendar", System.currentTimeMillis());

            // This pending intent will open after notification click
            Intent notificationIntent = new Intent(context, NotificationActivity.class);
            notificationIntent.putExtra("EVENT_ID", eventInTime.id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            note.setLatestEventInfo(context, title, content, pendingIntent);
            mgr.notify(R.string.notification_id, note);
        }
    }
}