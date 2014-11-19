package com.example.anl2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;


public class EventActivity extends ActionBarActivity {
	
	private static final String tag = "Event";
	
	private EventDataSource eventDataSource;
	
	private Calendar calCaculator;
	private int year;
	private int month;
	private int day;
	private int hour;
	
	protected SharedPreferences sharedPref;
	protected SharedPreferences.Editor editor;
	
	private EditText event;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        
        SharedPreferences sharedPref	= getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor							= sharedPref.edit();
        calCaculator					= Calendar.getInstance();
        
        year	= sharedPref.getInt(getString(R.string.data_year), calCaculator.get(Calendar.YEAR));
        month	= sharedPref.getInt(getString(R.string.data_month), calCaculator.get(Calendar.MONTH));
        day		= sharedPref.getInt(getString(R.string.data_day), calCaculator.get(Calendar.DAY_OF_MONTH));
        hour	= sharedPref.getInt(getString(R.string.data_hour), (calCaculator.get(Calendar.HOUR_OF_DAY) + 1));
        
    	
		ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", day) + " " + ((hour > 12 ? hour - 12 : hour) + (hour > 12 ? "PM" : "AM")));
        
        eventDataSource = new EventDataSource(this);
    	Cursor cursor = eventDataSource.getEventInTime(year, month, day, hour);
    	cursor.moveToFirst();
    	Log.d(tag, DatabaseUtils.dumpCursorToString(cursor));
    	
    	
    	event = (EditText) findViewById(R.id.event);
    	
    	Button save = (Button) findViewById(R.id.save);
    	if (cursor.getCount() > 0) {
    		event.setText(cursor.getString(cursor.getColumnIndex(EventDataSource.C_CONTENT)));
    		
    		save.setTag(R.string.event_id, cursor.getInt(cursor.getColumnIndex(EventDataSource.C_ID)));
    	} else {
    		save.setTag(R.string.event_id, 0);
    	}
    }
    
    public void saveEvent(View view) {
    	int id = (Integer) view.getTag(R.string.event_id);
    	Log.d(tag, "" + id);
    	
    	if (id == 0) {
    		eventDataSource.addEvent(year, month, day, hour, event.getText().toString());
    	} else {
    		eventDataSource.updateEvent(id, event.getText().toString());
    	}
    	
    	Intent intent = new Intent(getBaseContext(), DayActivity.class);
        startActivity(intent);
    }

}