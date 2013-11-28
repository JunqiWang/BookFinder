package com.wilddynamos.bookapp.activity.mybooks;

import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.action.profile.GetRequestDetail;


public class RequesterDetailActivity extends Activity {
	
	private ImageView profileImage;
	private TextView name;
	private TextView gender;
	private TextView campus;
	private TextView contact;
	private TextView address;
	
	private int requesterId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_profile);
		
		Intent intent = getIntent();
		requesterId = intent.getIntExtra("id",0);
		
		profileImage = (ImageView) findViewById(R.id.mybooks_profile_image);
		name = (TextView) findViewById(R.id.mybooks_profile_name);
		gender = (TextView) findViewById(R.id.mybooks_profile_gender);
		campus = (TextView) findViewById(R.id.mybooks_profile_campus);
		contact = (TextView) findViewById(R.id.mybooks_profile_contact);
		address = (TextView) findViewById(R.id.mybooks_profile_address);
		
		GetRequestDetail grd = new GetRequestDetail(this);
		String params[] = {String.valueOf(requesterId)};
		System.out.println(params[0]);
		grd.execute(params);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void fill(JSONArray jsonArray) {
		if(jsonArray == null || jsonArray.length() == 0)
			return;
		
		JSONObject jo = null;
		String s = null;
		try {
			jo = jsonArray.getJSONObject(0);
			
			name.setText(jo.getString("name"));
			gender.setText(jo.getString("gender"));
			campus.setText(jo.getString("campus"));
			contact.setText(jo.getString("contact"));
			address.setText(jo.getString("address"));
			
			s = jo.getString("photo");
			
		} catch (JSONException e) {
		}
		
		if(s != null && !"".equals(s)) {
			byte[] imageBytes = s.getBytes(Charset.forName("ISO-8859-1"));
			System.out.println(imageBytes.length);
			Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
			profileImage.setImageBitmap(image);
		}
		
		
	}
}
