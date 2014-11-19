package com.example.anl2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import android.widget.ListView;

import android.content.SharedPreferences;


public class DayActivity extends ActionBarActivity {
	
	private static final String tag = "Day";
	
	private Calendar calCaculator;
	private int year;
	private int month;
	private int day;
	
	protected SharedPreferences sharedPref;
	protected SharedPreferences.Editor editor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        
        SharedPreferences sharedPref	= getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor							= sharedPref.edit();
        calCaculator					= Calendar.getInstance();
        
        year	= sharedPref.getInt(getString(R.string.data_year), calCaculator.get(Calendar.YEAR));
        month	= sharedPref.getInt(getString(R.string.data_month), calCaculator.get(Calendar.MONTH));
        day		= sharedPref.getInt(getString(R.string.data_day), calCaculator.get(Calendar.DAY_OF_MONTH));
        
        
		ListView eventList		= (ListView) findViewById(R.id.date_event_list);
		DayEventAdapter adapter = new DayEventAdapter(this, generateData(year, month, day));
		eventList.setAdapter(adapter);
		
		ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", day));
    }
    
    public void backCalendar(View view) {
    	Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }
    
    private ArrayList<DayEvent> generateData(int year, int month, int day) {
    	EventDataSource eventDataSource = new EventDataSource(this);
    	Cursor cursor = eventDataSource.getEventInDay(year, month, day);
    	//Log.d(tag, DatabaseUtils.dumpCursorToString(cursor));
    	
    	HashMap<Integer, String> data = new HashMap<Integer, String>();
    	
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				int hour = cursor.getInt(cursor.getColumnIndex(EventDataSource.C_HOUR));
				String content = cursor.getString(cursor.getColumnIndex(EventDataSource.C_CONTENT));
				if (!data.containsKey(hour)) {
					data.put(hour, content);
				} else {
					String oldContent = data.get(hour);
					data.put(hour, oldContent + content);
				}
				cursor.moveToNext();
			}
		}
		cursor.close();
    	
        ArrayList<DayEvent> row = new ArrayList<DayEvent>();
        
        for (int i = 1; i <= 24; i++) {
        	row.add(new DayEvent(year, month, day, i, data.containsKey(i) ? data.get(i) : ""));
        }
 
        return row;
    }
    
    public void eventActivity(View view) {
    	editor.putInt(getString(R.string.data_hour), (Integer) view.getTag(R.string.data_hour));
    	editor.commit();
    	
    	Intent intent = new Intent(getBaseContext(), EventActivity.class);
        startActivity(intent);
    }
}