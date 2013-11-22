package com.wilddynamos.bookapp.ws.remote.action.profile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.wilddynamos.bookapp.activity.profile.EditProfileActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class EditMyProfile extends Thread {
	
	private EditProfileActivity a;
	private Context context;
	private User user;
	
	public EditMyProfile(EditProfileActivity a, Context context, User user) {
		this.a = a;
		this.context = context;
		this.user = user;
	}
	
	public void run() {
		try {
			UserDataSource userDataSource = new UserDataSource(context);
			userDataSource.open();
			int sqliteResult = userDataSource.updateUser(user);
			userDataSource.close();
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", user.getName());
			params.put("gender", Boolean.toString(user.getGender()));
			params.put("campus", user.getCampus());
			params.put("contact", user.getContact());
			params.put("address", user.getAddress());
			
			InputStream is = Connection.requestByPost("/EditProfile", params);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String result = br.readLine();
			
			if ((sqliteResult == 1) && result.equals("1"))
				a.getHandler().sendEmptyMessage(1);
			else a.getHandler().sendEmptyMessage(-1);
			
		} catch(Exception e) {
			e.printStackTrace();
			a.getHandler().sendEmptyMessage(-1);
		}
	}

}
