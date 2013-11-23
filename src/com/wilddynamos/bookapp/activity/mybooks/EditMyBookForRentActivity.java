package com.wilddynamos.bookapp.activity.mybooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import com.wilddynamos.bookapp.R;


public class EditMyBookForRentActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_create_edit);
		ShowSpinner showSpinner1 = new ShowSpinner((Spinner)findViewById(R.id.rentPriceUnitSelection));
		ShowSpinner showSpinner2 = new ShowSpinner((Spinner)findViewById(R.id.rentDurationUnitSelection));
		showSpinner1.setSpinner(this);
		showSpinner1.setListener();
		showSpinner2.setSpinner(this);
		showSpinner2.setListener();
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

