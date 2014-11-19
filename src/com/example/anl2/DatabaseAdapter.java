package com.example.anl2;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context) {
        DatabaseManager.initializeInstance(new EventDbHelper(context));
    }

    public void addEvent(int year, int month, int day, int hour, String content) {
        database = DatabaseManager.getInstance().openDatabase();

        ContentValues event = new ContentValues();
        event.put(EventDbHelper.C_YEAR, year);
        event.put(EventDbHelper.C_MONTH, month);
        event.put(EventDbHelper.C_DAY, day);
        event.put(EventDbHelper.C_HOUR, hour);
        event.put(EventDbHelper.C_CONTENT, content);
        event.put(EventDbHelper.C_DISMISS, 0);

        database.insertWithOnConflict(EventDbHelper.TABLE, null, event,
                SQLiteDatabase.CONFLICT_IGNORE);

        DatabaseManager.getInstance().closeDatabase();
    }

    public int deleteEvent(String id) {
        database = DatabaseManager.getInstance().openDatabase();
        String whereClause = EventDbHelper.C_ID + "=?";
        String[] whereArgs = new String[] { id };

        int result = database.delete(EventDbHelper.TABLE, whereClause, whereArgs);
        DatabaseManager.getInstance().closeDatabase();

        return result;
    }

    public int updateEvent(String id, String content) {
        database = DatabaseManager.getInstance().openDatabase();

        ContentValues event = new ContentValues();
        event.put(EventDbHelper.C_CONTENT, content);
        event.put(EventDbHelper.C_DISMISS, 0);
        String whereClause = EventDbHelper.C_ID + "=?";
        String[] whereArgs = new String[] {id};

        int result = database.update(EventDbHelper.TABLE, event, whereClause, whereArgs);
        DatabaseManager.getInstance().closeDatabase();

        return result;
    }

    @SuppressLint("UseSparseArrays")
    public HashMap<Integer, String> getEventInDay(int year, int month, int day) {
        database = DatabaseManager.getInstance().openDatabase();

        String whereClause = EventDbHelper.C_YEAR + "=? AND "
                + EventDbHelper.C_MONTH + "=? AND " + EventDbHelper.C_DAY
                + "=?";
        String[] whereArgs = new String[] { Integer.toString(year),  Integer.toString(month), Integer.toString(day) };
        Cursor cursor = database.query(EventDbHelper.TABLE, null, whereClause, whereArgs, null, null, null);

        HashMap<Integer, String> result = new HashMap<Integer, String>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                result.put(cursor.getInt(cursor.getColumnIndex(EventDbHelper.C_HOUR)),
                        cursor.getString(cursor.getColumnIndex(EventDbHelper.C_CONTENT)));
                cursor.moveToNext();
            }
        }
        cursor.close();

        DatabaseManager.getInstance().closeDatabase();

        return result;
    }

    public Event getEventInTime(int year, int month, int day, int hour) {
        database = DatabaseManager.getInstance().openDatabase();

        String whereClause = EventDbHelper.C_YEAR + "=? AND "
                + EventDbHelper.C_MONTH + "=? AND " + EventDbHelper.C_DAY
                + "=? AND " + EventDbHelper.C_HOUR + "=?";
        String[] whereArgs = new String[] { Integer.toString(year),
                Integer.toString(month), Integer.toString(day),
                Integer.toString(hour) };
        Cursor cursor = database.query(EventDbHelper.TABLE, null, whereClause, whereArgs,
                null, null, null);

        Event event = new Event();
        event.setTime(year, month, day, hour);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            event.id = cursor.getInt(cursor.getColumnIndex(EventDbHelper.C_ID));
            event.event = cursor.getString(cursor.getColumnIndex(EventDbHelper.C_CONTENT));
            event.dismiss = cursor.getInt(cursor.getColumnIndex(EventDbHelper.C_DISMISS));
        }
        cursor.close();

        DatabaseManager.getInstance().closeDatabase();

        return event;
    }
    
    public Event getEventById(int id) {
        database = DatabaseManager.getInstance().openDatabase();

        String whereClause = EventDbHelper.C_ID + "=?";
        String[] whereArgs = new String[] {Integer.toString(id)};
        Cursor cursor = database.query(EventDbHelper.TABLE, null, whereClause, whereArgs,
                null, null, null);

        Event event = new Event();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            event.id = cursor.getInt(cursor.getColumnIndex(EventDbHelper.C_ID));
            event.event = cursor.getString(cursor.getColumnIndex(EventDbHelper.C_CONTENT));
            event.dismiss = cursor.getInt(cursor.getColumnIndex(EventDbHelper.C_DISMISS));
            event.setTime(cursor.getInt(cursor.getColumnIndex(EventDbHelper.C_YEAR)),
                    cursor.getInt(cursor.getColumnIndex(EventDbHelper.C_MONTH)),
                    cursor.getInt(cursor.getColumnIndex(EventDbHelper.C_DAY)),
                    cursor.getInt(cursor.getColumnIndex(EventDbHelper.C_HOUR)));
        }
        cursor.close();

        DatabaseManager.getInstance().closeDatabase();

        return event;
    }

    public int dismissEvent(String id) {
        database = DatabaseManager.getInstance().openDatabase();

        ContentValues event = new ContentValues();
        event.put(EventDbHelper.C_DISMISS, 1);
        String whereClause = EventDbHelper.C_ID + "=?";
        String[] whereArgs = new String[] { id };

        int result = database.update(EventDbHelper.TABLE, event, whereClause, whereArgs);
        DatabaseManager.getInstance().closeDatabase();

        return result;
    }
}