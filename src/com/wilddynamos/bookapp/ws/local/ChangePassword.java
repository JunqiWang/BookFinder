package com.wilddynamos.bookapp.ws.local;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.profile.ChangePasswordActivity;
import com.wilddynamos.bookapp.activity.profile.MyProfileActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.ws.remote.Connection;
import com.wilddynamos.bookapp.ws.remote.action.profile.GetProfile;

public class ChangePassword extends AsyncTask<String, Void, Boolean> {

	private ChangePasswordActivity a;
	
	String[] params = null;

	public ChangePassword(ChangePasswordActivity a) {
		this.a = a;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		//TODO
		int i;
		
		this.params = new String[] { params[0], params[1] };

		return true;
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success)
			new com.wilddynamos.bookapp.ws.remote.action.profile.ChangePassword(a).execute(params);
		else
			Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
			
	}
}
