package com.example.anl2;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

public class NotificationService extends IntentService {

	public NotificationService() {
		super("NotificationService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Create alarm for crontab job
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		Intent myIntent				= new Intent(this, NotificationReceiver.class);
		PendingIntent alarmIntent	= PendingIntent.getBroadcast(this, 0, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 10, alarmIntent);
	}
}