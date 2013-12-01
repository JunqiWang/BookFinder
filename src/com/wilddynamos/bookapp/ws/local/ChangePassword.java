package com.wilddynamos.bookapp.ws.local;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.LoginActivity;
import com.wilddynamos.bookapp.activity.profile.ChangePasswordActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class ChangePassword extends AsyncTask<String, Void, Integer> {

	private ChangePasswordActivity a;
	
	public ChangePassword(ChangePasswordActivity a) {
		this.a = a;
	}

	@Override
	protected Integer doInBackground(String... params) {
		//TODO
		UserDataSource userDataSource = new UserDataSource(a);
		userDataSource.open();
		
		User user = new User();
		user.setId(Connection.id);
		user.setPassword(params[1]);
		
		int updateResult = userDataSource.updateUser(user);
		userDataSource.close();
		
		return updateResult;
	}

	@Override
	protected void onPostExecute(Integer result) {
		if (result != 1) {
			Toast.makeText(
			a,
			"Your password has been changed.\nYou need to re-login using your new password",
			Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, LoginActivity.class);
			intent.putExtra("logout", "logout");
			a.startActivity(intent);
		}
		else
			Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
			
	}
}
