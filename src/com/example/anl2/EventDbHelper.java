package com.example.anl2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDbHelper extends SQLiteOpenHelper {
	static final int VERSION = 2;
	static final String DATABASE = "event.db";
	static final String TABLE = "event";
	public static final String C_ID = "id";
	public static final String C_YEAR = "year";
	public static final String C_MONTH = "month";
	public static final String C_DAY = "day";
	public static final String C_HOUR = "hour";
	public static final String C_CONTENT = "content";
	public static final String C_DISMISS = "dismiss";
	
	public EventDbHelper(Context context) {
		super(context, DATABASE, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE
			+ " ("
			+ C_ID + " integer primary key autoincrement not null, "
			+ C_YEAR + " int, "
			+ C_MONTH + " int, "
			+ C_DAY + " int, "
			+ C_HOUR + " int, "
			+ C_CONTENT + " text not null, "
			+ C_DISMISS + " int"
			+ ")"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table " + TABLE);
		this.onCreate(db);
	}
}