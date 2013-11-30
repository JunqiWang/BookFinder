package com.wilddynamos.bookapp.ws.remote.action.profile;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.profile.MyProfileActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class GetMyProfile extends AsyncTask<String, Void, Boolean> {

	private MyProfileActivity a;
	private Context context;
	
	
	public GetMyProfile(MyProfileActivity a, Context context) {
		this.a = a;
		this.context = context;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {		//
		UserDataSource userDataSource = new UserDataSource(context);
		userDataSource.open();
		User user = userDataSource.getUser(Connection.id);
		a.setUser(user);
		userDataSource.close();
		
		return (user != null);
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if(success) {
			a.fill();
		} else
			Toast.makeText(a, "Oops!", Toast.LENGTH_SHORT).show();
	}
}

