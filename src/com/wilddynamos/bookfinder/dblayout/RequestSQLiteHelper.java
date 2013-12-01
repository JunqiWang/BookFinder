package com.wilddynamos.bookfinder.dblayout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RequestSQLiteHelper extends SQLiteOpenHelper {
	  public static final String TABLE_REQUEST = "request";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_MESSAGE = "message";
	  public static final String COLUMN_STATE = "state";
	  public static final String COLUMN_REQUEST_TIME = "request_time";
	  public static final String COLUMN_USER_ID = "user_id";
	  public static final String COLUMN_BOOK_ID = "book_id";

	  private static final String DATABASE_NAME = "bookapp.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_REQUEST + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_MESSAGE + " text, " 
	      + COLUMN_STATE + " boolean not null, "
	      + COLUMN_REQUEST_TIME + " datetime not null, "
	      + COLUMN_USER_ID + " int not null, "
	      + COLUMN_BOOK_ID + " int not null);";

	  public RequestSQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(RequestSQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUEST);
	    onCreate(db);
	  }
}
