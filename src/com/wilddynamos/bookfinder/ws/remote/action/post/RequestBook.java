package com.wilddynamos.bookfinder.ws.remote.action.post;

import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.post.PostDetailActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

public class RequestBook extends AsyncTask<String, Void, Boolean> {
	
	private PostDetailActivity a;
	
	public RequestBook(PostDetailActivity a) {
		this.a = a;
	}

	@Override
	protected Boolean doInBackground(String... params) {			// make a connection to the server
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		paramsMap.put("message", params[1]);
		
		try {
			if(DataUtils.receiveFlag(Connection
					.requestByPost("/RequestBook", paramsMap)).equals("1"))
				return true;
			else
				return false;
			
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if(success)
			a.requestSuccess();					
		else
			Toast.makeText(a, "Oops!", Toast.LENGTH_SHORT).show();
	}
}
