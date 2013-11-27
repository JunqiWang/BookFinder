package com.wilddynamos.bookapp.activity.mybooks;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;

public class MyPostForSaleDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_detail);
		TextView tvPrice = (TextView)findViewById(R.id.postdetails_priceBookDetail);
		TextView tvAvailable = (TextView)findViewById(R.id.postdetails_bookAvailable);
		TextView tvAvailable2 = (TextView)findViewById(R.id.postdetails_availableBookDetail);
		
		tvPrice.setText("15");
		tvAvailable.setText("Weclome to buy it!");
		tvAvailable2.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*edit button*/
	public void editBook(View view){
		Intent intent = new Intent(this, PostOrEditBookActivity.class);
		intent.putExtra("isPost", false);
		intent.putExtra("isRent", true);//TODO
		intent.putExtra("id", 10);//TODO
		intent.putExtra("name", "name");//TODO
		startActivity(intent);
	}
	
	/* see button*/
	public void showRequesterList(View view){
		Intent intent = new Intent(this, RequesterListActivity.class);
		//intent.putExtra("book_id",bookId);
		startActivity(intent);
	}
}
