package com.example.anl2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventDataSource {
	private static final String TAG = "EventDataSource";
	static final int VERSION = 1;
	static final String DATABASE = "event.db";
	static final String TABLE = "event";
	public static final String C_ID = "id";
	public static final String C_YEAR = "year";
	public static final String C_MONTH = "month";
	public static final String C_DAY = "day";
	public static final String C_HOUR = "hour";
	public static final String C_CONTENT = "content";

	// DbHelper implementations
	class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context context) {
			super(context, DATABASE, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating database: " + DATABASE);
			db.execSQL("create table " + TABLE
				+ " ("
				+ C_ID + " integer primary key autoincrement not null, "
				+ C_YEAR + " int, "
				+ C_MONTH + " int, "
				+ C_DAY + " int, "
				+ C_HOUR + " int, "
				+ C_CONTENT + " text not null"
				+ ")"
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table " + TABLE);
			this.onCreate(db);
		}
	}

	private final DbHelper dbHelper;

	public EventDataSource(Context context) {
		this.dbHelper = new DbHelper(context);
		Log.i(TAG, "Initialized data");
	}

	public void close() {
		this.dbHelper.close();
	}
	
	public void insertOrIgnore(ContentValues values) {
		Log.d(TAG, "insertOrIgnore on " + values);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(TABLE, null, values,
					SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}
	}
	
	public void addEvent(int year, int month, int day, int hour, String content) {
		ContentValues event = new ContentValues();
		
		event.put(C_YEAR, year);
		event.put(C_MONTH, month);
		event.put(C_DAY, day);
		event.put(C_HOUR, hour);
		event.put(C_CONTENT, content);
		
		this.insertOrIgnore(event);
	}
	
	public int updateEvent(int id, String content) {
		ContentValues event = new ContentValues();
		event.put(C_CONTENT, content);
		
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		String whereClause = C_ID + "=?";
		String[] whereArgs = new String[]{Integer.toString(id)};
		return db.update(TABLE, event, whereClause, whereArgs);
	}
	
	public Cursor getEventInDay(int year, int month, int day) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		String whereClause = C_YEAR + "=? AND " + C_MONTH + "=? AND " + C_DAY + "=?";
		String[] whereArgs = new String[]{Integer.toString(year), Integer.toString(month), Integer.toString(day)};
		return db.query(TABLE, null, whereClause, whereArgs, null, null, null);
	}
	
	
	public Cursor getEventInTime(int year, int month, int day, int hour) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		String whereClause = C_YEAR + "=? AND " + C_MONTH + "=? AND " + C_DAY + "=? AND " + C_HOUR + "=?";
		String[] whereArgs = new String[]{Integer.toString(year), Integer.toString(month), Integer.toString(day), Integer.toString(hour)};
		return db.query(TABLE, null, whereClause, whereArgs, null, null, null);
	}
}