package com.wilddynamos.bookapp.ws.local;

import android.os.AsyncTask;

import com.wilddynamos.bookapp.activity.profile.MyProfileActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.ws.remote.Connection;
import com.wilddynamos.bookapp.ws.remote.action.profile.GetProfile;

public class GetMyProfile extends AsyncTask<Void, Void, User> {

	private MyProfileActivity a;

	public GetMyProfile(MyProfileActivity a) {
		this.a = a;
	}

	@Override
	protected User doInBackground(Void... params) {
		UserDataSource userDataSource = new UserDataSource(a);
		userDataSource.open();
		User user = userDataSource.getUser(Connection.id);
		userDataSource.close();

		return user;
	}

	@Override
	protected void onPostExecute(User user) {
		if (user != null)
			a.fill(user);
		else
			new GetProfile(a).execute(new String[] { Connection.id + "" });
	}
}
