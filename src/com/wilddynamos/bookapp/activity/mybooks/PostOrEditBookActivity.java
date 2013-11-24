package com.wilddynamos.bookapp.activity.mybooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.MultiWindowActivity;


public class PostOrEditBookActivity extends Activity {
	
	private TextView title;
	private EditText name;
	private EditText price;
	private Spinner per;
	private EditText duration;
	private TextView durationUnit;
	private EditText description;
	private Button postOrSave;
	private Button cancel;
	
	private boolean isPost;
	private boolean isRent;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_create_edit);
		
		//initialisation
		isPost = getIntent().getExtras().getBoolean("isPost");
		isRent = getIntent().getExtras().getBoolean("isRent");
		
		title = (TextView) findViewById(R.id.createOrEditMyBookTitle);
		name = (EditText) findViewById(R.id.createOrEditMyBookName);
		price = (EditText) findViewById(R.id.createOrEditMyBookPrice);
		
		if(isRent) {
			per = (Spinner) findViewById(R.id.rentPriceUnitSelection);
			per.setAdapter(new ArrayAdapter<String>(
					this, 
					R.layout.mybooks_spinneritem, 
					new String[]{"day", "week", "month"}));
			per.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//TODO
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}

			});
			
			duration = (EditText) findViewById(R.id.rentDuration);
			durationUnit = (TextView) findViewById(R.id.rentDurationUnit);
		}
		
		description = (EditText) findViewById(R.id.createOrEditMyBookDescription);
		postOrSave = (Button) findViewById(R.id.createOrEditMyBookSubmit);
		cancel = (Button) findViewById(R.id.createOrEditMyBookCancel);
		//initialisation end
		
	}
	

	public void save(View view) {
		Intent intent = new Intent(this, MultiWindowActivity.class);
		intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
		startActivity(intent);
	}
	
	public void cancel(View view) {
		Intent intent = new Intent(this, MultiWindowActivity.class);
		intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
		startActivity(intent);
	}
} 

