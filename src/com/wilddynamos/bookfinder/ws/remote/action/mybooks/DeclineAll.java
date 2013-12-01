package com.wilddynamos.bookfinder.ws.remote.action.mybooks;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.mybooks.MyPostDetailActivity;
import com.wilddynamos.bookfinder.activity.mybooks.RequesterListActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

public class DeclineAll extends AsyncTask<String, Void, String> {

	private RequesterListActivity a;

	
	public DeclineAll(RequesterListActivity a) {
		this.a = a;
	}
	
	private int id;
	@Override
	protected String doInBackground(String... params) {		//send book_id to server
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		id = Integer.parseInt(params[0]);
		return DataUtils.receiveFlag(Connection.requestByGet("/DeclineAll", paramsMap));
	
	}

	@Override
	protected void onPostExecute(String result) {
		if(result.equals("1")) {	//after successfully declined all requests, go to book detail activity
			Toast.makeText(a, "All requests are declined!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MyPostDetailActivity.class);
            intent.putExtra("id", id);
            a.startActivity(intent);
		} 
		else if (result.equals("-1"))
			Toast.makeText(a, "Oops!", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(a, "What happened?", Toast.LENGTH_SHORT).show();
	}
}

