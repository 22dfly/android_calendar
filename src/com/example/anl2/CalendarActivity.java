package com.example.anl2;

import java.util.Calendar;

import android.database.DatabaseUtils;

import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Button;
import android.widget.Toast;


public class CalendarActivity extends ActionBarActivity {
	
	private static final String tag = "Calendar";
	
	private TableLayout calView;
	private Button currButton;
	
	private Calendar calCaculator;
	private int month;
	private int year;
	
	protected SharedPreferences sharedPref;
	protected SharedPreferences.Editor editor;
	
	private NotificationManager NM;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        
        SharedPreferences sharedPref	= getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor							= sharedPref.edit();
        calCaculator					= Calendar.getInstance();
        
        year	= sharedPref.getInt(getString(R.string.data_year), calCaculator.get(Calendar.YEAR));
        month	= sharedPref.getInt(getString(R.string.data_month), calCaculator.get(Calendar.MONTH));
        
        // Initialize variables
        calView      = (TableLayout) findViewById(R.id.calendar);
        currButton   = (Button) findViewById(R.id.currButton);
        
        // Draw header
        Typeface icon_font = Typeface.createFromAsset(getAssets(), "fonts/icomoon.ttf");
		
		Button prevButton = (Button) findViewById(R.id.prevButton);
		prevButton.setTypeface(icon_font);
		
		Button nextButton = (Button) findViewById(R.id.nextButton);
		nextButton.setTypeface(icon_font);
		
		// Draw calendar
        drawMonthCalendar();
    }
    
    public void movePrevMonth(View view) {
    	int[] prevMonth = getPrevMonth();
    	
    	// Set
    	month = prevMonth[0];
    	year  = prevMonth[1];
    	
    	// Redraw
    	drawMonthCalendar();
    }
    
    public void moveNextMonth(View view) {
    	int[] nextMonth = getNextMonth();
    	
    	// Set
    	month = nextMonth[0];
    	year  = nextMonth[1];
    	
    	// Redraw
    	drawMonthCalendar();
    }
    
    public void moveCurrMonth(View view) {
    	Calendar cal = Calendar.getInstance();
        month        = cal.get(Calendar.MONTH);
        year         = cal.get(Calendar.YEAR);

    	// Redraw
    	drawMonthCalendar();
    }
    
    public void drawMonthCalendar() {
    	//Log.d(tag, "Draw (month/year): " + (month + 1) + "/" + year);
    	
    	editor.putInt(getString(R.string.data_year), year);
    	editor.putInt(getString(R.string.data_month), month);
    	editor.commit();
		
		int index = 0;
		TableRow weekRow = new TableRow(this);
		
		// Calculate current month
		calCaculator.set(year, month, 1);
		currButton.setText(DateFormat.format("MMMM yyyy", calCaculator.getTime()));
		calView.removeAllViews();
		
		int daysOfMonth = calCaculator.getActualMaximum(Calendar.DAY_OF_MONTH);
		int firstDayOfWeek = calCaculator.get(Calendar.DAY_OF_WEEK);
		
		calCaculator.set(year, month, daysOfMonth);
		int lastDayOfWeek = calCaculator.get(Calendar.DAY_OF_WEEK);
		
		// Prepare prev month
		if (firstDayOfWeek != 1) {
			int prevMonthDay = firstDayOfWeek - 2;
			int[] prevMonth = getPrevMonth();
			calCaculator.set(prevMonth[1], prevMonth[0], 1);
			int lastDayOfPrevMonth = calCaculator.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			for (int i = prevMonthDay; i >= 0; i--) {
				Button dayButton = dayButton((lastDayOfPrevMonth - i), prevMonth[0], prevMonth[1], false);
				weekRow.addView(dayButton);
				index++;
				if (index % 7 == 0) {
					calView.addView(weekRow);
					weekRow = new TableRow(this);
				}
			}
		}
		
		// Prepare current month
		for (int i = 1; i <= daysOfMonth; i++) {
			Button dayButton = this.dayButton(i, month, year, true);
			weekRow.addView(dayButton);
			index++;
			if (index % 7 == 0) {
				calView.addView(weekRow);
				weekRow = new TableRow(this);
			}
		}
		
		// Prepare next month
		if (lastDayOfWeek != 7) {
			int nextMonthDay = 7 - lastDayOfWeek;
			int[] nextMonth = getNextMonth();
			
			calCaculator.set(nextMonth[1], nextMonth[0], 1);
			for (int i = 1; i <= nextMonthDay; i++) {
				Button dayButton = dayButton(i, nextMonth[0], nextMonth[1], false);
				weekRow.addView(dayButton);
				index++;
				if (index % 7 == 0) {
					calView.addView(weekRow);
					weekRow = new TableRow(this);
				}
			}
		}
    }
    
    private Button dayButton(int day, int month, int year, boolean isMonthDay) {
    	Button dayButton = new Button(this);
		TableRow.LayoutParams params = new TableRow.LayoutParams();
		
		params.width  = 0;
		params.height = TableRow.LayoutParams.WRAP_CONTENT;
		params.weight = 1;
		
		dayButton.setText("" + day);
		dayButton.setTag(R.string.data_day, day);
		dayButton.setLayoutParams(params);
		
		if (isMonthDay) {
			dayButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dayActivity(v);
					//updateNotification(v);
				}
			});
		} else {
			dayButton.setBackgroundColor(Color.TRANSPARENT);
			dayButton.setClickable(false);
			dayButton.setFocusable(false);
		}
		
		return dayButton;
    }
    
    public void dayActivity(View view) {
    	editor.putInt(getString(R.string.data_day), (Integer) view.getTag(R.string.data_day));
    	editor.commit();
    	
    	Intent intent = new Intent(getBaseContext(), DayActivity.class);
        startActivity(intent);
    }
    
    private int[] getPrevMonth() {
    	int prevMonth;
    	int prevYear;
    	
    	if (month == 0) {
			prevMonth = 11;
			prevYear  = year - 1;
		} else {
			prevMonth = month - 1;
			prevYear  = year;
		}
    	
    	return new int[] {prevMonth, prevYear};
    }
    
    private int[] getNextMonth() {
    	int nextMonth;
    	int nextYear;
    	
    	if (month == 11) {
    		nextMonth = 0;
    		nextYear  = year + 1;
		} else {
			nextMonth = month + 1;
			nextYear  = year;
		}
    	
    	return new int[] {nextMonth, nextYear};
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    private static final int NOTIFY_ME_ID=1337;
    
    protected void updateNotification(View view){
    	 Log.i("Update", "notification");

    	 final NotificationManager mgr=
    	            (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
    	        Notification note=new Notification(R.drawable.ic_action_back,
    	                                                        "Android Example Status message!",
    	                                                        System.currentTimeMillis());
    	         
    	        // This pending intent will open after notification click
    	        PendingIntent i=PendingIntent.getActivity(this, 0,
    	                                                new Intent(this, CalendarActivity.class),
    	                                                0);
    	         
    	        note.setLatestEventInfo(this, "Android Example Notification Title",
    	                                "This is the android example notification message", i);
    	         
    	        //After uncomment this line you will see number of notification arrived
    	        note.number=2;
    	        mgr.notify(NOTIFY_ME_ID, note);
    	


     }
}
