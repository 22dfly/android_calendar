package com.example.anl2;

import java.util.Calendar;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


import android.widget.EditText;
import android.widget.Button;


public class EventActivity extends ActionBarActivity {

    private DatabaseAdapter databaseAdapter;

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

        SharedPreferences sharedPref    = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor                          = sharedPref.edit();
        calCaculator                    = Calendar.getInstance();
        
        year    = sharedPref.getInt(getString(R.string.data_year), calCaculator.get(Calendar.YEAR));
        month   = sharedPref.getInt(getString(R.string.data_month), calCaculator.get(Calendar.MONTH));
        day     = sharedPref.getInt(getString(R.string.data_day), calCaculator.get(Calendar.DAY_OF_MONTH));
        hour    = sharedPref.getInt(getString(R.string.data_hour), (calCaculator.get(Calendar.HOUR_OF_DAY) + 1));

		ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", day) + " " + Event.getTime(hour, false));
        
        databaseAdapter = new DatabaseAdapter(this);
        Event eventInTime = databaseAdapter.getEventInTime(year, month, day, hour);

        event = (EditText) findViewById(R.id.event);

        Button save     = (Button) findViewById(R.id.save);
        Button delete   = (Button) findViewById(R.id.delete);

        if (eventInTime.id != 0) {
            save.setTag(R.string.event_id, Integer.toString(eventInTime.id));
            delete.setTag(R.string.event_id, Integer.toString(eventInTime.id));
            event.setText(eventInTime.event);
        } else {
            save.setTag(R.string.event_id, "0");
            delete.setEnabled(false);
        }
    }

    public void saveEvent(View view) {
        String id = (String) view.getTag(R.string.event_id);

        if (id == "0") {
            databaseAdapter.addEvent(year, month, day, hour, event.getText().toString());
        } else {
            databaseAdapter.updateEvent(id, event.getText().toString());
        }

        Intent intent = new Intent(getBaseContext(), DayActivity.class);
        startActivity(intent);
    }

    public void deleteEvent(View view) {
        String id = (String) view.getTag(R.string.event_id);
        databaseAdapter.deleteEvent(id);
        Intent intent = new Intent(getBaseContext(), DayActivity.class);
        startActivity(intent);
    }

}