package com.wilddynamos.bookapp.ws.remote.action.profile;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.mybooks.MyPostDetailActivity;
import com.wilddynamos.bookapp.activity.mybooks.RequesterListActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class AcceptRequest extends AsyncTask<String, Void, String> {

	private RequesterListActivity a;
	private int id;
	
	public AcceptRequest(RequesterListActivity a) {
		this.a = a;
	}
	
	@Override
	protected String doInBackground(String... params) {		//send the accepted book_id and requester_id to server 
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("bookId", params[0]);
		id = Integer.parseInt(params[0]);
		paramsMap.put("requesterId", params[1]);
		
		return DataUtils.receiveFlag(Connection.requestByGet("/AcceptRequest", paramsMap));
	
	}

	@Override
	protected void onPostExecute(String result) {
		if(result.equals("1")) {		//after successfully accepting the request, go to book detail activity
			Toast.makeText(a, "You've accepted the request!", Toast.LENGTH_SHORT).show();
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

