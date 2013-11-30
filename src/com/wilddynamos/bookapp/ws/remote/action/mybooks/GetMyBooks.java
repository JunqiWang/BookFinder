package com.wilddynamos.bookapp.ws.remote.action.mybooks;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.mybooks.MybooksListActivity;
import com.wilddynamos.bookapp.activity.post.PostListActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class GetMyBooks extends AsyncTask<String, Void, JSONArray> {
	
	private MybooksListActivity a;
	
	public GetMyBooks(MybooksListActivity a) {
		this.a = a;
	}

	@Override
	protected JSONArray doInBackground(String... params) {
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		
		try {
			return DataUtils.receiveJSON(
					Connection.requestByPost("/GetMyBooks", paramsMap));
			
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(JSONArray jsonArray) {
		System.out.println("###"+jsonArray == null);
		if(jsonArray == null)
			Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
		else {
			a.loadData(jsonArray);
			a.fill();
		}
	}
}
