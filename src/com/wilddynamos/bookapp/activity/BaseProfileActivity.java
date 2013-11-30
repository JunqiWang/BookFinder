package com.wilddynamos.bookapp.activity;

import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.utils.ZoomInOutAction;

public abstract class BaseProfileActivity extends Activity {

	protected ImageView bg;
	protected TextView name;
	protected TextView email;
	protected ImageView profileImage;
	protected TextView gender;
	protected TextView campus;
	protected TextView contact;
	protected TextView address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_detail);

		bg = (ImageView) findViewById(R.id.profile_block);

		name = (TextView) findViewById(R.id.profile_name);
		email = (TextView) findViewById(R.id.profile_email);
		profileImage = (ImageView) findViewById(R.id.profile_image);
		gender = (TextView) findViewById(R.id.profile_gender);
		campus = (TextView) findViewById(R.id.profile_campus);
		contact = (TextView) findViewById(R.id.profile_contact);
		address = (TextView) findViewById(R.id.profile_address);

		createFunctionSpecificView();
	}

	protected abstract void createFunctionSpecificView();

	public void fill(JSONArray jsonArray) {

		try {
			JSONObject jo = jsonArray.getJSONObject(0);

			name.setText(jo.getString("name"));
			email.setText(jo.getString("email"));
			gender.setText(jo.getBoolean("gender") ? "Male" : "Female");
			campus.setText(jo.getString("campus"));
			contact.setText(jo.getString("contact"));
			address.setText(jo.getString("address"));

			String s = jo.getString("cover");
			if (s != null && !"".equals(s)) {
				byte[] cover = s.getBytes(Charset.forName("ISO-8859-1"));
				System.out.println(cover.length);
				Bitmap coverImage = BitmapFactory.decodeByteArray(cover, 0,
						cover.length);
				profileImage.setImageBitmap(coverImage);
			}
		} catch (Exception e) {
		}

		bg.setAlpha(0f);
	}

	public final void zoomInOut(View view) {
		ZoomInOutAction.action(this, profileImage);
	}

}
