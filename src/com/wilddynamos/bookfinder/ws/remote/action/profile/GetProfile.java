package com.wilddynamos.bookfinder.ws.remote.action.profile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.BaseProfileActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

public class GetProfile extends AsyncTask<String, Void, JSONArray> {

	private BaseProfileActivity a;

	public GetProfile(BaseProfileActivity a) {
		this.a = a;
	}

	@Override
	protected JSONArray doInBackground(String... params) {		// make a connection to the server
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		if(params.length >= 2)
			paramsMap.put("bookId", params[1]);

		try {
			return DataUtils.receiveJSON(Connection.requestByPost("/Profile",
					paramsMap));

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(JSONArray jsonArray) {		// after connection, fill the relative text view and image view
		if (jsonArray == null || jsonArray.length() == 0)
			Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
		else
			a.fill(jsonArray);
	}
}