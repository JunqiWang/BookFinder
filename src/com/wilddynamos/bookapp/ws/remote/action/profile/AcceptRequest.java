package com.wilddynamos.bookapp.ws.remote.action.profile;

import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.mybooks.RequesterListActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class AcceptRequest extends AsyncTask<String, Void, String> {

	private RequesterListActivity a;

	
	public AcceptRequest(RequesterListActivity a) {
		this.a = a;
	}
	///since long time ago there gonna be a lonely
	@Override
	protected String doInBackground(String... params) {
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("bookId", params[0]);
		paramsMap.put("requesterId", params[1]);
		
		return DataUtils.receiveFlag(Connection.requestByGet("/AcceptRequest", paramsMap));
	
	}

	@Override
	protected void onPostExecute(String result) {
		if(result.equals("1")) {
			Toast.makeText(a, "You've accepted the request!", Toast.LENGTH_SHORT).show();
		} 
		else if (result.equals("-1"))
			Toast.makeText(a, "Oops!", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(a, "What happened?", Toast.LENGTH_SHORT).show();
	}
}

