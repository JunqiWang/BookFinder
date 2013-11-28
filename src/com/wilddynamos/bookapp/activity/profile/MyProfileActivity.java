package com.wilddynamos.bookapp.activity.profile;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.ChangePasswordActivity;
import com.wilddynamos.bookapp.activity.LoginActivity;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.utils.ZoomInOutAction;
import com.wilddynamos.bookapp.ws.remote.action.profile.GetMyProfile;

public class MyProfileActivity extends Activity {
	
	ImageView profileImage;
	TextView name;
	TextView gender;
	TextView campus;
	TextView contact;
	TextView address;
	Button edit;
	Button changePassword;
	Button logout;
	
	private User user;
	
//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		@Override
//    	public void handleMessage(Message msg){
//    		
//    		if(msg.what < 0)
//    			Toast.makeText(MyProfileActivity.this, "Oops!", Toast.LENGTH_SHORT).show();
//    		else if(msg.what == 1)
//    			fill();
//    		else
//    			Toast.makeText(MyProfileActivity.this, "What happened?", Toast.LENGTH_SHORT).show();
//    	}
//	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_detail);
		
		profileImage = (ImageView) findViewById(R.id.profile_image);
		name = (TextView) findViewById(R.id.name);
		gender = (TextView) findViewById(R.id.gender);
		campus = (TextView) findViewById(R.id.campus);
		contact = (TextView) findViewById(R.id.contact);
		address = (TextView) findViewById(R.id.address);
		edit = (Button) findViewById(R.id.edit_button);
		logout = (Button) findViewById(R.id.logout_button);
		
//		new GetMyProfile(this, this).start();
		GetMyProfile gmp = new GetMyProfile(this, this);
		gmp.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	public void fill() {
		name.setText(user.getName());
		gender.setText(user.getGender()? "Male" : "Female");
		campus.setText(user.getCampus());
		contact.setText(user.getContact());
		address.setText(user.getAddress());
		
		String photoPath = user.getPhotoAddr();
		if (photoPath != null) {
			System.out.println(photoPath);
			//Bitmap bmp = getBitmap(this, photoPath);
			Bitmap bmp = BitmapFactory.decodeFile(photoPath);
			profileImage.setImageBitmap(bmp);
		}
	
	}
	
	/*edit profile button*/
	public void editProfile(View view){
		Intent intent = new Intent(this, EditProfileActivity.class);
		startActivity(intent);
	}
	/* log out button */
	public void logOut(View view){
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra("logout", "logout");
		startActivity(intent);
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Bitmap getBitmap(Context ctx, String pathNameRelativeToAssetsFolder) {
		  InputStream bitmapIs = null;
		  Bitmap bmp = null;
		  try {
		    bitmapIs = ctx.getAssets().open(pathNameRelativeToAssetsFolder);
		    bmp = BitmapFactory.decodeStream(bitmapIs);
		    bitmapIs.close();
		  } catch (IOException e) {
		    // Error reading the file
		    e.printStackTrace();
		  }
		  return bmp;
		}
	public void zoomInOut(View view){
	    	ZoomInOutAction.action(this,profileImage);
		}
	public void changePassword(View view){
		Intent intent = new Intent(this, ChangePasswordActivity.class);
		startActivity(intent);
	}
}
