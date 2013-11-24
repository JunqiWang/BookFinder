package com.wilddynamos.bookapp.dblayout;
import com.wilddynamos.bookapp.model.*;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private UserSQLiteHelper dbHelper;
	  private String[] allColumns = { UserSQLiteHelper.COLUMN_ID,
	      UserSQLiteHelper.COLUMN_EMAIL, UserSQLiteHelper.COLUMN_PASSWORD, UserSQLiteHelper.COLUMN_NAME,
	      UserSQLiteHelper.COLUMN_GENDER, UserSQLiteHelper.COLUMN_CAMPUS, UserSQLiteHelper.COLUMN_CONTACT,
	      UserSQLiteHelper.COLUMN_ADDRESS, UserSQLiteHelper.COLUMN_PHOTO };

	  public UserDataSource(Context context) {
	    dbHelper = new UserSQLiteHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public User createUser(int id, String email, String password, String name, Boolean gender, String campus,
			  String contact, String address, String photoPath) {
	    ContentValues values = new ContentValues();
	    values.put(UserSQLiteHelper.COLUMN_ID, id);
	    values.put(UserSQLiteHelper.COLUMN_EMAIL, email);
	    values.put(UserSQLiteHelper.COLUMN_PASSWORD, password);
	    values.put(UserSQLiteHelper.COLUMN_NAME, name);
	    values.put(UserSQLiteHelper.COLUMN_GENDER, gender);
	    values.put(UserSQLiteHelper.COLUMN_CAMPUS, campus);
	    values.put(UserSQLiteHelper.COLUMN_CONTACT, contact);
	    values.put(UserSQLiteHelper.COLUMN_ADDRESS, address);
	    values.put(UserSQLiteHelper.COLUMN_PHOTO, photoPath);
	    database.insert(UserSQLiteHelper.TABLE_USER, null, values);
	    Cursor cursor = database.query(UserSQLiteHelper.TABLE_USER,
	        allColumns, UserSQLiteHelper.COLUMN_ID + " = " + id, null,
	        null, null, null);
	    cursor.moveToFirst();
	    User newUser = cursorToUser(cursor);
	    cursor.close();
	    return newUser;
	  }
	  
	  public User getUser(int user_id) {
		  Cursor cursor = database.query(UserSQLiteHelper.TABLE_USER,
			        allColumns, UserSQLiteHelper.COLUMN_ID + " = " + user_id, null,
			        null, null, null);
		  cursor.moveToFirst();
		  User user = cursorToUser(cursor);
		  cursor.close();
		  return user;
	  }
	  
	  public int updateUser(User user) {
		  ContentValues values = new ContentValues();
//		  values.put(UserSQLiteHelper.COLUMN_EMAIL, user.getEmail());
//		  values.put(UserSQLiteHelper.COLUMN_PASSWORD, user.getPassword());
		  values.put(UserSQLiteHelper.COLUMN_NAME, user.getName());
		  values.put(UserSQLiteHelper.COLUMN_GENDER, user.getGender());
		  values.put(UserSQLiteHelper.COLUMN_CAMPUS, user.getCampus());
		  values.put(UserSQLiteHelper.COLUMN_CONTACT, user.getContact());
		  values.put(UserSQLiteHelper.COLUMN_ADDRESS, user.getAddress());
		  values.put(UserSQLiteHelper.COLUMN_PHOTO, user.getPhotoAddr());
		  System.out.println(values);
		  return database.update(UserSQLiteHelper.TABLE_USER, values, UserSQLiteHelper.COLUMN_ID
			        + " = " + user.getId(), null);
	  }

	  public void deleteUser(User user) {
	    long id = user.getId();
	    System.out.println("User deleted with id: " + id);
	    database.delete(UserSQLiteHelper.TABLE_USER, UserSQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<User> getAllUsers() {
	    List<User> users = new ArrayList<User>();

	    Cursor cursor = database.query(UserSQLiteHelper.TABLE_USER,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      User user = cursorToUser(cursor);
	      users.add(user);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return users;
	  }

	  private User cursorToUser(Cursor cursor) {
	    User user = new User();
	    user.setId(cursor.getInt(0));
	    user.setEmail(cursor.getString(1));
	    user.setPassword(cursor.getString(2));
	    user.setName(cursor.getString(3));
	    user.setGender(cursor.getInt(4) == 1);
	    user.setCampus(cursor.getString(5));
	    user.setContact(cursor.getString(6));
	    user.setAddress(cursor.getString(7));
	    user.setPhotoAddr(cursor.getString(8));
	    
	    return user;
	  }
	  
	  
} 