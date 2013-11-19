package com.wilddynamos.bookapp.activity.mybooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.wilddynamos.bookapp.R;


public class EditMyBookForSaleActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_edit_my_book_for_sale);
	}
	
	public void save(View view){
		Intent intent = new Intent(this, MyPostForSaleDetailsActivity.class);
		startActivity(intent);
	}
	
	public void cancel(View view){
		Intent intent = new Intent(this, MyPostForSaleDetailsActivity.class);
		startActivity(intent);
	}
} 

