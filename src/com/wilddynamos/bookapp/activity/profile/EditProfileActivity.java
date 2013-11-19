package com.wilddynamos.bookapp.activity.profile;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.R.layout;
import com.wilddynamos.bookapp.activity.MultiWindowActivity;

public class EditProfileActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_editprofile);   
        
    }
	
	/* save button*/
	public void save(View view){
		Intent intent = new Intent(this, MultiWindowActivity.class);
		intent.putExtra(MultiWindowActivity.TAB_SELECT, 2);
		startActivity(intent);
	}
	
	/* cancel button*/
	public void cancel(View view){
		Intent intent = new Intent(this, MultiWindowActivity.class);
		intent.putExtra(MultiWindowActivity.TAB_SELECT, 2);
		startActivity(intent);
	}
} 

