package com.wilddynamos.bookfinder.dblayout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserSQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_USER = "user";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_PASSWORD = "password";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_GENDER = "gender";
	public static final String COLUMN_CAMPUS = "campus";
	public static final String COLUMN_CONTACT = "contact";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_PHOTO = "photo";

	private static final String DATABASE_NAME = "bookapp.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_USER
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_EMAIL + " text not null, " + COLUMN_PASSWORD
			+ " text not null, " + COLUMN_NAME + " text, " + COLUMN_GENDER
			+ " boolean, " + COLUMN_CAMPUS + " text, " + COLUMN_CONTACT
			+ " text, " + COLUMN_ADDRESS + " text, " + COLUMN_PHOTO + " text);";

	public UserSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(UserSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		onCreate(db);
	}
}
