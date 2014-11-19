package com.example.anl2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ListView;

import android.content.SharedPreferences;

public class DayActivity extends ActionBarActivity {

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

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        editor = sharedPref.edit();
        calCaculator = Calendar.getInstance();
        year = sharedPref.getInt(getString(R.string.data_year), calCaculator.get(Calendar.YEAR));
        month = sharedPref.getInt(getString(R.string.data_month), calCaculator.get(Calendar.MONTH));
        day = sharedPref.getInt(getString(R.string.data_day), calCaculator.get(Calendar.DAY_OF_MONTH));

        ListView eventList = (ListView) findViewById(R.id.date_event_list);
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

    private ArrayList<Event> generateData(int year, int month, int day) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);

        HashMap<Integer, String> data = databaseAdapter.getEventInDay(year, month, day);

        ArrayList<Event> row = new ArrayList<Event>();

        for (int i = 0; i <= 23; i++) {
            Event event = new Event();
            event.year = year;
            event.month = month;
            event.day = day;
            event.hour = i;
            event.event = data.containsKey(i) ? data.get(i) : "";

            row.add(event);
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