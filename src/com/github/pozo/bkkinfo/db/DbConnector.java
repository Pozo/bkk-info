package com.github.pozo.bkkinfo.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbConnector {
	private static final String ID = "id";
	
	private static final String LINE_NAME = "line_name";
	private static final String ENTRY_ID = "entry_id";
	
	private static DbConnector intance = null;
	
	private final SQLiteOpenHelper databaseHelper;
	private final SQLiteDatabase database;

	private DbConnector(Context context) {
		databaseHelper = new DatabaseHelper(context);
		database = databaseHelper.getWritableDatabase();
	}
	public static synchronized DbConnector getInstance(Context context) {
		if(intance == null || intance.isClosed()) {
			intance = new DbConnector(context);
		}
		return intance;
	}
	public static void close() {
		if(intance != null) {
			intance.closeResources();
		}		
	}
	private class DatabaseHelper extends SQLiteOpenHelper {
		public static final  String DB_NAME = "notification";
		
		private static final String TABLE_NAME_REQUIRED_LINES = "required_lines";
		private static final String TABLE_NAME_NOTIFICATIONS = "notifications";
		
		private static final String CREATE_SCHEMA_REQUIRED_LINES = 
			"CREATE TABLE ["+TABLE_NAME_REQUIRED_LINES+"] (" +
				"[" + ID + "] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
				",[" + LINE_NAME + "] VARCHAR (8) NULL)";

		private static final String CREATE_SCHEMA_NOTIFICATIONS = 
				"CREATE TABLE ["+TABLE_NAME_NOTIFICATIONS+"] (" +
					"[" + ID + "] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
					",[" + ENTRY_ID + "] VARCHAR (8) NULL)";
		
		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase dataBase) {		
			dataBase.execSQL(CREATE_SCHEMA_REQUIRED_LINES);
			dataBase.execSQL(CREATE_SCHEMA_NOTIFICATIONS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_REQUIRED_LINES	+ ";");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTIFICATIONS	+ ";");
			onCreate(db);
		}
	}

	public synchronized boolean isClosed() {
		return !database.isOpen();
	}
	private synchronized void closeResources() {
		if(databaseHelper !=null){
			databaseHelper.close();	
		}
		if(database != null) {
			database.close();			
		}		
	}
	public boolean isNotified(int entryId) {
		// check success in notifications schema
		
		return false;
	}
	public synchronized ArrayList<String> getRequiredLines() {
		ArrayList<String> lines = new ArrayList<String>();
		final Cursor cursor = database.query(
				DatabaseHelper.TABLE_NAME_REQUIRED_LINES, 
				new String[]{LINE_NAME}, 
				null, null, null, null, null);
		
		if(cursor.moveToFirst()) {
			do {
				lines.add(cursor.getString(0));
			} while (cursor.moveToNext());				
		}
		cursor.close();
		return lines;
	}
	public synchronized boolean isRequiredLine(String lineName) {
		boolean retval = false;
		final String constraint = LINE_NAME + " = \"" + lineName+"\"";

		final Cursor cursor = database.query(
				DatabaseHelper.TABLE_NAME_REQUIRED_LINES,
				new String[]{ID},
				constraint,
				null, null, null, null);
		retval = cursor.moveToFirst();
		cursor.close();		
		return retval;
	}
	public synchronized long addRequiredLine(String lineName) {
		final ContentValues values = new ContentValues();

		values.put(LINE_NAME, lineName);
		
		return database.insert(DatabaseHelper.TABLE_NAME_REQUIRED_LINES, null, values);
	}
	public synchronized long addRequiredLines(String[] lineNames) {
		final ContentValues values = new ContentValues();

		for (String lineName : lineNames) {
			values.put(LINE_NAME, lineName);			
		}
		
		return database.insert(DatabaseHelper.TABLE_NAME_REQUIRED_LINES, null, values);
	}
	public synchronized int removeRequiredLine(String lineName) {
		final String constraint = LINE_NAME + " = \"" + lineName+"\"";

		return database.delete(
				DatabaseHelper.TABLE_NAME_REQUIRED_LINES,
				constraint, null);
	}
	public synchronized int removeRequiredLines(String[] lineNames) {
		StringBuilder constraintBuilder = new StringBuilder();
		
		for (int i = 0; i < lineNames.length; i++) {
			if(i!=lineNames.length-1) {
				constraintBuilder.append(LINE_NAME + " = \"" + lineNames[i]+"\" AND ");	
			} else {
				constraintBuilder.append(LINE_NAME + " = \"" + lineNames[i]+"\"");
			}
		}
		return database.delete(
				DatabaseHelper.TABLE_NAME_REQUIRED_LINES,
				constraintBuilder.toString(), null);
	}
	public synchronized long addNotification(String entryId) {
		final ContentValues values = new ContentValues();

		values.put(ENTRY_ID, entryId);
		
		return database.insert(DatabaseHelper.TABLE_NAME_NOTIFICATIONS, null, values);
	}
	public synchronized int deleteAllNotifications() {
		return database.delete(
				DatabaseHelper.TABLE_NAME_NOTIFICATIONS,
				null, null);
	}
	public synchronized int deleteAllRequiredLines() {
		return database.delete(
				DatabaseHelper.TABLE_NAME_REQUIRED_LINES,
				null, null);		
	}
	public synchronized ArrayList<String> getNotifications() {
		ArrayList<String> lines = new ArrayList<String>();
		final Cursor cursor = database.query(
				DatabaseHelper.TABLE_NAME_NOTIFICATIONS, 
				new String[]{ENTRY_ID}, 
				null, null, null, null, null);
		
		if(cursor.moveToFirst()) {
			do {
				lines.add(cursor.getString(0));
			} while (cursor.moveToNext());				
		}
		cursor.close();
		return lines;
	}
}
