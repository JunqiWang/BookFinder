package com.wilddynamos.bookapp.ws.remote.action.profile;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.LoginActivity;
import com.wilddynamos.bookapp.activity.profile.ChangePasswordActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class ChangePassword extends AsyncTask<String, Void, Boolean> {

	private ChangePasswordActivity a;
	private String newPassword; 

	public ChangePassword(ChangePasswordActivity a) {
		this.a = a;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		
		newPassword = params[1];
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("oldPassword", params[0]);
		paramsMap.put("newPassword", params[1]);
		paramsMap.put("id", Connection.id + "");
		
		String flag = null;
		try {
			flag = DataUtils.receiveFlag(Connection.requestByPost("/ChangePwd",
					paramsMap));
		} catch (Exception e) {
			return false;
		}

		if ("1".equals(flag))
			return true;
		else if ("-1".equals(flag))
			return null;
		else
			return false;

	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success == null)
			Toast.makeText(a, "Wrong password", Toast.LENGTH_SHORT).show();
		else {
			if (success) {
				Toast.makeText(
						a,
						"Your password has been changed.\nYou need to re-login using your new password",
						Toast.LENGTH_SHORT).show();
				UserDataSource userDataSource = new UserDataSource(a);
				userDataSource.open();
				
				User user = new User();
				user.setId(Connection.id);
				user.setPassword(newPassword);
				
				userDataSource.updateUser(user);
				userDataSource.close();

				Intent intent = new Intent(a, LoginActivity.class);
				intent.putExtra("logout", "logout");
				a.startActivity(intent);
			} else
				Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
		}
	}
}
