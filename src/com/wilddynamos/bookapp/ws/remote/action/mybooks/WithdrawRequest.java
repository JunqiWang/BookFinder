package com.wilddynamos.bookapp.ws.remote.action.mybooks;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.MultiWindowActivity;
import com.wilddynamos.bookapp.activity.mybooks.MyRequestDetailActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class WithdrawRequest extends AsyncTask<String, Void, String> {

	private MyRequestDetailActivity a;
	
	public WithdrawRequest(MyRequestDetailActivity a) {
		this.a = a;
	}
	
	@Override
	protected String doInBackground(String... params) {	 	//asynctask background, connect to server
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("bookId", params[0]);
		paramsMap.put("requesterId", params[1]);
		
		return DataUtils.receiveFlag(Connection.requestByPost("/WithdrawRequest", paramsMap));

	}

	@Override
	protected void onPostExecute(String result) {			//after get result from server 
		if(result.equals("1")) {
			Toast.makeText(a, "Request withdrew!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MultiWindowActivity.class);	//go to mybooks if get withdrew
			intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
			a.startActivity(intent);
		}
		else
			Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
	}
}
