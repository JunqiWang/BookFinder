package com.wilddynamos.bookapp.ws.remote.action.profile;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.MultiWindowActivity;
import com.wilddynamos.bookapp.activity.profile.EditProfileActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class EditMyProfile extends AsyncTask<String, Void, String> {

	private EditProfileActivity a;
	private Context context;
	private int sqliteResult;
	
	public EditMyProfile(EditProfileActivity a, Context context) {
		this.a = a;
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		//update profile in sqlite
		UserDataSource userDataSource = new UserDataSource(context);
		userDataSource.open();
		
		User user = new User();
		user.setId(Integer.parseInt(params[0]));
		user.setName(params[1]);
		user.setGender(params[2].equals("Male"));
		user.setCampus(params[3]);
		user.setContact(params[4]);
		user.setAddress(params[5]);
		user.setPhotoAddr(params[6]);
		
		sqliteResult = userDataSource.updateUser(user);
		userDataSource.close();
		
		//send the new profile data to server
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		paramsMap.put("name", params[1]);
		paramsMap.put("gender", params[2]);
		paramsMap.put("campus", params[3]);
		paramsMap.put("contact", params[4]);
		paramsMap.put("address", params[5]);
		paramsMap.put("image", params[7]);
		
		return DataUtils.receiveFlag(Connection.requestByPost("/EditProfile", paramsMap));
	}

	@Override
	protected void onPostExecute(String result) {
		if ((sqliteResult != 1) && !result.equals("1"))
			Toast.makeText(a, "Both sqlite and mysql wrong!", Toast.LENGTH_SHORT).show();
		else if ((sqliteResult == 1) && !result.equals("1"))
			Toast.makeText(a, "Mysql wrong!", Toast.LENGTH_SHORT).show();
		else if ((sqliteResult != 1) && result.equals("1"))
			Toast.makeText(a, "Sqlite wrong!", Toast.LENGTH_SHORT).show();
		else if ((sqliteResult == 1) && result.equals("1")) { 	//after data updated successfully, go to my profile activity
			Toast.makeText(a, "Profile updated!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MultiWindowActivity.class);
            intent.putExtra(MultiWindowActivity.TAB_SELECT, 2);
            a.startActivity(intent);
		}
		else 
			Toast.makeText(a, "What happened?", Toast.LENGTH_SHORT).show();
			
	}
}
