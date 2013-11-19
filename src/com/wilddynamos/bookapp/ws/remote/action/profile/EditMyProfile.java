package com.wilddynamos.bookapp.ws.remote.action.profile;

import android.content.Context;

import com.wilddynamos.bookapp.activity.profile.EditProfileActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;

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
			userDataSource.updateUser(user);
			userDataSource.close();
			
			a.getHandler().sendEmptyMessage(1);
			
		} catch(Exception e) {
			a.getHandler().sendEmptyMessage(-1);
		}
	}

}
