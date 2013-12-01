package com.wilddynamos.bookfinder.dblayout;
import com.wilddynamos.bookfinder.model.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RequestDataSource {
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  // Database fields
		  private SQLiteDatabase database;
		  private RequestSQLiteHelper dbHelper;
		  private String[] allColumns = { RequestSQLiteHelper.COLUMN_ID,
		      RequestSQLiteHelper.COLUMN_MESSAGE, RequestSQLiteHelper.COLUMN_STATE, 
		      RequestSQLiteHelper.COLUMN_REQUEST_TIME,
		      RequestSQLiteHelper.COLUMN_USER_ID, RequestSQLiteHelper.COLUMN_BOOK_ID };

		  public RequestDataSource(Context context) {
		    dbHelper = new RequestSQLiteHelper(context);
		  }

		  public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
		  }

		  public void close() {
		    dbHelper.close();
		  }

		  public Request createRequest(String message, Boolean state, Date requestTime, int user_id,
				  int book_id, Context context) {
		    ContentValues values = new ContentValues();
		    values.put(RequestSQLiteHelper.COLUMN_MESSAGE, message);
		    values.put(RequestSQLiteHelper.COLUMN_STATE, state);
		    values.put(RequestSQLiteHelper.COLUMN_REQUEST_TIME, dateFormat.format(requestTime));
		    values.put(RequestSQLiteHelper.COLUMN_USER_ID, user_id);
		    values.put(RequestSQLiteHelper.COLUMN_BOOK_ID, book_id);
		    long insertId = database.insert(RequestSQLiteHelper.TABLE_REQUEST, null,
		        values);
		    Cursor cursor = database.query(RequestSQLiteHelper.TABLE_REQUEST,
		        allColumns, RequestSQLiteHelper.COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		    cursor.moveToFirst();
		    Request newRequest = cursorToRequest(cursor, context);
		    cursor.close();
		    return newRequest;
		  }
		  
		  public Request getRequest(int request_id, Context context) {
			  Cursor cursor = database.query(RequestSQLiteHelper.TABLE_REQUEST,
				        allColumns, RequestSQLiteHelper.COLUMN_ID + " = " + request_id, null,
				        null, null, null);
			  cursor.moveToFirst();
			  Request request = cursorToRequest(cursor, context);
			  cursor.close();
			  return request;
		  }

		  public void deleteRequest(Request request) {
		    long id = request.getId();
		    System.out.println("Request deleted with id: " + id);
		    database.delete(RequestSQLiteHelper.TABLE_REQUEST, RequestSQLiteHelper.COLUMN_ID
		        + " = " + id, null);
		  }

		  public List<Request> getAllRequests(Context context) {
		    List<Request> requests = new ArrayList<Request>();

		    Cursor cursor = database.query(RequestSQLiteHelper.TABLE_REQUEST,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      Request request = cursorToRequest(cursor, context);
		      requests.add(request);
		      cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    return requests;
		  }

		  private Request cursorToRequest(Cursor cursor, Context context) {
		    Request request = new Request();
		    request.setId(cursor.getInt(0));
		    request.setMessage(cursor.getString(1));
		    request.setState(cursor.getInt(2) == 1);
		    
		    try {
		    	request.setRequestTime(dateFormat.parse(cursor.getString(3)));
		    } catch(Exception e) {}
		    
		    int user_id = cursor.getInt(4);
		    UserDataSource userSource = new UserDataSource(context);
		    User owner = userSource.getUser(user_id);		
		    request.setUser(owner);
		    
		    int book_id = cursor.getInt(5);
		    BookDataSource bookSource = new BookDataSource(context);
		    Book book = bookSource.getBook(book_id, context);		
		    request.setBook(book);
		    
		    return request;
		  }
}
