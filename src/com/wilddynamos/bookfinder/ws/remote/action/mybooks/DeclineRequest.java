package com.wilddynamos.bookfinder.ws.remote.action.mybooks;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.mybooks.MyPostDetailActivity;
import com.wilddynamos.bookfinder.activity.mybooks.RequesterProfileActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

public class DeclineRequest extends AsyncTask<String, Void, Boolean> {

	private RequesterProfileActivity a;
	private int id;

	public DeclineRequest(RequesterProfileActivity a) {
		this.a = a;
	}

	@Override
	protected Boolean doInBackground(String... params) {

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("bookId", params[0]);
		id = Integer.parseInt(params[0]);
		paramsMap.put("requesterId", params[1]);

		try {
			return "1".equals(DataUtils.receiveFlag(Connection.requestByGet(
					"/DeclineRequest", paramsMap)));
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			Toast.makeText(a, "You've declined this request!",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MyPostDetailActivity.class);
			intent.putExtra("id", id);
			a.startActivity(intent);
		} else
			Toast.makeText(a, "Oops!", Toast.LENGTH_SHORT).show();
	}
}
