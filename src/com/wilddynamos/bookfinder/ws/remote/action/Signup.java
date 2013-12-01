package com.wilddynamos.bookfinder.ws.remote.action;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.MultiWindowActivity;
import com.wilddynamos.bookfinder.activity.SignupActivity;
import com.wilddynamos.bookfinder.dblayout.UserDataSource;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

/** Sign Up action **/
public class Signup extends AsyncTask<String, Void, String> {

	private SignupActivity a;

	private String email;
	private String name;
	private String password;

	public Signup(SignupActivity a) {
		this.a = a;
	}

	/** pass params to server **/
	@Override
	protected String doInBackground(String... params) { // background: send
														// email, name and pwd
														// to server to signup
														// as a new user
		Map<String, String> paramsMap = new HashMap<String, String>();

		email = params[0];
		name = params[1];
		password = params[2];

		paramsMap.put("email", email);
		paramsMap.put("name", name);
		paramsMap.put("password", password);

		return DataUtils.receiveFlag(Connection.requestByPost("/Signup",
				paramsMap));
	}

	/** post result and exception **/
	@Override
	protected void onPostExecute(String result) {
		if ("1".equals(result)) {

			new Thread() { // new thread to add new user into sqlite
				@Override
				public void run() {
					Connection.login(email, password);
					UserDataSource userDataSource = new UserDataSource(a);
					userDataSource.open();
					userDataSource.createUser(Connection.id, email, password,
							name, null, null, null, null, null);
					userDataSource.close();
				}
			}.start();

			Toast.makeText(a, "Welcome, new friend!", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(a, MultiWindowActivity.class); // same as
																		// pages
																		// after
																		// login
			a.startActivity(intent);
		} else if (result.equals("-2"))
			Toast.makeText(a, "Email already exists!", Toast.LENGTH_SHORT)
					.show();
		else
			Toast.makeText(a, "Failed", Toast.LENGTH_SHORT).show();

	}
}
