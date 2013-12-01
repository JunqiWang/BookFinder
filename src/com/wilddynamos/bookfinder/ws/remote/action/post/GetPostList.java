package com.wilddynamos.bookfinder.ws.remote.action.post;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.post.PostListActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

public class GetPostList extends AsyncTask<String, Void, JSONArray> {
	
	private PostListActivity a;
	
	private String currentPage;
	
	public GetPostList(PostListActivity a) {
		this.a = a;
	}

	@Override
	protected JSONArray doInBackground(String... params) {		// make a connection to the server
		this.currentPage = params[0];
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("currentPage", params[0]);
		paramsMap.put("sOrR", params[1]);
		paramsMap.put("search", params[2]);
		paramsMap.put("id", Connection.id + "");
		
		try {
			return DataUtils.receiveJSON(
					Connection.requestByPost("/GetPosts", paramsMap));
			
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(JSONArray jsonArray) {		// after connection load data or just pour
		if(jsonArray == null)
			Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
		else {
			if("1".equals(currentPage))
				a.pour(jsonArray);
			else {
				a.loadData(jsonArray);
			}
		}
	}
}
