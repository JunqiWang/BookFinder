package com.wilddynamos.bookfinder.ws.local;

import android.os.AsyncTask;

import com.wilddynamos.bookfinder.activity.profile.MyProfileActivity;
import com.wilddynamos.bookfinder.dblayout.UserDataSource;
import com.wilddynamos.bookfinder.model.User;
import com.wilddynamos.bookfinder.ws.remote.Connection;
import com.wilddynamos.bookfinder.ws.remote.action.profile.GetProfile;

/** Servlet to get profile information **/
public class GetMyProfile extends AsyncTask<Void, Void, User> {

	private MyProfileActivity a;

	public GetMyProfile(MyProfileActivity a) {
		this.a = a;
	}

	/** Connect with sever's datbase to get profile **/
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
