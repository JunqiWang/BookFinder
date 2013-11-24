package com.wilddynamos.bookapp.ws.remote.action.profile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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
	private byte[] byteArray;
	
	public EditMyProfile(EditProfileActivity a, Context context, User user, byte[] byteArray) {
		this.a = a;
		this.context = context;
		this.user = user;
		this.byteArray = byteArray;
	}
	
	public void run() {
		try {
			UserDataSource userDataSource = new UserDataSource(context);
			userDataSource.open();
			int sqliteResult = userDataSource.updateUser(user);
			userDataSource.close();
			//System.out.println(sqliteResult);
			System.out.println(byteArray.length);
			String imageString = new String(byteArray, Charset.forName("ISO-8859-1"));
			System.out.println(imageString.length());
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", String.valueOf(user.getId()));
			params.put("name", user.getName());
			params.put("gender", user.getGender() ? "M" : "F");
			params.put("campus", user.getCampus());
			params.put("contact", user.getContact());
			params.put("address", user.getAddress());
			params.put("image",imageString);
			
			InputStream is = Connection.requestByPost("/EditProfile", params);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String result = br.readLine();
			
			if ((sqliteResult != 1) && !result.equals("1"))
				a.getHandler().sendEmptyMessage(-2);
			else if ((sqliteResult == 1) && !result.equals("1"))
				a.getHandler().sendEmptyMessage(-3);
			else if ((sqliteResult != 1) && result.equals("1"))
				a.getHandler().sendEmptyMessage(-4);
			else if ((sqliteResult == 1) && result.equals("1"))
				a.getHandler().sendEmptyMessage(1);
		} catch(Exception e) {
			e.printStackTrace();
			a.getHandler().sendEmptyMessage(-1);
		}
	}

}
