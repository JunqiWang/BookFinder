package com.wilddynamos.bookapp.activity.mybooks;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.MultiWindowActivity;
import com.wilddynamos.bookapp.ws.remote.action.mybooks.PostOrEditBook;


public class PostOrEditBookActivity extends Activity {
	
	private TextView title;
	private EditText name;
	private ImageView cover;
	private EditText price;
	private TextView wordPer;
	Spinner per;
	private Boolean perValue;
	private LinearLayout rentOnly;
	private EditText duration;
	private TextView durationUnit;
	private EditText description;
	private Button postOrSave;
	
	private boolean isPost;
	private boolean sOrR;
	private Integer id = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_create_edit);
		
		title = (TextView) findViewById(R.id.createOrEditMyBookTitle);
		name = (EditText) findViewById(R.id.createOrEditMyBookName);
		cover = (ImageView) findViewById(R.id.createOrEditMyBookCover);
		price = (EditText) findViewById(R.id.createOrEditMyBookPrice);
		description = (EditText) findViewById(R.id.createOrEditMyBookDescription);
		postOrSave = (Button) findViewById(R.id.createOrEditMyBookSubmit);
		
		isPost = getIntent().getExtras().getBoolean("isPost");
		sOrR = getIntent().getExtras().getBoolean("sOrR");
		id = getIntent().getExtras().getInt("id");
		
		if(isPost) {
			postOrSave.setText("Post");
			
			if(!sOrR)
				title.setText("Post a new book for RENT");
			else
				title.setText("Post a new book for SELL");
			
		} else {
			title.setText("Edit a book");
			postOrSave.setText("Save");
		}
		
		wordPer = (TextView) findViewById(R.id.createOrEditMyBookPer);
		rentOnly = (LinearLayout) findViewById(R.id.createOrEditMyBookPerRentOnly);
		per = (Spinner) findViewById(R.id.rentPriceUnitSelection);
		if(!sOrR) {
			duration = (EditText) findViewById(R.id.rentDuration);
			durationUnit = (TextView) findViewById(R.id.rentDurationUnit);
			durationUnit.setText("week");
			
			per.setAdapter(new ArrayAdapter<String>(
					this, 
					R.layout.mybooks_spinneritem, 
					new String[]{"week", "month"}));
			per.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					switch (arg2) {
					case 0:
						perValue = false;
						durationUnit.setText("weeks");
						break;
						
					case 1:
						perValue = true;
						durationUnit.setText("months");
						break;
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			
		} else {
			wordPer.setVisibility(TextView.INVISIBLE);
			per.setVisibility(Spinner.INVISIBLE);
			rentOnly.setVisibility(LinearLayout.INVISIBLE);
		}
		
		
		if(!isPost)
			fill();
	}
	
	private void fill() {
		name.setText(getIntent().getExtras().getString("name"));
		//TODO  read cover form sd card sqlite
		price.setText(getIntent().getExtras().getString("price"));
		if(!sOrR) {
			per.setSelection(getIntent().getExtras().getBoolean("per") ? 1 : 0);
			duration.setText(getIntent().getExtras().getInt("duration") + "");
		}
		description.setText(getIntent().getExtras().getString("description"));
	}
	
	public void save(View view) {
		
		if("".equals(name.getText().toString())
				|| "".equals(price.getText().toString())) {
			
			Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!sOrR
				&& "".equals(duration.getText().toString())) {
			Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String[] params = null;
		
		if(!sOrR)
			params = new String[] {
					!sOrR + "", id == null ? null : id + "",
					name.getText().toString(), price.getText().toString(),
					description.getText().toString(),
					perValue + "", duration.getText().toString()
				};
		else
			params = new String[] {
					!sOrR + "", id == null ? null : id + "",
					name.getText().toString(), price.getText().toString(),
					description.getText().toString()
				};
		
		PostOrEditBook submit = new PostOrEditBook(this);
		submit.execute(params);
	}
	
	public void cancel(View view) {
		
		new AlertDialog.Builder(this)
			.setTitle("Abort?")
			.setPositiveButton("Yes", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(PostOrEditBookActivity.this, 
							MultiWindowActivity.class);
					intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
					startActivity(intent);
				}
			})
			.setNegativeButton("Back", null)
			.show();
	}
} 

