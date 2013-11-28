package com.wilddynamos.bookapp.ws.remote.action;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.LoginActivity;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class Login extends AsyncTask<String, Void, Boolean> {
	
	private LoginActivity a;
	
	public Login(LoginActivity a) {
		this.a = a;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		
		if(Connection.login(params[0], params[1]))
			return true;
		else
			return false;
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if(success) {
			Toast.makeText(a, "Logged in", Toast.LENGTH_SHORT).show();
			a.signIn();
		} else {
			Toast.makeText(a, "Failed", Toast.LENGTH_SHORT).show();
			a.block.setAlpha(0f);
		}
	}
}
