package com.wilddynamos.bookapp.activity.mybooks;

import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask.Status;
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
import com.wilddynamos.bookapp.ws.remote.action.GetBookDetail;
import com.wilddynamos.bookapp.ws.remote.action.mybooks.PostOrEditBook;


public class PostOrEditBookActivity extends Activity {
	
	private TextView title;
	private EditText name;
	private ImageView cover;
	private EditText price;
	private TextView wordPer;
	private Boolean perValue;
	private LinearLayout rentOnly;
	private EditText duration;
	private TextView durationUnit;
	private EditText description;
	private Button postOrSave;
	
	private boolean isPost;
	private Integer id = null;
	private boolean isRent;
	private String nameValue;
	
	private GetBookDetail preLoad;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_create_edit);
		
//		isPost = getIntent().getExtras().getBoolean("isPost");
//		id = getIntent().getExtras().getInt("id");
//		isRent = getIntent().getExtras().getBoolean("isRent");
//		nameValue = getIntent().getExtras().getString("name");
		isPost = false;
		isRent = true;
		id = 2;
		
		title = (TextView) findViewById(R.id.createOrEditMyBookTitle);
		name = (EditText) findViewById(R.id.createOrEditMyBookName);
		cover = (ImageView) findViewById(R.id.createOrEditMyBookCover);
		price = (EditText) findViewById(R.id.createOrEditMyBookPrice);
		description = (EditText) findViewById(R.id.createOrEditMyBookDescription);
		postOrSave = (Button) findViewById(R.id.createOrEditMyBookSubmit);
		
		if(isPost) {
			postOrSave.setText("Post");
			
			if(isRent)
				title.setText("Post a new book for RENT");
			else
				title.setText("Post a new book for SELL");
			
		} else {
			title.setText("Edit a book");
			postOrSave.setText("Save");
		}
		
		wordPer = (TextView) findViewById(R.id.createOrEditMyBookPer);
		rentOnly = (LinearLayout) findViewById(R.id.createOrEditMyBookPerRentOnly);
		Spinner per = (Spinner) findViewById(R.id.rentPriceUnitSelection);
		if(isRent) {
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
						durationUnit.setText("week");
						break;
						
					case 1:
						perValue = true;
						durationUnit.setText("month");
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
		
		if(!isPost) {
			preLoad = new GetBookDetail(this);
			preLoad.execute(new String[]{id + ""});
		}
	}
	
	public void fill(JSONArray jsonArray) {
		name.setText(nameValue);
		
		if(jsonArray == null || jsonArray.length() == 0)
			return;
		
		String s = null;
		try {
			JSONObject jo = jsonArray.getJSONObject(0);
			System.out.println(jo.length());
			price.setText(jo.getString("price"));
			description.setText(jo.getString("description"));
			System.out.println(jo.getString("price"));
			System.out.println(jo.getString("description"));
			if(isRent) {
				duration.setText(jo.getString("availableTime"));
				//TODO  per w/m
			}
			s = jo.getString("cover");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(s != null && !"".equals(s)) {
			byte[] cover = s.getBytes(Charset.forName("ISO-8859-1"));
			Bitmap bitmap = BitmapFactory.decodeByteArray(cover, 0, cover.length);
			this.cover.setImageBitmap(bitmap);
		}
	}
	
	public void save(View view) {
		if(!isPost && (preLoad == null || preLoad.getStatus() != Status.FINISHED))
			return;
		
		if("".equals(name.getText().toString())
				|| "".equals(price.getText().toString())) {
			
			Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
			return;
		}
		if(isRent
				&& "".equals(duration.getText().toString())) {
			Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String[] params = null;
		
		if(isRent)
			params = new String[] {
					isRent + "", id == null ? null : id + "",
					name.getText().toString(), price.getText().toString(),
					description.getText().toString(),
					perValue + "", duration.getText().toString()
				};
		else
			params = new String[] {
				isRent + "", id == null ? null : id + "",
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

