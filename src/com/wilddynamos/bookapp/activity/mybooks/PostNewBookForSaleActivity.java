package com.wilddynamos.bookapp.activity.mybooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.MultiWindowActivity;


public class PostNewBookForSaleActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_edit_my_book_for_sale);
		TextView tv1 = (TextView)findViewById(R.id.editMyBookForSaleTitle);
		tv1.setText("Post a New Book For Sale");
		Button b1 = (Button)findViewById(R.id.edit_my_book_for_sale_saveButton);
		b1.setText("Post");
	}
	
	/* post button*/
	public void save(View view){
		Intent intent = new Intent(this, MultiWindowActivity.class);
		intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
		startActivity(intent);
	}
	
	/* cancel button*/
	public void cancel(View view){
		Intent intent = new Intent(this, MultiWindowActivity.class);
		intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
		startActivity(intent);
	}
} 

