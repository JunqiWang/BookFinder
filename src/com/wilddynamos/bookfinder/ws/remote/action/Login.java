package com.wilddynamos.bookfinder.ws.remote.action;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.LoginActivity;
import com.wilddynamos.bookfinder.dblayout.UserDataSource;
import com.wilddynamos.bookfinder.ws.remote.Connection;

/** Login Action **/
public class Login extends AsyncTask<String, Void, Boolean> {

	private LoginActivity a;
	private String email;
	private String password;

	public Login(LoginActivity a) {
		this.a = a;
	}

	/** pass login infromation to server **/
	@Override
	protected Boolean doInBackground(String... params) {

		email = params[0];
		password = params[1];

		if (Connection.login(params[0], params[1]))
			return true;
		else
			return false;
	}

	/** post result and handel exception **/
	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			Toast.makeText(a, "Logged in", Toast.LENGTH_SHORT).show();
			UserDataSource userDataSource = new UserDataSource(a);
			userDataSource.open();
			if (userDataSource.getUser(Connection.id) == null)
				userDataSource.createUser(Connection.id, email, password, null,
						null, null, null, null, null);
			userDataSource.close();
			a.signIn();
		} else {
			Toast.makeText(a, "Failed", Toast.LENGTH_SHORT).show();
			a.block.setAlpha(0f);
		}
	}
}
