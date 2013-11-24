package com.wilddynamos.bookapp.ws.remote.action;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.wilddynamos.bookapp.activity.mybooks.PostOrEditBookActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

import android.os.AsyncTask;
import android.widget.Toast;

public class GetBookDetail extends AsyncTask<String, Void, JSONArray> {

	private PostOrEditBookActivity a;
	
	public GetBookDetail(PostOrEditBookActivity a) {
		this.a = a;
	}
	
	@Override
	protected JSONArray doInBackground(String... params) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		paramsMap.put("isEdit", "1");
		
		try {
			return DataUtils.receiveJSON(
					Connection.requestByPost("/GetBookDetail", paramsMap));
			
		} catch(Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	@Override
	protected void onPostExecute(JSONArray jsonArray) {
		if(jsonArray == null || jsonArray.length() == 0)
			Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
		else
			a.fill(jsonArray);
	}
}
