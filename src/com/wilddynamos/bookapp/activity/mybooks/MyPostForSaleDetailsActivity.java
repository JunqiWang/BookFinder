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
		setContentView(R.layout.mybooks_mypostdetails);
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
		Intent intent = new Intent(this, EditMyBookForSaleActivity.class);
		startActivity(intent);
	}
	
	/* see button*/
	public void showRequesterList(View view){
		Intent intent = new Intent(this, RequesterListActivity.class);
		startActivity(intent);
	}
}
