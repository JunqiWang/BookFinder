package com.wilddynamos.bookapp.activity.mybooks;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import com.wilddynamos.bookapp.R;

public class MyBuyRequestDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_myrequestdetails);
		TextView tvPrice = (TextView)findViewById(R.id.myrequestdetails_priceBookDetail);
		TextView tvAvailable = (TextView)findViewById(R.id.myrequestdetails_bookAvailable);
		TextView tvAvailable2 = (TextView)findViewById(R.id.myrequestdetails_availableBookDetail);
		
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
	
}
