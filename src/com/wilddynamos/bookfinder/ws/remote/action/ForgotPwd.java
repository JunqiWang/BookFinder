package com.wilddynamos.bookfinder.ws.remote.action;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.os.AsyncTask;

import com.wilddynamos.bookfinder.activity.LoginActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

/** Forget password action **/
public class ForgotPwd extends AsyncTask<String, Void, Boolean> {

	private LoginActivity a;

	public ForgotPwd(LoginActivity a) {
		this.a = a;
	}

	/** Connect with sever to do forgetpassword action **/
	@Override
	protected Boolean doInBackground(String... params) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("email", params[0]);

		try {

			String flag = DataUtils.receiveFlag(Connection.requestByPost(
					"/ForgotPwd", paramsMap));
			if ("1".equals(flag))
				return true;
			else if ("-1".equals(flag))
				return false;
			else
				return null;

		} catch (Exception e) {
			return null;
		}
	}

	/** handle result and action **/
	@Override
	protected void onPostExecute(Boolean success) {
		if (success != null && success)
			new AlertDialog.Builder(a)
					.setTitle("Check your email for temporary password.")
					.setPositiveButton("Ok", null).show();
		else if (success != null && !success)
			new AlertDialog.Builder(a)
					.setTitle("Wrong email, have you registered?")
					.setPositiveButton("OK", null).show();
		else
			new AlertDialog.Builder(a).setTitle("Oops! What happened?")
					.setPositiveButton("OK", null).show();
	}

}
