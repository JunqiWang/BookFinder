package com.wilddynamos.bookfinder.ws.remote.action.profile;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.LoginActivity;
import com.wilddynamos.bookfinder.activity.profile.ChangePasswordActivity;
import com.wilddynamos.bookfinder.dblayout.UserDataSource;
import com.wilddynamos.bookfinder.model.User;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

public class ChangePassword extends AsyncTask<String, Void, Boolean> {

	private ChangePasswordActivity a;
	
	String[] params = null;

	public ChangePassword(ChangePasswordActivity a) {
		this.a = a;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		
		this.params = params;
		
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
				new com.wilddynamos.bookfinder.ws.local.ChangePassword(a).execute(params);
			} else
				Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
		}
	}
}
