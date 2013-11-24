package com.wilddynamos.bookapp.ws.remote.action.profile;

import android.content.Context;

import com.wilddynamos.bookapp.activity.profile.MyProfileActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.ws.remote.Connection;


public class GetMyProfile extends Thread {
	
	private MyProfileActivity a;
	private Context context;
	
	public GetMyProfile(MyProfileActivity a, Context context) {
		this.a = a;
		this.context = context;
	}
	
	public void run() {
		try {
			UserDataSource userDataSource = new UserDataSource(context);
			userDataSource.open();
			a.setUser(userDataSource.getUser(Connection.id));
			userDataSource.close();
			
			a.getHandler().sendEmptyMessage(1);
			
		} catch(Exception e) {
			a.getHandler().sendEmptyMessage(-1);
		}
	}
}
