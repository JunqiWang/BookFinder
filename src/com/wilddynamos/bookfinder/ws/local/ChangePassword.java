package com.wilddynamos.bookfinder.ws.local;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.LoginActivity;
import com.wilddynamos.bookfinder.activity.profile.ChangePasswordActivity;
import com.wilddynamos.bookfinder.dblayout.UserDataSource;
import com.wilddynamos.bookfinder.model.User;
import com.wilddynamos.bookfinder.ws.remote.Connection;

/** Servlet for change password **/
public class ChangePassword extends AsyncTask<String, Void, Integer> {

	private ChangePasswordActivity a;

	public ChangePassword(ChangePasswordActivity a) {
		this.a = a;
	}

	/** connect to server to change password **/
	@Override
	protected Integer doInBackground(String... params) {
		// TODO
		UserDataSource userDataSource = new UserDataSource(a);
		userDataSource.open();

		User user = new User();
		user.setId(Connection.id);
		user.setPassword(params[1]);

		int updateResult = userDataSource.updateUserPassword(user);
		userDataSource.close();

		return updateResult;
	}

	/** work after successfully change password **/
	@Override
	protected void onPostExecute(Integer result) {
		if (result == 1) {
			Toast.makeText(
					a,
					"Your password has been changed.\nYou need to re-login using your new password",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, LoginActivity.class);
			intent.putExtra("logout", "logout");
			a.startActivity(intent);
		} else
			Toast.makeText(a, "Ooops", Toast.LENGTH_SHORT).show();

	}
}
