package com.wilddynamos.bookfinder.ws.remote.action;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.LoginActivity;
import com.wilddynamos.bookfinder.activity.profile.MyProfileActivity;
import com.wilddynamos.bookfinder.service.NotificationCenter;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

public class Logout extends AsyncTask<Void, Void, Boolean> {
	private MyProfileActivity a;

	public Logout(MyProfileActivity a) {
		this.a = a;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", Connection.id + "");

		if ("1".equals(DataUtils.receiveFlag(Connection.requestByGet("/Logout",
				paramsMap)))) {

			NotificationCenter.handler.sendEmptyMessage(-1);
			return true;
		} else
			return false;
	}

	protected void onPostExecute(Boolean success) {
		System.out.println(success);
		if (success) {
			Toast.makeText(a, "Logged out", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, LoginActivity.class);
			intent.putExtra("logout", "logout");
			a.startActivity(intent);
		} else
			Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
	}
}
