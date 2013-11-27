package com.wilddynamos.bookapp.ws.remote.action.profile;

import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.mybooks.RequesterListActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class DeclineAll extends AsyncTask<String, Void, String> {

	private RequesterListActivity a;

	
	public DeclineAll(RequesterListActivity a) {
		this.a = a;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		
		return DataUtils.receiveFlag(Connection.requestByGet("/DeclineAll", paramsMap));
	
	}

	@Override
	protected void onPostExecute(String result) {
		if(result.equals("1")) {
			Toast.makeText(a, "All requests are declined!", Toast.LENGTH_SHORT).show();
		} 
		else if (result.equals("-1"))
			Toast.makeText(a, "Oops!", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(a, "What happened?", Toast.LENGTH_SHORT).show();
	}
}

