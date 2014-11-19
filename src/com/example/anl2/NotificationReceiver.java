package com.example.anl2;
 
import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
 
public class NotificationReceiver extends BroadcastReceiver {
      
    @Override
    @SuppressWarnings("deprecation")
    public void onReceive(Context context, Intent intent) {
    	// Check have event in next hour
    	EventDataSource eventDataSource = new EventDataSource(context);
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.HOUR_OF_DAY, 1);
    	Cursor cursor = eventDataSource.getEventInTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR));
    	if (cursor.getCount() > 0) {
    		cursor.moveToFirst();
    		String title	= "You have a event in " + calendar.get(Calendar.HOUR_OF_DAY) + ":00";
    		String content	= cursor.getString(cursor.getColumnIndex(EventDataSource.C_CONTENT));
        	
    		// Create notification
    		NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    		Notification note		= new Notification(R.drawable.ic_action_back, "Calendar", System.currentTimeMillis());

    		// This pending intent will open after notification click
    	    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, CalendarActivity.class), 0);
    				
    	    note.setLatestEventInfo(context, title, content, pendingIntent);

    		mgr.notify(0, note);
    	}
	}
}