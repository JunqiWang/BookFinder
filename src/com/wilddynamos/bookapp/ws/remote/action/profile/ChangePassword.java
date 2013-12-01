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
				new com.wilddynamos.bookapp.ws.local.ChangePassword(a).execute(params);
			} else
				Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
		}
	}
}
