package com.wilddynamos.bookapp.ws.remote.action.profile;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.mybooks.RequesterDetailActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class GetRequestDetail extends AsyncTask<String, Void, JSONArray> {

	private RequesterDetailActivity a;
	
	
	public GetRequestDetail(RequesterDetailActivity a) {
		this.a = a;
	}
	
	@Override
	protected JSONArray doInBackground(String... params) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		
		try {
			return DataUtils.receiveJSON(Connection.requestByPost("/GetRequestDetail", paramsMap));
			
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(JSONArray jsonArray) {
		System.out.println(jsonArray);
		if(a != null)
			if(jsonArray == null || jsonArray.length() == 0)
				Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
			else
				a.fill(jsonArray);
	}
}