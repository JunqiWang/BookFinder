package com.wilddynamos.bookapp.activity.mybooks;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import com.wilddynamos.bookapp.R;

public class MyPostForRentDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_mypostdetails);
		TextView tv = (TextView)findViewById(R.id.titleMyPostDetail);
		tv.setText("Rent");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*edit button*/
	public void editBook(View view){
		Intent intent = new Intent(this, EditMyBookForRentActivity.class);
		startActivity(intent);
	}
	
	/* see button*/
	public void showRequesterList(View view){
		Intent intent = new Intent(this, RequesterListActivity.class);
		startActivity(intent);
	}
}
