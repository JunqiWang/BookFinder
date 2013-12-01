package com.wilddynamos.bookfinder.dblayout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookSQLiteHelper extends SQLiteOpenHelper {
	  public static final String TABLE_BOOK = "book";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_PRICE = "price";
	  public static final String COLUMN_PER = "per";
	  public static final String COLUMN_AVAILABLE_TIME = "available_time";
	  public static final String COLUMN_LIKES = "likes";
	  public static final String COLUMN_S_OR_R = "s_or_r";
	  public static final String COLUMN_STATE = "state";
	  public static final String COLUMN_DESCRIPTION = "description";
	  public static final String COLUMN_POST_TIME = "post_time";
	  public static final String COLUMN_OWNER_ID = "owner_id";

	  private static final String DATABASE_NAME = "bookapp.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_BOOK + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_NAME + " text not null, " 
	      + COLUMN_PRICE + " integer not null, "
	      + COLUMN_PER + " boolean not null, "
	      + COLUMN_AVAILABLE_TIME + " integer not null, "
	      + COLUMN_LIKES + " integer, "
	      + COLUMN_S_OR_R + " boolean not null, "
	      + COLUMN_STATE + " boolean, "
	      + COLUMN_DESCRIPTION + " text not null, "
	      + COLUMN_POST_TIME + " datetime not null, "
	      + COLUMN_OWNER_ID + "int not null);";

	  public BookSQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(BookSQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
	    onCreate(db);
	  }
}
