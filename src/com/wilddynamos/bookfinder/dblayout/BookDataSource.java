package com.wilddynamos.bookfinder.dblayout;
import com.wilddynamos.bookfinder.model.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BookDataSource {
	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

	  // Database fields
	  private SQLiteDatabase database;
	  private BookSQLiteHelper dbHelper;
	  private String[] allColumns = { BookSQLiteHelper.COLUMN_ID,
	      BookSQLiteHelper.COLUMN_NAME, BookSQLiteHelper.COLUMN_PRICE, BookSQLiteHelper.COLUMN_PER,
	      BookSQLiteHelper.COLUMN_AVAILABLE_TIME, BookSQLiteHelper.COLUMN_LIKES,
	      BookSQLiteHelper.COLUMN_S_OR_R, BookSQLiteHelper.COLUMN_STATE, 
	      BookSQLiteHelper.COLUMN_DESCRIPTION, BookSQLiteHelper.COLUMN_POST_TIME, 
	      BookSQLiteHelper.COLUMN_OWNER_ID };

	  public BookDataSource(Context context) {	
		// new a sqlite helper
	    dbHelper = new BookSQLiteHelper(context);
	  }

	  public void open() throws SQLException {
		// open database
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
		// close database
	    dbHelper.close();
	  }

	  public Book createBook(String name, int price, Boolean per, int availableTime, int likes,
			  Boolean sOrR, Boolean state, String description, Date postTime, int ownerId,
			  Context context) {	// new a book
	    ContentValues values = new ContentValues();
	    values.put(BookSQLiteHelper.COLUMN_NAME, name);
	    values.put(BookSQLiteHelper.COLUMN_PRICE, price);
	    values.put(BookSQLiteHelper.COLUMN_PER, per);
	    values.put(BookSQLiteHelper.COLUMN_AVAILABLE_TIME, availableTime);
	    values.put(BookSQLiteHelper.COLUMN_LIKES, likes);
	    values.put(BookSQLiteHelper.COLUMN_S_OR_R, sOrR);
	    values.put(BookSQLiteHelper.COLUMN_STATE, state);
	    values.put(BookSQLiteHelper.COLUMN_DESCRIPTION, description);
	    values.put(BookSQLiteHelper.COLUMN_POST_TIME, dateFormat.format(postTime));
	    values.put(BookSQLiteHelper.COLUMN_OWNER_ID, ownerId);
	    
	    long insertId = database.insert(BookSQLiteHelper.TABLE_BOOK, null,
	        values);
	    Cursor cursor = database.query(BookSQLiteHelper.TABLE_BOOK,
	        allColumns, BookSQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Book newBook = cursorToBook(cursor, context);
	    cursor.close();
	    return newBook;
	  }
	  
	  public Book getBook(int book_id, Context context) {	// find a book
		  Cursor cursor = database.query(BookSQLiteHelper.TABLE_BOOK,
			        allColumns, UserSQLiteHelper.COLUMN_ID + " = " + book_id, null,
			        null, null, null);
		  cursor.moveToFirst();
		  Book book= cursorToBook(cursor, context);
		  cursor.close();
		  return book;
	  }

	  public void deleteBook(Book book) {		// delete a book
	    long id = book.getId();
	    System.out.println("Book deleted with id: " + id);
	    database.delete(BookSQLiteHelper.TABLE_BOOK, BookSQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<Book> getAllBooks(Context context) {		// find books
	    List<Book> books = new ArrayList<Book>();

	    Cursor cursor = database.query(BookSQLiteHelper.TABLE_BOOK,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Book book = cursorToBook(cursor, context);
	      books.add(book);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return books;
	  }

	  private Book cursorToBook(Cursor cursor, Context context) {
	    Book book = new Book();
	    book.setId(cursor.getInt(0));
	    book.setName(cursor.getString(1));
	    book.setPrice(cursor.getInt(2));
	    book.setPer(cursor.getInt(3) == 1);
	    book.setAvailableTime(cursor.getInt(4));
	    book.setLikes(cursor.getInt(5));
	    book.setSOrR(cursor.getInt(6) == 1);
	    book.setState(cursor.getInt(7) == 1);
	    book.setDescription(cursor.getString(8));
	    try {
	    	book.setPostTime(dateFormat.parse(cursor.getString(9)));
	    } catch(Exception e) {}
	    
	    int owner_id = cursor.getInt(10);
	    UserDataSource userSource = new UserDataSource(context);
	    User owner = userSource.getUser(owner_id);		
	    book.setOwner(owner);
	    
	    return book;
	  }
	} 